package de.mchammer;

import de.mchammer.item.HammerItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class McHammerMod implements ModInitializer {
	public static final String MOD_ID = "mc_hammer";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		HammerItems.registerModItems();
		LOGGER.info("MC Hammer initialized.");
	}
}
