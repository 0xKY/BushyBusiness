package me.kaloyankys.bushybusiness.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class LeafParticle extends SpriteBillboardParticle {
    private final float rotationSpeed;
    private final SpriteProvider spriteProvider;

    public LeafParticle(ClientWorld world, double x, double y, double z, float colorRed, float colorGreen, float colorBlue, SpriteProvider spriteProvider) {
        super(world, x, y, z);
        this.spriteProvider = spriteProvider;
        this.red = colorRed;
        this.green = colorGreen;
        this.blue = colorBlue;
        int i = (int) (32.0 / (Math.random() * 0.8 + 0.2));
        this.maxAge = 800;
        this.setSpriteForAge(spriteProvider);
        this.rotationSpeed = ((float) Math.random() - 0.5f) * 0.1f;
        this.angle = (float) Math.random() * ((float) Math.PI * 2);
        this.scale *= 1.5 + random.nextFloat() * 1.5f;
        this.gravityStrength /= 2;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getSize(float tickDelta) {
        return this.scale * MathHelper.clamp(((float) this.age + tickDelta) / (float) this.maxAge * 32.0f, 0.0f, 1.0f);
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
            return;
        }
        this.setSpriteForAge(this.spriteProvider);
        this.prevAngle = this.angle;
        this.angle += (float) Math.PI * this.rotationSpeed * 2.0f;
        this.move(this.velocityX, this.velocityY, this.velocityZ);
        this.velocityY -= 0.003f;
        this.velocityY = Math.max(this.velocityY, -0.14f);
        this.velocityX += 0.001f;
        this.velocityZ += 0.001f;
        float upperAngle = this.angle + 0.1f;
        float lowerAngle = this.angle - 0.1f;
        if (!this.onGround) {
            if (this.angle < upperAngle) {
                this.angle += 0.0002f;
            } else if (this.angle > lowerAngle) {
                this.angle -= 0.0002f;
            }
        } else {
            this.angle = this.prevAngle;
        }
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        private final Map<Block, Integer> SPECIAL_COLOURS = Map.of(Blocks.SPRUCE_LEAVES, FoliageColors.getSpruceColor(), Blocks.BIRCH_LEAVES, FoliageColors.getBirchColor(), Blocks.AZALEA_LEAVES, FoliageColors.getDefaultColor() + 100);

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        @Nullable
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            BlockPos pos = new BlockPos(x, y, z);
            BlockState state = world.getBlockState(new BlockPos(x, y, z));
            int colour = MinecraftClient.getInstance().getBlockColors().getParticleColor(state, world, pos);
            LeafParticle leaf = this.getLeaf(world, x, y, z, spriteProvider, colour);
            if (state.getBlock() instanceof LeavesBlock) {
                for (Map.Entry<Block, Integer> entry : SPECIAL_COLOURS.entrySet()) {
                    if (state.getBlock() == entry.getKey()) {
                        leaf = this.getLeaf(world, x, y, z, spriteProvider, entry.getValue());
                    }
                }
            } return leaf;
        }

        private LeafParticle getLeaf(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, int blockColor) {
            float k = (float) (blockColor >> 16 & 0xFF) / 255.0f;
            float l = (float) (blockColor >> 8 & 0xFF) / 255.0f;
            float m = (float) (blockColor & 0xFF) / 255.0f;
            return new LeafParticle(world, x, y, z, k, l, m, spriteProvider);
        }
    }
}

