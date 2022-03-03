package me.kaloyankys.bushybusiness;

import me.kaloyankys.bushybusiness.client.PublicDefaultParticleType;
import net.fabricmc.api.ModInitializer;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Bushybusiness implements ModInitializer {
    public static final DefaultParticleType LEAF = new PublicDefaultParticleType(true);
    @Override
    public void onInitialize() {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier("bushybusiness", "leaf"), LEAF);
    }
}
