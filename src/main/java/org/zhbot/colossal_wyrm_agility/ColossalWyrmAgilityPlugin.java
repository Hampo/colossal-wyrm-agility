package org.zhbot.colossal_wyrm_agility;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.events.*;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

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

	@Inject
	private Client client;

	@Inject
	private Notifier notifier;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ColossalWyrmAgilityConfig config;

	@Inject
	private TextUtils textUtils;

	@Inject
	private ColossalWyrmAgilityPanel panel;

	private boolean inColossalWyrmRemainsArea = false;
	private boolean justLoggedIn = false;

	@Override
	protected void startUp() throws Exception
	{
		updateConfig();
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(panel);
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
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals(ColossalWyrmAgilityConfig.group))
			updateConfig();
	}

	private void updateConfig()
	{
		if (config.overlayPanelEnabled())
			overlayManager.add(panel);
		else
			overlayManager.remove(panel);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGED_IN)
			justLoggedIn = true;
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		if (!inColossalWyrmRemainsArea)
			return;

		if (event.getVarbitId() != VarbitID.BUSY)
			return;

		if (justLoggedIn)
		{
			justLoggedIn = false;
			return;
		}

		var player = client.getLocalPlayer();
		if (player == null)
			return;

		var location = player.getWorldLocation();
		if (location == null)
			return;

		if (event.getValue() == 0)
		{
			panel.setObstacle(null);

			var obstacle = Obstacle.getByEndPoint(location);
			if (obstacle == null)
				return;

			if (config.obstacleMinimumTicks() < obstacle.getTicks())
				notifier.notify(config.obstacleCompleteNotifications(), "Obstacle \"" + obstacle.getName() + "\" complete");
		}
		else
		{
			var obstacle = Obstacle.getByStartPoint(location);
			if (obstacle == null)
				return;

			if (obstacle.getTicks() > 1)
				panel.setObstacle(obstacle);
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

	@Provides
	ColossalWyrmAgilityConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ColossalWyrmAgilityConfig.class);
	}
}
