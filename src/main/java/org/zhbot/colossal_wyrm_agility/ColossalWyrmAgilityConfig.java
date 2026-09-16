package org.zhbot.colossal_wyrm_agility;

import net.runelite.client.config.*;

@ConfigGroup(ColossalWyrmAgilityConfig.group)
public interface ColossalWyrmAgilityConfig extends Config
{
	String group = "colossal-wyrm-agility";

	@ConfigSection(
			name = "Notifications",
			description = "Configure notifications",
			position = 0
	)
	String notificationsSection = "notificationsSection";

	@ConfigItem(
			keyName = "obstacleCompleteNotifications",
			name = "Obstacle Complete",
			description = "Configuration for obstacle complete notifications",
			section = notificationsSection,
			position = 0
	)
	default Notification obstacleCompleteNotifications()
	{
		return Notification.ON;
	}

	@ConfigItem(
			keyName = "obstacleMinimumTicks",
			name = "Minimum Ticks",
			description = "Minimum ticks an obstacle must take to trigger a notification. 0 for all obstacles.",
			section = notificationsSection,
			position = 1
	)
	default int obstacleMinimumTicks()
	{
		return 1;
	}

	@ConfigSection(
			name = "Overlay panel",
			description = "Configure the overlay panel",
			position = 1
	)
	String overlayPanelSection = "overlayPanelSection";

	@ConfigItem(
			keyName = "overlayPanelEnabled",
			name = "Enabled",
			description = "If the overlay panel should be shown",
			section = overlayPanelSection,
			position = 0
	)
	default boolean overlayPanelEnabled()
	{
		return true;
	}

	@ConfigItem(
			keyName = "overlayPanelTitle",
			name = "Show Title",
			description = "If the overlay panel should have a title showing the current obstacle name",
			section = overlayPanelSection,
			position = 1
	)
	default boolean overlayPanelTitle()
	{
		return true;
	}

	@ConfigItem(
			keyName = "overlayPanelMode",
			name = "Duration Format",
			description = "What format to show an obstacle's duration",
			section = overlayPanelSection,
			position = 1
	)
	default DurationMode overlayPanelMode()
	{
		return DurationMode.TICKS;
	}

	@ConfigSection(
			name = "Chat messages",
			description = "Configure chat messages",
			position = 2
	)
	String chatMessagesSection = "chatMessagesSection";

	@ConfigItem(
			keyName = "hideChatMessages",
			name = "Hide Chat Messages",
			description = "Hide the forced chat messages",
			section = chatMessagesSection,
			position = 0
	)
	default boolean hideChatMessages()
	{
		return true;
	}

	@ConfigItem(
			keyName = "hideChatMessagesOverhead",
			name = "Hide Overhead",
			description = "Hide the forced chat messages overhead",
			section = chatMessagesSection,
			position = 0
	)
	default boolean hideChatMessagesOverhead()
	{
		return true;
	}

	@ConfigItem(
			keyName = "hideLapCount",
			name = "Hide Lap Count",
			description = "Hide the completed lap count message",
			section = chatMessagesSection,
			position = 1
	)
	default boolean hideLapCount()
	{
		return true;
	}

	@ConfigItem(
			keyName = "hideLapDuration",
			name = "Hide Lap Duration",
			description = "Hide the lap duration message",
			section = chatMessagesSection,
			position = 2
	)
	default boolean hideLapDuration()
	{
		return true;
	}

	@ConfigItem(
			keyName = "hideTermites",
			name = "Hide Termites",
			description = "Hide the termite message",
			section = chatMessagesSection,
			position = 3
	)
	default boolean hideTermites()
	{
		return true;
	}

	@ConfigItem(
			keyName = "hideBoneShards",
			name = "Hide Bone Shards",
			description = "Hide the bone shards message",
			section = chatMessagesSection,
			position = 4
	)
	default boolean hideBoneShards()
	{
		return true;
	}
}
