package com.zovo.tooltipwrap.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ConfigTest {
	@TempDir Path dir;

	@Test void missingFileWritesDefaults() throws Exception {
		Path f = dir.resolve("config/tooltip-wrap.json");
		Config c = Config.load(f);
		assertTrue(c.enabled);
		assertEquals(120, c.minimumWidth);
		assertTrue(Files.readString(f).contains("\"minimumWidth\": 120"));
	}

	@Test void readsValues() throws Exception {
		Path f = dir.resolve("c.json");
		Files.writeString(f, "{\"enabled\":false,\"minimumWidth\":200}");
		Config c = Config.load(f);
		assertFalse(c.enabled);
		assertEquals(200, c.minimumWidth);
	}

	@Test void badValuesWarn() throws Exception {
		Path f = dir.resolve("c.json");
		Files.writeString(f, "{\"enabled\":1,\"minimumWidth\":5}");
		Config c = Config.load(f);
		assertTrue(c.enabled);
		assertEquals(120, c.minimumWidth);
		assertEquals(2, c.warnings.size());
	}
}
