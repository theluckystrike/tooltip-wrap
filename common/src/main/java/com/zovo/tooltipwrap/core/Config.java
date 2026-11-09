package com.zovo.tooltipwrap.core;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** config/tooltip-wrap.json. */
public final class Config {
	public boolean enabled = true;
	public int minimumWidth = 120;
	public final List<String> warnings = new ArrayList<>();

	public static Config defaults() {
		return new Config();
	}

	public static Config load(Path file) {
		Config c = new Config();
		if (!Files.exists(file)) {
			c.save(file);
			return c;
		}
		JsonObject o;
		try {
			JsonElement e = JsonParser.parseString(Files.readString(file, StandardCharsets.UTF_8));
			if (!e.isJsonObject()) throw new IllegalStateException("not a JSON object");
			o = e.getAsJsonObject();
		} catch (Exception ex) {
			c.warnings.add("could not read the file (" + ex.getMessage() + "), using defaults");
			return c;
		}
		if (o.has("enabled")) {
			JsonElement e = o.get("enabled");
			if (e.isJsonPrimitive() && e.getAsJsonPrimitive().isBoolean()) c.enabled = e.getAsBoolean();
			else c.warnings.add("enabled must be true or false, using true");
		}
		if (o.has("minimumWidth")) {
			try {
				int v = o.get("minimumWidth").getAsInt();
				if (v < 40 || v > 1000) c.warnings.add("minimumWidth must be from 40 to 1000, using 120");
				else c.minimumWidth = v;
			} catch (Exception ex) {
				c.warnings.add("minimumWidth must be a whole number, using 120");
			}
		}
		return c;
	}

	public void save(Path file) {
		try {
			Files.createDirectories(file.getParent());
			Files.writeString(file, "{\n  \"enabled\": " + enabled + ",\n  \"minimumWidth\": " + minimumWidth + "\n}\n", StandardCharsets.UTF_8);
		} catch (IOException ex) {
			warnings.add("could not write " + file + ": " + ex.getMessage());
		}
	}
}
