package com.zovo.tooltipwrap;

import com.zovo.tooltipwrap.core.WrapWidth;
import com.zovo.tooltipwrap.mixin.ClientTextTooltipAccessor;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

/** Splits the text lines of a tooltip that would run off screen. Pictures in a tooltip (maps, bundles) are kept. */
public final class Wrapper {
	private Wrapper() {}

	/** The wrapped lines, or the same list if nothing needs to change. */
	public static List<ClientTooltipComponent> wrap(Font font, List<ClientTooltipComponent> lines, int screenWidth, int mouseX, boolean besideMouse) {
		int widest = 0;
		for (ClientTooltipComponent c : lines) widest = Math.max(widest, c.getWidth(font));
		int target = WrapWidth.target(screenWidth, mouseX, widest, besideMouse, TooltipWrapClient.CONFIG.minimumWidth);
		if (target <= 0) return lines;
		List<ClientTooltipComponent> out = new ArrayList<>(lines.size() + 4);
		boolean changed = false;
		for (ClientTooltipComponent c : lines) {
			if (c instanceof ClientTextTooltip t && c.getWidth(font) > target) {
				for (FormattedCharSequence part : font.split(toText(((ClientTextTooltipAccessor) t).tooltipwrap$text()), target)) {
					out.add(ClientTooltipComponent.create(part));
				}
				changed = true;
			} else {
				out.add(c);
			}
		}
		return changed ? out : lines;
	}

	/** Turns a styled character sequence back into text, one piece per run of the same style, so it can be split. */
	static Component toText(FormattedCharSequence seq) {
		MutableComponent root = Component.empty();
		StringBuilder run = new StringBuilder();
		Style[] runStyle = {null};
		seq.accept((index, style, codepoint) -> {
			if (runStyle[0] != null && !style.equals(runStyle[0])) {
				root.append(Component.literal(run.toString()).setStyle(runStyle[0]));
				run.setLength(0);
			}
			runStyle[0] = style;
			run.appendCodePoint(codepoint);
			return true;
		});
		if (runStyle[0] != null && !run.isEmpty()) root.append(Component.literal(run.toString()).setStyle(runStyle[0]));
		return root;
	}
}
