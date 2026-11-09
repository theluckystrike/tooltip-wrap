#!/usr/bin/env python3
"""Builds the gallery image from two real game screenshots (vanilla and with the mod).
Usage: render-gallery.py <vanilla.png> <with-mod.png> <out.png>"""
import sys
from PIL import Image, ImageDraw, ImageFont

van, mod, out = sys.argv[1:4]
FONT = '/System/Library/Fonts/Menlo.ttc'
SURF, INK, INK2 = (26, 26, 25), (240, 239, 236), (195, 194, 183)
f = lambda n: ImageFont.truetype(FONT, n)
a, b = Image.open(van).convert('RGB'), Image.open(mod).convert('RGB')
w = 740
a = a.resize((w, int(a.height * w / a.width)), Image.LANCZOS)
b = b.resize((w, int(b.height * w / b.width)), Image.LANCZOS)
W, H = 1600, 900
im = Image.new('RGB', (W, H), SURF)
d = ImageDraw.Draw(im)
d.text((40, 36), 'The same tooltip, mouse in the middle of the screen', font=f(34), fill=INK)
d.text((40, 86), 'Real Minecraft 26.3 screenshots, 854 x 480 window, GUI scale 2', font=f(22), fill=INK2)
y = 190
d.text((40, y - 46), 'Vanilla: the text runs off the screen', font=f(26), fill=INK)
d.text((W - 40 - w, y - 46), 'With Tooltip Wrap: wrapped to fit', font=f(26), fill=INK)
im.paste(a, (40, y)); im.paste(b, (W - 40 - w, y))
d.text((40, y + a.height + 30), 'Colors and styles of each line are kept. Tooltips that already fit are not touched.', font=f(22), fill=INK2)
im.save(out)
