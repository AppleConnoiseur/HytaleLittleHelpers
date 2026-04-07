package chjees.tools.algorithm;

import org.joml.Vector3i;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.function.Predicate;

/**
 * <p>Generates a two-dimensional spiral pattern from an origin for a predicate to act on.</p>
 * <p>This algorithm is biased towards generating a pattern to the "north".</p>
 * <p>As simple as spiral algorithms can be.</p>
 * <img src="Algorithm-Spiral.png"/>
 */
public class SpiralPattern {
    /**
     * <p>Generates a two-dimensional spiral pattern from an origin for a predicate to act on.</p>
     * @param maxCells Maximum amount of cells to generate. If set to 9 it only generates cells in a 3 x 3 area.
     * @param originCell Origin cell for predicate to act on.
     * @param cell Predicate that acts on the current cell in the algorithm.
     */
    public static AlgorithmResult generate(@Nonnegative int maxCells, @Nonnull Vector3i originCell, @Nonnull Predicate<Vector3i> cell)
    {
        //Working variables.
        int currentDistance = 0; //Current traversed distance.
        int currentSquare = 1; //Start: 3 x 3
        int currentX = 0; //Start in the middle.
        int currentZ = -1; //Start at the top.

        //Indicates the "direction" we are adding towards.
        //Start by going right.
        Direction direction = Direction.Right;

        //Keep going until we traversed the maximum cell distance.
        while (currentDistance < maxCells)
        {
            //Make the predicate act on the current cell.
            //If it returns true we exit out early.
            Vector3i destinationVector = new Vector3i(originCell);
            originCell.add(currentX, originCell.y, currentZ, destinationVector);
            if(cell.test(destinationVector)) {
                //Found a match.
                return new AlgorithmResult(destinationVector);
            }


            //Act on current direction.
            switch (direction) {
                case Up:
                {
                    currentZ--;
                    if(currentZ < -currentSquare)
                    {
                        //Increases the current "square" area this algorithm can explore.
                        currentSquare++;
                        direction = Direction.Right;
                    }
                }
                break;
                case Right:
                {
                    currentX++;
                    if(currentX == currentSquare)
                        direction = Direction.Down;
                }
                break;
                case Down:
                {
                    currentZ++;
                    if(currentZ == currentSquare)
                        direction = Direction.Left;
                }
                break;
                case Left:
                {
                    currentX--;
                    if(currentX == -currentSquare)
                        direction = Direction.Up; //Loop back to Up.
                }
            }

            //Continue traversing.
            currentDistance++;
        }

        //No match found.
        return AlgorithmResult.NO_MATCH;
    }
}
