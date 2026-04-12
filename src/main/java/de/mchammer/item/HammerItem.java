package de.mchammer.item;

import de.mchammer.McHammerMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class HammerItem extends Item {
	private static final TagKey<Block> HAMMER_MINEABLE = TagKey.of(
		RegistryKeys.BLOCK,
		Identifier.of(McHammerMod.MOD_ID, "hammer_mineable")
	);
	private static final ThreadLocal<Boolean> AOE_ACTIVE = ThreadLocal.withInitial(() -> false);

	private final float effectiveSpeed;
	private final float bonusAttackDamage;

	public HammerItem(Settings settings, float effectiveSpeed, float bonusAttackDamage) {
		super(settings);
		this.effectiveSpeed = effectiveSpeed;
		this.bonusAttackDamage = bonusAttackDamage;
	}

	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		if (state.isIn(HAMMER_MINEABLE)) {
			return effectiveSpeed;
		}
		return 1.5f;
	}

	@Override
	public float getMiningSpeed(ItemStack stack, BlockState state) {
		return getMiningSpeedMultiplier(stack, state);
	}

	@Override
	public boolean isCorrectForDrops(ItemStack stack, BlockState state) {
		return true;
	}

	@Override
	public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.damage(1, attacker, EquipmentSlot.MAINHAND);
		if (attacker instanceof ServerPlayerEntity player && player.getEntityWorld() instanceof ServerWorld serverWorld) {
			target.damage(serverWorld, player.getDamageSources().playerAttack(player), bonusAttackDamage);
		}
	}

	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		if (!world.isClient() && state.getHardness(world, pos) != 0.0f) {
			stack.damage(1, miner, EquipmentSlot.MAINHAND);
			mineAdjacentBlocks(world, pos, miner);
		}
		return true;
	}

	private void mineAdjacentBlocks(World world, BlockPos centerPos, LivingEntity miner) {
		if (AOE_ACTIVE.get()) {
			return;
		}

		Direction facing = miner.getHorizontalFacing();
		Direction left = facing.rotateYCounterclockwise();
		Direction right = facing.rotateYClockwise();

		AOE_ACTIVE.set(true);
		try {
			breakIfPossible(world, centerPos.offset(left), miner);
			breakIfPossible(world, centerPos.offset(right), miner);
		} finally {
			AOE_ACTIVE.set(false);
		}
	}

	private void breakIfPossible(World world, BlockPos pos, LivingEntity miner) {
		BlockState adjacent = world.getBlockState(pos);
		if (adjacent.isAir() || adjacent.getHardness(world, pos) < 0.0f) {
			return;
		}

		if (miner instanceof ServerPlayerEntity player) {
			player.interactionManager.tryBreakBlock(pos);
			return;
		}

		world.breakBlock(pos, true, miner);
	}
}
