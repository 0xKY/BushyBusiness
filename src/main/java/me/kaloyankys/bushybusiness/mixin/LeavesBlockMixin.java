package me.kaloyankys.bushybusiness.mixin;

import me.kaloyankys.bushybusiness.Bushybusiness;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin extends Block {
    public LeavesBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        if (random.nextInt(30) == 0) {
            this.leafFall(world, pos, random);
        }
    }

    @Override
    protected void spawnBreakParticles(World world, PlayerEntity player, BlockPos pos, BlockState state) {
        Random random = new Random();
        for (int i = random.nextInt(4); i > 0; i--) {
            this.leafFall(world, pos, random);
        }
        super.spawnBreakParticles(world, player, pos, state);
    }

    private void leafFall(World world, BlockPos pos, Random random) {
        if (random.nextInt(2) == 0) {
            world.addParticle(Bushybusiness.LEAF, true, pos.getX(), pos.getY(), pos.getZ(),
                    0.0, +0.01, 0.0);
        }
    }
}
