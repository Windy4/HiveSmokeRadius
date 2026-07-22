package nz.owe.hivesmokeradius.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nz.owe.hivesmokeradius.HiveSmokeRadiusMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Widens {@link BeehiveBlockEntity#isSmoked()}, which vanilla uses to decide
 * whether bees released from a broken hive get angry at the player.
 */
@Mixin(BeehiveBlockEntity.class)
public abstract class BeehiveBlockEntityMixin {
	@WrapOperation(
			method = "isSmoked",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/CampfireBlock;isLitCampfireInRange(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z"
			)
	)
	private boolean hivesmokeradius$widenSmokedCheck(World world, BlockPos pos, Operation<Boolean> original) {
		return original.call(world, pos) || HiveSmokeRadiusMod.isLitCampfireInRadius(world, pos);
	}
}
