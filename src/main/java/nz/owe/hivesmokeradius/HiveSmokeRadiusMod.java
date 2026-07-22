package nz.owe.hivesmokeradius;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.CampfireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HiveSmokeRadiusMod implements ModInitializer {
	public static final String MOD_ID = "hivesmokeradius";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hive Smoke Radius loaded: campfire smoke now calms hives from adjacent and diagonal columns.");
	}

	/**
	 * Widened replacement for vanilla's smoked-out check. Vanilla only scans the
	 * single column straight below {@code hivePos}; this also scans the 8
	 * horizontally adjacent/diagonal columns, reusing vanilla's own per-column
	 * scan (including its obstruction rules) for each candidate.
	 */
	public static boolean isLitCampfireInRadius(World world, BlockPos hivePos) {
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				if (CampfireBlock.isLitCampfireInRange(world, hivePos.add(dx, 0, dz))) {
					return true;
				}
			}
		}
		return false;
	}
}
