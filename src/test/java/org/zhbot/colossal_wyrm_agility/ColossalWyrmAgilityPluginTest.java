package org.zhbot.colossal_wyrm_agility;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ColossalWyrmAgilityPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ColossalWyrmAgilityPlugin.class);
		RuneLite.main(args);
	}
}