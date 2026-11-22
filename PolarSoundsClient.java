package com.polarstick.polarsounds;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

/**
 * Polar Sounds - gdy awansujesz poziom doświadczenia, odtwarza mocniejszą
 * fanfarę i odpala mały pokaz iskier. Wyłącznie kosmetyczne wzmocnienie
 * istniejącego dźwięku awansu, bez żadnej ingerencji w rozgrywkę.
 */
public class PolarSoundsClient implements ClientModInitializer {
    private int lastLevel = -1;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(Minecraft client) {
        LocalPlayer player = client.player;
        ClientLevel level = client.level;
        if (player == null || level == null) {
            return;
        }
        int current = player.experienceLevel;
        if (lastLevel == -1) {
            lastLevel = current;
            return;
        }
        if (current > lastLevel) {
            double x = player.getX();
            double y = player.getY() + 1.0;
            double z = player.getZ();
            level.playLocalSound(x, y, z, SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS,
                    1.0f, 1.0f, false);
            for (int i = 0; i < 12; i++) {
                double ox = (level.random.nextDouble() - 0.5) * 1.2;
                double oy = level.random.nextDouble() * 1.4;
                double oz = (level.random.nextDouble() - 0.5) * 1.2;
                level.addParticle(ParticleTypes.FIREWORK, x + ox, y + oy, z + oz, 0.0, 0.05, 0.0);
            }
        }
        lastLevel = current;
    }
}
