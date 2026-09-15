package org.zhbot.colossal_wyrm_agility;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ProgressBarComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

public class ColossalWyrmAgilityPanel extends OverlayPanel {
    private final Client client;
    private final ColossalWyrmAgilityConfig config;

    private Obstacle obstacle = null;
    private int startTick = -1;

    @Inject
    private ColossalWyrmAgilityPanel(Client client, ColossalWyrmAgilityConfig config)
    {
        this.client = client;
        this.config = config;

        setPosition(OverlayPosition.TOP_LEFT);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (obstacle == null)
            return null;

        panelComponent.getChildren().clear();

        if (config.overlayPanelTitle())
            panelComponent.getChildren().add(TitleComponent.builder()
                .text(obstacle.getName())
                .color(Color.GREEN)
                .build());

        var ticks = client.getTickCount() - startTick - 1;
        var ticksLeft = obstacle.getTicks() - ticks;

        var progressBar = new ProgressBarComponent();
        progressBar.setMinimum(0);
        progressBar.setMaximum(obstacle.getTicks());
        progressBar.setValue(Math.min(ticks, obstacle.getTicks()));
        progressBar.setLabelDisplayMode(ProgressBarComponent.LabelDisplayMode.TEXT_ONLY);
        progressBar.setCenterLabel(ticksLeft + " tick" + (ticksLeft == 1 ? "" : "s"));
        panelComponent.getChildren().add(progressBar);

        return super.render(graphics);
    }

    public void setObstacle(Obstacle obstacle)
    {
        setObstacle(obstacle, -1);
    }

    public void setObstacle(Obstacle obstacle, int startTick)
    {
        this.obstacle = obstacle;
        this.startTick = startTick;
    }
}
