package com.example.freelook;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.lwjgl.glfw.GLFW;

/**
 * Freelook-Logik.
 *
 * Funktionsweise:
 *  - Beim Aktivieren merken wir uns die aktuelle Blickrichtung des Spielers ("locked").
 *  - Solange Freelook aktiv ist, wird jede Mausbewegung, die Vanilla auf den Spieler anwendet,
 *    pro Frame abgegriffen, auf eine eigene Kamera-Rotation addiert und die Spieler-Rotation
 *    sofort wieder auf den gelockten Wert zurueckgesetzt. Der Spieler (Koerper/Kopf) dreht sich
 *    dadurch nie, und der Server bekommt auch nie eine geaenderte Blickrichtung.
 *  - Die Kamera wird um den Spieler herum positioniert; Camera#getMaxZoom (Vanilla-Kollision,
 *    gleiche Logik wie bei F5) sorgt dafuer, dass sie nicht durch Bloecke geht.
 */
public final class FreelookClient {
    public static final KeyMapping FREELOOK_KEY = new KeyMapping(
            "key.freelook.freelook",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_ALT,
            "key.categories.freelook");

    private static boolean active = false;
    private static boolean toggled = false;

    private static float lockYaw;
    private static float lockPitch;
    private static float camYaw;
    private static float camPitch;
    private static CameraType previousCameraType = CameraType.FIRST_PERSON;

    private FreelookClient() {}

    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(FREELOOK_KEY);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || mc.level == null) {
            toggled = false;
            stop(mc);
            return;
        }

        boolean want;
        if (FreelookConfig.TOGGLE_MODE.get()) {
            while (FREELOOK_KEY.consumeClick()) {
                toggled = !toggled;
            }
            want = toggled;
        } else {
            toggled = false;
            // Klicks verwerfen, damit sie sich nicht aufstauen
            while (FREELOOK_KEY.consumeClick()) { /* leeren */ }
            want = FREELOOK_KEY.isDown();
        }

        // In Menues (z. B. Inventar) wird Freelook beendet
        if (mc.screen != null) {
            want = false;
            toggled = false;
        }

        if (want && !active) {
            start(mc, player);
        } else if (!want && active) {
            stop(mc);
        }
    }

    @SubscribeEvent
    public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        if (!active) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }

        // 1) Mausbewegung abgreifen und Spieler-Rotation zuruecksetzen
        float dYaw = player.getYRot() - lockYaw;
        float dPitch = player.getXRot() - lockPitch;
        if (dYaw != 0.0F || dPitch != 0.0F) {
            camYaw += dYaw;
            camPitch = Mth.clamp(camPitch + dPitch, -90.0F, 90.0F);
            player.setYRot(lockYaw);
            player.setXRot(lockPitch);
            player.yRotO = lockYaw;
            player.xRotO = lockPitch;
        }

        // 2) Kamera um den Spieler positionieren (mit Block-Kollision)
        Camera camera = event.getCamera();
        float partialTick = (float) event.getPartialTick();
        Vec3 eye = player.getEyePosition(partialTick);

        camera.setPosition(eye);
        camera.setRotation(camYaw, camPitch);
        camera.move(-camera.getMaxZoom(FreelookConfig.CAMERA_DISTANCE.get().floatValue()), 0.0F, 0.0F);

        event.setYaw(camYaw);
        event.setPitch(camPitch);
    }

    private static void start(Minecraft mc, LocalPlayer player) {
        active = true;
        lockYaw = player.getYRot();
        lockPitch = player.getXRot();
        camYaw = lockYaw;
        camPitch = lockPitch;

        previousCameraType = mc.options.getCameraType();
        // In der Ego-Perspektive wuerde der Spieler nicht gerendert und die Kamera wuerde
        // nicht "abgekoppelt" -> auf Third Person umschalten.
        if (previousCameraType.isFirstPerson()) {
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        }
    }

    private static void stop(Minecraft mc) {
        if (!active) {
            return;
        }
        active = false;
        mc.options.setCameraType(previousCameraType);
    }
}
