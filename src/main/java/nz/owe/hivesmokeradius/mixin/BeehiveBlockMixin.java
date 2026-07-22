package nz.owe.hivesmokeradius.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nz.owe.hivesmokeradius.HiveSmokeRadiusMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Widens the smoked-out check in the honey-harvest path
 * ({@link BeehiveBlock}'s use-with-shears/bottle handler), which calls
 * {@code CampfireBlock.isLitCampfireInRange} directly rather than going
 * through the block entity.
 */
@Mixin(BeehiveBlock.class)
public abstract class BeehiveBlockMixin {
	@WrapOperation(
			method = "onUseWithItem",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/CampfireBlock;isLitCampfireInRange(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z"
			)
	)
	private boolean hivesmokeradius$widenHarvestSmokedCheck(World world, BlockPos pos, Operation<Boolean> original) {
		return original.call(world, pos) || HiveSmokeRadiusMod.isLitCampfireInRadius(world, pos);
	}
}
