package org.zhbot.colossal_wyrm_agility;

import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Obstacle {
    START_LADDER("Start Ladder", new WorldPoint(1652, 2931, 0), new WorldPoint(1652, 2931, 0), 1),
    TIGHTROPE("Tightrope", new WorldPoint(1655, 2926, 1), new WorldPoint(1649, 2910, 1), 39),

    BASIC_TIGHTROPE("Basic Tightrope", new WorldPoint(1647, 2910, 1), new WorldPoint(1635, 2910, 1), 16),
    ROPE("Rope", new WorldPoint(1632, 2910, 1), new WorldPoint(1627, 2931, 1), 25),
    BASIC_LADDER("Basic Ladder", new WorldPoint(1626, 2932, 1), new WorldPoint(1626, 2932, 1), 1),

    ADVANCED_LADDER("Advanced Ladder", new WorldPoint(1648, 2909, 1), new WorldPoint(1648, 2909, 1), 1),
    EDGE("Edge", new WorldPoint(1647, 2907, 2), new WorldPoint(1635, 2907, 2), 10),
    ADVANCED_TIGHTROPE("Advanced Tightrope", new WorldPoint(1633, 2908, 2), new WorldPoint(1624, 2931, 2), 55),
    ADVANCED_TIGHTROPE2("Advanced Tightrope", new WorldPoint(1634, 2908, 2), new WorldPoint(1624, 2931, 2), 56),
    ZIPLINE("Zipline", new WorldPoint(1625, 2933, 2), new WorldPoint(1645, 2933, 0), 13);

    private final String name;
    private final WorldPoint startPoint;
    private final WorldPoint endPoint;
    private final int ticks;

    Obstacle(String name, WorldPoint startPoint, WorldPoint endPoint, int ticks)
    {
        this.name = name;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.ticks = ticks;
    }

    private static final Map<WorldPoint, Obstacle> POINT_MAP = new HashMap<>();
    private static final Map<WorldPoint, Obstacle> START_POINT_MAP = new HashMap<>();
    private static final Map<WorldPoint, Obstacle> END_POINT_MAP = new HashMap<>();

    static
    {
        for (var obstacle : values())
        {
            POINT_MAP.put(obstacle.getStartPoint(), obstacle);
            POINT_MAP.put(obstacle.getEndPoint(), obstacle);

            START_POINT_MAP.put(obstacle.getStartPoint(), obstacle);

            END_POINT_MAP.put(obstacle.getEndPoint(), obstacle);
        }
    }

    public static Obstacle getByPoint(WorldPoint point)
    {
        return POINT_MAP.get(point);
    }

    public static Obstacle getByStartPoint(WorldPoint point)
    {
        return START_POINT_MAP.get(point);
    }

    public static Obstacle getByEndPoint(WorldPoint point)
    {
        return END_POINT_MAP.get(point);
    }
}
