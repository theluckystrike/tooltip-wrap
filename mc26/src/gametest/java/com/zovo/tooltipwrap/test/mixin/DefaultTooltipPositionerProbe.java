package com.zovo.tooltipwrap.test.mixin;

import com.zovo.tooltipwrap.test.TooltipWrapClientGameTest;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Test only: records where vanilla finally places each tooltip and how wide it is. */
@Mixin(DefaultTooltipPositioner.class)
public abstract class DefaultTooltipPositionerProbe {
	@Inject(method = "positionTooltip(IIIIII)Lorg/joml/Vector2ic;", at = @At("RETURN"))
	private void probe(int screenWidth, int screenHeight, int x, int y, int tooltipWidth, int tooltipHeight, CallbackInfoReturnable<Vector2ic> cir) {
		TooltipWrapClientGameTest.LAST = new int[] {screenWidth, cir.getReturnValue().x(), tooltipWidth, tooltipHeight};
	}
}
