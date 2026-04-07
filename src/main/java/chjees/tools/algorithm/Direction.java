package chjees.tools.algorithm;

/**
 * Represents a cardinal direction.
 */
public enum Direction {
    /// Cardinal direction Up or North.
    Up,
    /// Cardinal direction Right or East.
    Right,
    /// Cardinal direction Down or South.
    Down,
    /// Cardinal direction Left or West.
    Left;

    /**
     * Returns the next direction. Loops back to Up after Left.
     * @return Next direction.
     */
    public Direction next()
    {
        if(this == Left)
            return Up;

        return  Direction.values()[this.ordinal() + 1];
    }
}
