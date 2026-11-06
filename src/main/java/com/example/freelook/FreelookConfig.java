package com.example.freelook;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class FreelookConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue TOGGLE_MODE = BUILDER
            .comment("true = Taste einmal druecken schaltet Freelook um, false = Taste gedrueckt halten")
            .define("toggleMode", false);

    public static final ModConfigSpec.DoubleValue CAMERA_DISTANCE = BUILDER
            .comment("Maximaler Abstand der Kamera zum Spieler (Vanilla F5 = 4.0)")
            .defineInRange("cameraDistance", 4.0D, 1.0D, 16.0D);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private FreelookConfig() {}
}
