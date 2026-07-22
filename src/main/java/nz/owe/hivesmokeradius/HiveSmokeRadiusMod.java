package nz.owe.hivesmokeradius;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HiveSmokeRadiusMod implements ModInitializer {
	public static final String MOD_ID = "hivesmokeradius";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hive Smoke Radius loaded: campfire smoke now calms hives from adjacent and diagonal columns.");
	}
}
