package chjees.tools.algorithm;

import org.joml.Vector3d;
import org.joml.Vector3i;

import javax.annotation.Nonnull;

/**
 * Represents the result of an algorithm.
 */
public class AlgorithmResult {
    /// No match was found in the algorithm.
    public final static AlgorithmResult NO_MATCH = new AlgorithmResult();

    /// Matching cell found in the algorithm.
    private final Vector3i cell;
    /// Is true if there is a match.
    private final boolean foundMatch;

    /**
     * Result with a matching cell. Will return true on {@code matching()}.
     * @param cell Matching cell.
     */
    public AlgorithmResult(@Nonnull Vector3i cell)
    {
        this.cell = cell;
        this.foundMatch = true;
    }

    /**
     * Result with no matching cell. Will return false on {@code matching()}
     */
    public AlgorithmResult()
    {
        this.cell = null;
        this.foundMatch = false;
    }

    /**
     * If this result matches it will return true.
     * @return Result of a match.
     */
    public boolean matching()
    {
        return this.foundMatch;
    }

    /**
     * If the result matches this will return a non-null cell.
     * @return Cell if a match was found, {@code null} otherwise.
     */
    public Vector3i getCell()
    {
        return this.cell;
    }

    /**
     * If the result matches this will return a non-null cell of a {@link Vector3d} type.
     * @return Cell if a match was found, {@code null} otherwise.
     */
    public Vector3d getCellDouble()
    {
        if(this.cell != null)
            return new Vector3d(this.cell.x, this.cell.y, this.cell.z);
        return null;
    }
}
