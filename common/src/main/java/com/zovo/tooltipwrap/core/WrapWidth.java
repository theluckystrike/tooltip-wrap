package com.zovo.tooltipwrap.core;

/**
 * Picks the width to wrap a tooltip to, so the whole box stays on screen.
 *
 * Vanilla places a tooltip 12 px right of the mouse. If it does not fit there it moves it to the left of the mouse,
 * but never further left than 4 px from the edge. Vanilla never makes a tooltip narrower, so a wide one runs off
 * the screen. The box has 4 px of border and padding on each side of the text.
 */
public final class WrapWidth {
	/** Border plus padding on one side of the text. */
	public static final int FRAME = 4;

	private WrapWidth() {}

	/**
	 * @param screenWidth  GUI width in GUI pixels
	 * @param mouseX       x the tooltip is placed from
	 * @param textWidth    width of the widest line
	 * @param besideMouse  true for the normal placement next to the mouse; false for other placements (under a
	 *                     button, for example), where only the screen width counts
	 * @param minimumWidth narrowest width worth wrapping to beside the mouse; below it the tooltip may cover the mouse
	 * @return the width to wrap to, or 0 if the tooltip already fits
	 */
	public static int target(int screenWidth, int mouseX, int textWidth, boolean besideMouse, int minimumWidth) {
		int whole = screenWidth - 2 * FRAME;
		if (whole <= 0) return 0;
		if (!besideMouse) return textWidth > whole ? whole : 0;
		int right = screenWidth - (mouseX + 12) - FRAME;  // text width that fits right of the mouse
		int left = mouseX - 12 - FRAME;                    // text width that fits left of the mouse
		int side = Math.max(right, left);
		if (textWidth <= side) return 0;
		if (side >= minimumWidth) return side;
		// Too little room on either side: use the whole screen width. Vanilla then puts the box at the left edge.
		return textWidth > whole ? whole : 0;
	}
}
