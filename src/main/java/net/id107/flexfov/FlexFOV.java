package net.id107.flexfov;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlexFOV implements ClientModInitializer {
	public static final String MOD_ID = "flex-fov";
	public static final Logger LOGGER = LoggerFactory.getLogger("flex-fov");

	public void onInitializeClient() {
		ConfigManager.loadConfig();
		LOGGER.info("Flex FOV loaded");
	}
}
