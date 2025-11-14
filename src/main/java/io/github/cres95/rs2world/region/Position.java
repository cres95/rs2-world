package io.github.cres95.rs2world.region;

public class Position {

    private int x;
    private int y;
    private int z;

    public Position(int x, int y) {
        this(x, y, 0);
    }

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void move(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    public int getRegionX() {
        return (x >> 3) - 6;
    }

    public int getRegionY() {
        return (y >> 3) - 6;
    }

    public int getLocalX() {
        return x - 8 * getRegionX();
    }

    public int getLocalY() {
        return y - 8 * getRegionY();
    }

    public boolean isVisibleFrom(Position other) {
        int deltaX = this.x - other.x;
        int deltaY = this.y - other.y;
        return this.z == other.z && deltaX >= -15 && deltaX <= 14 && deltaY >= -15 && deltaY <= 14;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
