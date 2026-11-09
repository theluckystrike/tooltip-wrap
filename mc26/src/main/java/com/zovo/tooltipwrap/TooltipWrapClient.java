package com.zovo.tooltipwrap;

import com.zovo.tooltipwrap.core.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TooltipWrapClient implements ClientModInitializer {
	public static final Logger LOG = LoggerFactory.getLogger("tooltip-wrap");
	public static volatile Config CONFIG = Config.defaults();

	@Override
	public void onInitializeClient() {
		reload();
	}

	/** Reads config/tooltip-wrap.json again. Also used by the game test. */
	public static void reload() {
		Config c = Config.load(FabricLoader.getInstance().getConfigDir().resolve("tooltip-wrap.json"));
		for (String w : c.warnings) LOG.warn("tooltip-wrap.json: {}", w);
		CONFIG = c;
		LOG.info("Tooltip Wrap ready: enabled={}, minimumWidth={}", c.enabled, c.minimumWidth);
	}
}
