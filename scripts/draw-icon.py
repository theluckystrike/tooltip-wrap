#!/usr/bin/env python3
"""Draws the 512 px icon: a tooltip box whose long line wraps inside the screen edge.
Usage: draw-icon.py <out.png>   (also writes <out>-128.png)"""
import sys
from PIL import Image, ImageDraw

out = sys.argv[1]
S = 512
im = Image.new('RGBA', (S, S), (0, 0, 0, 0))
d = ImageDraw.Draw(im)
d.rounded_rectangle([0, 0, S - 1, S - 1], 96, fill=(30, 34, 52))
# screen edge
d.line([(440, 60), (440, 452)], fill=(120, 130, 170), width=8)
# tooltip box in vanilla style: dark fill, purple frame
d.rectangle([70, 120, 420, 392], fill=(16, 0, 16))
d.rectangle([76, 126, 414, 386], outline=(80, 0, 255), width=6)
# title line and wrapped text lines
d.rounded_rectangle([104, 156, 330, 180], 6, fill=(85, 255, 255))
for i, w in enumerate((290, 270, 180)):
    y = 212 + i * 48
    d.rounded_rectangle([104, y, 104 + w, y + 20], 6, fill=(170, 170, 170))
# wrap arrow from the end of line 1 back to the start of line 2
d.line([(398, 222), (398, 244), (130, 244)], fill=(255, 210, 80), width=8)
d.polygon([(118, 244), (136, 232), (136, 256)], fill=(255, 210, 80))
im.save(out)
im.resize((128, 128), Image.LANCZOS).save(out.replace('.png', '-128.png'))
