package com.music.rs3music;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class RS3MusicPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(RS3MusicPlugin.class);
		RuneLite.main(args);
	}
}