package com.zovo.tooltipwrap.test;

import com.zovo.tooltipwrap.TooltipWrapClient;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Real game run: shows a screen with a long item-style tooltip at three mouse positions, with the mod off (vanilla)
 * and on. For each case it records where the game placed the tooltip box and takes a screenshot.
 * Results go to run/tooltip-wrap-result.csv.
 */
public final class TooltipWrapClientGameTest implements FabricClientGameTest {
	public static volatile int[] LAST;

	static final List<Component> LINES = List.of(
		Component.literal("Staff of the Quiet Northern Lighthouse Keeper").withStyle(ChatFormatting.AQUA),
		Component.literal("Right click a block to light a beacon of soft blue fire that guides lost players home through fog and rain.").withStyle(ChatFormatting.GRAY),
		Component.literal("When in Main Hand: +4 Attack Damage, +10% Movement Speed while it is night").withStyle(ChatFormatting.DARK_GREEN),
		Component.literal("examplemod:lighthouse_keeper_staff").withStyle(ChatFormatting.DARK_GRAY));

	static final class TipScreen extends Screen {
		int tipX;

		TipScreen() {
			super(Component.literal("Tooltip Wrap test"));
		}

		@Override
		public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
			super.extractRenderState(graphics, mouseX, mouseY, a);
			graphics.setTooltipForNextFrame(this.font, LINES, Optional.empty(), tipX, 40);
		}
	}

	@Override
	public void runTest(ClientGameTestContext context) {
		TipScreen[] holder = new TipScreen[1];
		context.runOnClient(mc -> holder[0] = new TipScreen());
		TipScreen screen = holder[0];
		context.setScreen(() -> screen);
		context.waitTicks(5);
		List<String> rows = new ArrayList<>();
		rows.add("mode,position,screen_width,box_left,box_right,text_width,text_height,on_screen");
		List<String> failures = new ArrayList<>();
		Path cfg = FabricLoader.getInstance().getConfigDir().resolve("tooltip-wrap.json");
		for (boolean on : new boolean[] {false, true}) {
			write(cfg, on);
			TooltipWrapClient.reload();
			for (String pos : List.of("left", "middle", "right")) {
				int[] w = new int[1];
				context.runOnClient(mc -> w[0] = mc.getWindow().getGuiScaledWidth());
				screen.tipX = switch (pos) {
					case "left" -> 30;
					case "middle" -> w[0] / 2;
					default -> w[0] - 30;
				};
				LAST = null;
				context.waitTicks(3);
				int[] r = LAST;
				if (r == null) {
					failures.add(pos + ": no tooltip was placed");
					continue;
				}
				int left = r[1] - 4, right = r[1] + r[2] + 4;
				boolean fits = left >= 0 && right <= r[0];
				String mode = on ? "with-mod" : "vanilla";
				rows.add(String.format(Locale.ROOT, "%s,%s,%d,%d,%d,%d,%d,%s", mode, pos, r[0], left, right, r[2], r[3], fits));
				context.takeScreenshot("tooltip-" + mode + "-" + pos);
				if (on && !fits) failures.add("with mod, " + pos + ": box " + left + ".." + right + " on a " + r[0] + " wide screen");
			}
		}
		write(cfg, true);
		long vanillaOff = rows.stream().filter(s -> s.startsWith("vanilla") && s.endsWith("false")).count();
		if (vanillaOff == 0) failures.add("vanilla never ran off screen, so the test text is too short to prove anything");
		try {
			Files.write(Path.of("tooltip-wrap-result.csv"), rows, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		rows.forEach(s -> TooltipWrapClient.LOG.info("RESULT {}", s));
		context.setScreen(() -> null);
		if (!failures.isEmpty()) throw new AssertionError("Tooltip Wrap game test failed: " + failures);
		TooltipWrapClient.LOG.info("RESULT all checks passed");
	}

	static void write(Path cfg, boolean on) {
		try {
			Files.createDirectories(cfg.getParent());
			Files.writeString(cfg, "{\n  \"enabled\": " + on + ",\n  \"minimumWidth\": 120\n}\n", StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
