Long tooltips no longer run off the screen. When an item name, a description or a list of stats is too wide for the space next to your mouse, this mod wraps it onto more lines so you can read all of it.

## The problem in vanilla

Minecraft never makes a tooltip narrower. If a line is wider than the space on either side of the mouse, the box is pushed to the left edge and the rest of the text is cut off at the right edge. This happens with modded items that have long descriptions, enchantment descriptions, long custom names, and with a large GUI scale or a small window. In the real game test below, a 4-line tooltip was 542 pixels wide on a screen 427 pixels wide, so a whole sentence was lost.

## What this mod changes

- A tooltip that fits stays exactly as it is.
- A tooltip that does not fit is wrapped to the width of the bigger side next to the mouse, so it still sits beside the cursor.
- If both sides are very narrow (under 120 GUI pixels by default), it is wrapped to the width of the screen instead.
- Each line keeps its colors and styles. Pictures in tooltips, such as maps and bundle contents, are left alone.

It works for item tooltips in inventories and creative tabs, and for tooltips from other mods that use the normal game code. Button tooltips, which sit above or below the button, are only wrapped when they are wider than the whole screen.

## Install

1. Install Fabric Loader 0.17 or newer.
2. Put `tooltip-wrap-fabric-1.0.0+26.x.jar` in your `mods/` folder. Fabric API is not needed.
3. Start the game. Settings are in `config/tooltip-wrap.json`.

Client side only. Servers do not need it.

## Config

```json
{
  "enabled": true,
  "minimumWidth": 120
}
```

- `enabled` turns wrapping on or off.
- `minimumWidth` is the narrowest width, in GUI pixels, that a tooltip is wrapped to beside the mouse. With less room than that on both sides, it uses the full screen width.

## Tested in the real game

A client game test opens a screen in a real Minecraft 26.3 client (854 x 480 window, GUI scale 2, screen 427 GUI pixels wide) with a 4-line item tooltip, at three mouse positions, with the mod off and on. It records where the game placed the tooltip box.

| Mouse position | Vanilla: tooltip box (screen is 0 to 427) | With the mod: tooltip box |
|---|---|---|
| Left | 0 to 542, runs off the screen | 38 to 418, fits (5 lines instead of 4) |
| Middle | 0 to 542, runs off the screen | 221 to 426, fits (8 lines instead of 4) |
| Right | 0 to 542, runs off the screen | 9 to 389, fits (5 lines instead of 4) |

Vanilla cut the end of the description off in all three positions. With the mod the whole tooltip was on screen every time.

Free and open source under the MIT license.

[zovo-support-start]: #

## Support Zovo

Zovo builds and maintains Tooltip Wrap, and every feature stays free. If it saves you time, you can support the work on [GitHub Sponsors](https://github.com/sponsors/theluckystrike). Sponsorship is optional, and the mod works the same without it.

You can also use [PayPal](https://www.paypal.com/paypalme/najzzz).

Bug reports and ideas go to the issue tracker linked on this page.

Zovo is made by [theluckystrike](https://github.com/theluckystrike) on GitHub.

## More mods by Zovo

- [Local Boss Sounds](https://github.com/theluckystrike/local-boss-sounds). Hear the wither, the dragon death and the end portal only when you are near them. Client side, Fabric 26.3.
- [Villager Replant](https://github.com/theluckystrike/villager-replant). Farmer villagers replant the crop they harvested, so rows stay one crop. Server side, Fabric 26.3.
- [Async Locator 26](https://github.com/theluckystrike/async-locator-26). Structure searches for /locate, maps, dolphins and eyes of ender run off the server thread. Fabric 26.3 and 1.21.11.
- [All Zovo tools on GitHub](https://github.com/theluckystrike?tab=repositories). Free developer tools, including VS Code extensions and Apify Actors.

[zovo-support-end]: #
