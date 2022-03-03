package me.kaloyankys.bushybusiness.client;

import me.kaloyankys.bushybusiness.Bushybusiness;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class BushybusinessClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(Bushybusiness.LEAF, LeafParticle.Factory::new);
    }
}
