package com.zovo.tooltipwrap.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class WrapWidthTest {
	@Test void fitsRightOfMouseNoWrap() {
		assertEquals(0, WrapWidth.target(427, 50, 200, true, 120));
	}

	@Test void fitsLeftOfMouseNoWrap() {
		// vanilla flips it to the left side, which has room
		assertEquals(0, WrapWidth.target(427, 380, 300, true, 120));
	}

	@Test void tooWideWrapsToTheBiggerSide() {
		// mouse in the middle: right side 427-212-12-4 = 199, left side 212-16 = 196
		assertEquals(199, WrapWidth.target(427, 212, 380, true, 120));
	}

	@Test void wrappedWidthReallyFitsOnThatSide() {
		for (int mouse = 0; mouse <= 427; mouse += 7) {
			int t = WrapWidth.target(427, mouse, 600, true, 120);
			assertTrue(t > 0);
			boolean right = mouse + 12 + t + WrapWidth.FRAME <= 427;
			boolean left = mouse - 12 - t - WrapWidth.FRAME >= 0;
			boolean edge = t <= 427 - 2 * WrapWidth.FRAME;
			assertTrue(right || left || edge, "mouse " + mouse + " width " + t);
		}
	}

	@Test void littleRoomUsesWholeScreen() {
		// mouse in the middle: each side is 144, under the 150 minimum
		assertEquals(312, WrapWidth.target(320, 160, 500, true, 150));
		assertEquals(0, WrapWidth.target(320, 160, 300, true, 150));
	}

	@Test void otherPlacementOnlyChecksScreenWidth() {
		assertEquals(0, WrapWidth.target(427, 400, 400, false, 120));
		assertEquals(419, WrapWidth.target(427, 400, 500, false, 120));
	}

	@Test void tinyScreenDoesNothing() {
		assertEquals(0, WrapWidth.target(6, 3, 100, true, 120));
	}
}
