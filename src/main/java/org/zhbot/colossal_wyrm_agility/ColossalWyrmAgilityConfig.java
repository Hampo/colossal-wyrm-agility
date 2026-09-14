package org.zhbot.colossal_wyrm_agility;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Notification;

@ConfigGroup(ColossalWyrmAgilityConfig.group)
public interface ColossalWyrmAgilityConfig extends Config
{
	String group = "colossal-wyrm-agility";

	@ConfigItem(
			keyName = "obstacleCompleteNotifications",
			name = "Obstacle Notifications",
			description = "Configuration for obstacle complete notifications",
			position = 0
	)
	default Notification obstacleCompleteNotifications()
	{
		return Notification.OFF;
	}

	@ConfigItem(
			keyName = "hideChatMessages",
			name = "Hide Chat Messages",
			description = "Hide the forced chat messages",
			position = 1
	)
	default boolean hideChatMessages()
	{
		return true;
	}

	@ConfigItem(
			keyName = "hideLapCount",
			name = "Hide Lap Count",
			description = "Hide the completed lap count message",
			position = 2
	)
	default boolean hideLapCount()
	{
		return true;
	}

	@ConfigItem(
			keyName = "hideLapDuration",
			name = "Hide Lap Duration",
			description = "Hide the lap duration message",
			position = 3
	)
	default boolean hideLapDuration()
	{
		return true;
	}

	@ConfigItem(
			keyName = "hideTermites",
			name = "Hide Termites",
			description = "Hide the termite message",
			position = 4
	)
	default boolean hideTermites()
	{
		return true;
	}

	@ConfigItem(
			keyName = "hideBoneShards",
			name = "Hide Bone Shards",
			description = "Hide the bone shards message",
			position = 5
	)
	default boolean hideBoneShards()
	{
		return true;
	}
}
