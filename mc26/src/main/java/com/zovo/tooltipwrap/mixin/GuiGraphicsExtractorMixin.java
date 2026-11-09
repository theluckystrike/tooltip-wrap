package com.zovo.tooltipwrap.mixin;

import com.zovo.tooltipwrap.TooltipWrapClient;
import com.zovo.tooltipwrap.Wrapper;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsExtractorMixin {
	@Unique private boolean tooltipwrap$inside;

	@Shadow public abstract int guiWidth();

	@Shadow public abstract void tooltip(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner,
		@Nullable Identifier style, boolean extraSpaceAfterFirstLine);

	// Every tooltip is drawn through this method. If its text would run off screen, draw the wrapped lines instead.
	@Inject(method = "tooltip", at = @At("HEAD"), cancellable = true)
	private void tooltipwrap$wrap(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner,
		@Nullable Identifier style, boolean extraSpaceAfterFirstLine, CallbackInfo ci) {
		if (this.tooltipwrap$inside || !TooltipWrapClient.CONFIG.enabled || lines.isEmpty()) return;
		List<ClientTooltipComponent> wrapped = Wrapper.wrap(font, lines, this.guiWidth(), xo, positioner instanceof DefaultTooltipPositioner);
		if (wrapped == lines) return;
		ci.cancel();
		this.tooltipwrap$inside = true;
		try {
			this.tooltip(font, wrapped, xo, yo, positioner, style, extraSpaceAfterFirstLine);
		} finally {
			this.tooltipwrap$inside = false;
		}
	}
}
