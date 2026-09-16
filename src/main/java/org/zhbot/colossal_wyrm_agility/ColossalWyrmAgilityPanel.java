package org.zhbot.colossal_wyrm_agility;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ProgressBarComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

public class ColossalWyrmAgilityPanel extends OverlayPanel {
    private static final long MILLIS_PER_TICK = 600;

    private final Client client;
    private final ColossalWyrmAgilityConfig config;
    private final ProgressBarComponent progressBar = new ProgressBarComponent();

    private Obstacle obstacle = null;
    private int startTick = -1;
    private long startMS = -1;

    @Inject
    private ColossalWyrmAgilityPanel(Client client, ColossalWyrmAgilityConfig config)
    {
        this.client = client;
        this.config = config;

        setPosition(OverlayPosition.TOP_LEFT);
        progressBar.setMinimum(0);
        progressBar.setLabelDisplayMode(ProgressBarComponent.LabelDisplayMode.TEXT_ONLY);
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

        switch (config.overlayPanelMode())
        {
            case TICKS:
                var ticks = client.getTickCount() - startTick - 1;
                var ticksLeft = obstacle.getTicks() - ticks;

                progressBar.setMaximum(obstacle.getTicks());
                progressBar.setValue(Math.min(ticks, obstacle.getTicks()));
                progressBar.setCenterLabel(ticksLeft + (ticksLeft == 1 ? " tick" : " ticks"));

                break;
            case SECONDS:
                var totalMillis = obstacle.getTicks() * MILLIS_PER_TICK;
                var millis = System.currentTimeMillis() - startMS;
                var millisLeft = totalMillis - millis;

                progressBar.setMaximum(totalMillis);
                progressBar.setValue(Math.min(millis, totalMillis));
                progressBar.setCenterLabel(millisLeft > 0 ? String.format("%.1f seconds", millisLeft / 1000.0) : "Finishing...");
                break;
        }

        panelComponent.getChildren().add(progressBar);

        return super.render(graphics);
    }

    public void setObstacle(Obstacle obstacle)
    {
        this.obstacle = obstacle;
        if (obstacle != null)
        {
            this.startTick = client.getTickCount();
            this.startMS = System.currentTimeMillis();
        }
        else
        {
            this.startTick = -1;
            this.startMS = -1;
        }
    }
}
