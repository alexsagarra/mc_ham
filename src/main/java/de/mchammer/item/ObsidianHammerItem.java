package de.mchammer.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObsidianHammerItem extends HammerItem {

	private static final int MAX_RADIUS = 2;

	public ObsidianHammerItem(Settings settings, float effectiveSpeed, float bonusAttackDamage) {
		super(settings, effectiveSpeed, bonusAttackDamage);
	}

	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		boolean result = super.postMine(stack, world, state, pos, miner);

		if (!world.isClient() && world instanceof ServerWorld serverWorld) {
			strikeLightning(serverWorld, pos);
			destroySurroundingBlocks(serverWorld, pos, miner);
		}

		return result;
	}

	@Override
	public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		super.postHit(stack, target, attacker);

		if (attacker instanceof ServerPlayerEntity player
				&& player.getEntityWorld() instanceof ServerWorld serverWorld) {
			strikeLightning(serverWorld, target.getBlockPos());
		}
	}

	private void strikeLightning(ServerWorld world, BlockPos pos) {
		LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
		lightning.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
		world.spawnEntity(lightning);
	}

	private void destroySurroundingBlocks(ServerWorld world, BlockPos center, LivingEntity miner) {
		int count = 2 + world.random.nextInt(4); // 2 bis 5 Blöcke

		List<BlockPos> candidates = new ArrayList<>();
		for (BlockPos p : BlockPos.iterate(
				center.add(-MAX_RADIUS, -1, -MAX_RADIUS),
				center.add(MAX_RADIUS, 1, MAX_RADIUS))) {
			if (!p.equals(center) && !world.getBlockState(p).isAir()) {
				BlockState bs = world.getBlockState(p);
				if (bs.getHardness(world, p) >= 0.0f) {
					candidates.add(p.toImmutable());
				}
			}
		}

		Collections.shuffle(candidates);
		candidates.stream().limit(count).forEach(p -> world.breakBlock(p, true, miner));
	}
}
