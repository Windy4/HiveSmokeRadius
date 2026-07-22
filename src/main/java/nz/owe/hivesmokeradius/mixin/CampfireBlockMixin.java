package nz.owe.hivesmokeradius.mixin;

import net.minecraft.block.CampfireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Widens {@link CampfireBlock#isLitCampfireInRange(World, BlockPos)}, the
 * single check vanilla uses everywhere it decides whether a beehive/bee nest
 * is smoked out (both the shears/bottle harvest path and anger-on-break).
 *
 * <p>When the vanilla scan of the column below {@code pos} finds nothing, the
 * same vanilla scan is re-run on the 8 horizontally adjacent/diagonal columns,
 * so vanilla's scan depth and smoke-obstruction rules apply unchanged per
 * column. The neighbor scans call back into this method; a reentrancy guard
 * keeps them single-column instead of fanning out recursively.
 */
@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin {
	@Unique
	private static final ThreadLocal<Boolean> hivesmokeradius$scanningNeighbors = ThreadLocal.withInitial(() -> false);

	@Inject(method = "isLitCampfireInRange", at = @At("RETURN"), cancellable = true)
	private static void hivesmokeradius$widenToNeighborColumns(World world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValueZ() || hivesmokeradius$scanningNeighbors.get()) {
			return;
		}
		hivesmokeradius$scanningNeighbors.set(true);
		try {
			for (int dx = -1; dx <= 1; dx++) {
				for (int dz = -1; dz <= 1; dz++) {
					if ((dx != 0 || dz != 0) && CampfireBlock.isLitCampfireInRange(world, pos.add(dx, 0, dz))) {
						cir.setReturnValue(true);
						return;
					}
				}
			}
		} finally {
			hivesmokeradius$scanningNeighbors.set(false);
		}
	}
}
