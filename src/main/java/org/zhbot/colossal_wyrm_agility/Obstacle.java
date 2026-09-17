package org.zhbot.colossal_wyrm_agility;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public enum Obstacle {
    START_LADDER("Start Ladder", 1, new WorldPoint(1652, 2931, 0), new WorldPoint(1652, 2931, 0)),
    TIGHTROPE("Tightrope", 39, new WorldPoint(1649, 2910, 1), new WorldPoint(1655, 2926, 1)),

    BASIC_TIGHTROPE("Basic Tightrope", 16, new WorldPoint(1635, 2910, 1), new WorldPoint(1647, 2910, 1)),
    ROPE("Rope", 25, new WorldPoint(1627, 2931, 1), new WorldPoint(1632, 2910, 1)),
    BASIC_LADDER("Basic Ladder", 1, new WorldPoint(1626, 2932, 1), new WorldPoint(1626, 2932, 1)),

    ADVANCED_LADDER("Advanced Ladder", 1, new WorldPoint(1648, 2909, 1), new WorldPoint(1648, 2909, 1)),
    EDGE("Edge", 10, new WorldPoint(1635, 2907, 2), new WorldPoint(1647, 2907, 2)),
    ADVANCED_TIGHTROPE("Advanced Tightrope", 56, new WorldPoint(1624, 2931, 2), new WorldPoint(1633, 2908, 2), new WorldPoint(1634, 2908, 2), new WorldPoint(1633, 2907, 2)),
    ZIPLINE("Zipline", 13, new WorldPoint(1645, 2933, 0), new WorldPoint(1625, 2933, 2));

    private final String name;
    private final Set<WorldPoint> startPoints;
    private final WorldPoint endPoint;
    private final int ticks;

    Obstacle(String name, int ticks, WorldPoint endPoint, WorldPoint... startPoints)
    {
        this.name = name;
        this.ticks = ticks;
        this.endPoint = endPoint;
        this.startPoints = ImmutableSet.copyOf(startPoints);
    }

    private static final Map<WorldPoint, Obstacle> START_POINT_MAP = new HashMap<>();
    private static final Map<WorldPoint, Obstacle> END_POINT_MAP = new HashMap<>();

    static
    {
        for (var obstacle : values())
        {
            for (var startPoint : obstacle.startPoints)
                START_POINT_MAP.put(startPoint, obstacle);

            END_POINT_MAP.put(obstacle.endPoint, obstacle);
        }
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
