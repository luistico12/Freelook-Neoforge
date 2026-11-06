package com.example.freelook;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Rein clientseitige Mod (dist = CLIENT): wird auf dedizierten Servern gar nicht erst geladen.
 */
@Mod(value = Freelook.MODID, dist = Dist.CLIENT)
public class Freelook {
    public static final String MODID = "freelook";

    public Freelook(IEventBus modEventBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, FreelookConfig.SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        modEventBus.addListener(FreelookClient::registerKeys);
        NeoForge.EVENT_BUS.register(FreelookClient.class);
    }
}
