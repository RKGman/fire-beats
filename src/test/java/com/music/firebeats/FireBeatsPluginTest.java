package com.music.firebeats;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class FireBeatsPluginTest {
	public static void main(String[] args) throws Exception {
		ExternalPluginManager.loadBuiltin(FireBeatsPlugin.class);
		RuneLite.main(args);
	}
}