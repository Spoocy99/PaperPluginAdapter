package dev.spoocy.adapter.gui.layout;

import java.util.Objects;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class Coordinate {

    public static Coordinate of(int x, int y) {
        return new Coordinate(x, y);
    }

    private final int x;
    private final int y;

    private Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Coordinate)) return false;
        Coordinate that = (Coordinate) obj;
        return y == that.y && x != that.x;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

}
