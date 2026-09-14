package org.zhbot.colossal_wyrm_agility;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.Set;

@Slf4j
@PluginDescriptor(
	name = "Colossal Wyrm Agility",
	description = "A plugin to help AFKing Colossal Wyrm Agility.",
	tags = {"colossal", "wyrm", "agility", "afk"}
)
public class ColossalWyrmAgilityPlugin extends Plugin
{
	private static final WorldArea COLOSSAL_WYRM_REMAINS_AREA = new WorldArea(1623, 2905, 35, 31, 0);
	private static final Set<String> CHAT_MESSAGES = ImmutableSet.of(
			"I hope my next adventure is fun.",
			"Nice view.",
			"*sigh*",
			"I wonder what I'll do next.",
			"Boy, my hands are tired.",
			"Why can that anteater talk?",
			"Boy I'm tired."
	);
	private static final String LAP_COUNT_BASIC_MESSAGE = "Your Colossal Wyrm Agility Course (Basic) lap count is: ";
	private static final String LAP_COUNT_ADVANCED_MESSAGE = "Your Colossal Wyrm Agility Course (Advanced) lap count is: ";
	private static final String LAP_DURATION_MESSAGE = "Lap duration: ";
	private static final String TERMITES_MESSAGE = "You managed to scoop up ";
	private static final String BONE_SHARDS_MESSAGE = "You also find";
	private static final Set<WorldPoint> OBSTACLE_COMPLETE_POINTS = ImmutableSet.of(
			// Start
			new WorldPoint(1653, 2931, 1),
			new WorldPoint(1649, 2910, 1),

			// Beginner
			new WorldPoint(1635, 2910, 1),
			new WorldPoint(1635, 2910, 1),
			new WorldPoint(1627, 2931, 1),
			new WorldPoint(1625, 2932, 2),

			// Advanced
			new WorldPoint(1648, 2908, 2),
			new WorldPoint(1635, 2907, 2),
			new WorldPoint(1624, 2931, 2),
			new WorldPoint(1645, 2933, 0)
	);
	private static final int IDLE_POSE_ANIMATION_ID = 808;
	private static final WorldPoint BASIC_TIGHTROPE_END_POINT = new WorldPoint(1635, 2910, 1);

	@Inject
	private Client client;

	@Inject
	private Notifier notifier;

	@Inject
	private ColossalWyrmAgilityConfig config;

	@Inject
	private TextUtils textUtils;

	private boolean inColossalWyrmRemainsArea = false;
	private int lastPoseAnimation = -1;

	@Override
	protected void startUp() throws Exception
	{
		log.debug("Example started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.debug("Example stopped!");
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		var player = client.getLocalPlayer();
		if (player == null)
		{
			inColossalWyrmRemainsArea = false;
			return;
		}

		inColossalWyrmRemainsArea = COLOSSAL_WYRM_REMAINS_AREA.contains2D(player.getWorldLocation());
		if (!inColossalWyrmRemainsArea)
			return;

		var poseAnimation = player.getPoseAnimation();
		if (poseAnimation != lastPoseAnimation)
		{
			lastPoseAnimation = poseAnimation;
			if (poseAnimation == IDLE_POSE_ANIMATION_ID && player.getWorldLocation().distanceTo(BASIC_TIGHTROPE_END_POINT) == 0)
				notifier.notify(config.obstacleCompleteNotifications(), "Obstacle complete");
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (!inColossalWyrmRemainsArea)
			return;

		var message = textUtils.Clean(event.getMessage());

		switch (event.getType())
		{
			case PUBLICCHAT:
				if (!config.hideChatMessages())
					return;

				if (!event.getName().equals(client.getLocalPlayer().getName()))
					return;

				if (!CHAT_MESSAGES.contains(message))
					return;

				break;
			case GAMEMESSAGE:
				if (message.startsWith(LAP_COUNT_BASIC_MESSAGE) || message.startsWith(LAP_COUNT_ADVANCED_MESSAGE))
				{
					if (!config.hideLapCount())
						return;
				}
				else if (message.startsWith(LAP_DURATION_MESSAGE))
				{
					if (!config.hideLapDuration())
						return;
				}
				else if (message.startsWith(TERMITES_MESSAGE))
				{
					if (!config.hideTermites())
						return;
				}
				else if (message.startsWith(BONE_SHARDS_MESSAGE))
				{
					if (!config.hideBoneShards())
						return;
				}
				else
				{
					return;
				}

				break;
			default:
				return;
		}

		final var lineBuffer = client.getChatLineMap().get(event.getType().getType());
		if (lineBuffer == null)
			return;

		lineBuffer.removeMessageNode(event.getMessageNode());
		client.refreshChat();
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event)
	{
		if (!inColossalWyrmRemainsArea)
			return;

		if (!config.obstacleCompleteNotifications().isEnabled())
			return;

		var player = client.getLocalPlayer();
		if (player == null)
			return;

		if (event.getActor() != player)
			return;

		if (player.getAnimation() != -1)
			return;

		var location = player.getWorldLocation();
		if (location == null)
			return;

		if (!OBSTACLE_COMPLETE_POINTS.contains(location))
			return;

		notifier.notify(config.obstacleCompleteNotifications(), "Obstacle complete");
	}

	@Provides
	ColossalWyrmAgilityConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ColossalWyrmAgilityConfig.class);
	}
}
