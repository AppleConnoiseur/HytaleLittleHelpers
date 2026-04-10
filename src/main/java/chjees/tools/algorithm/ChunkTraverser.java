package chjees.tools.algorithm;

import com.hypixel.hytale.math.util.ChunkUtil;
import org.joml.Vector2i;
import org.joml.Vector3i;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.function.Predicate;

/**
 * <p>Traverses chunks from an origin position.</p>
 * <p>A very simple algorithm that provides chunks to traverse from the origin position. It uses Hytale's chunk size to calculate how many chunks to traverse.</p>
 * <p>Each chunk have a size of 32 by 32 blocks in the X and Z axes. Using simple maths with division and modulus and this algorithm can easily provide all relevant chunks.</p>
 * <img src="Algorithm-ChunkTraversal.png"/>
 */
public class ChunkTraverser {
    /**
     * <p>Traverses chunks from an origin position.</p>
     * <p>Uses a predicate to exit out early if a match was found.</p>
     * @param searchRange Search range from origin position. Will be halved to get the radius.
     * @param originPosition The position to start the search from.
     * @param chunk Predicate that checks if a match was found, and exits out early if it returns true.
     */
    public static void generate(@Nonnegative int searchRange, @Nonnull Vector3i originPosition, @Nonnull Predicate<Vector2i> chunk)
    {
        //Half the search range to get the radius.
        int searchRadius = searchRange / 2;

        //Get the top-left and bottom-right positions.
        Vector2i topLeftPosition = new Vector2i(
                originPosition.x - searchRadius,
                originPosition.z - searchRadius);
        Vector2i bottomRightPosition = new Vector2i(
                originPosition.x + searchRadius,
                originPosition.z + searchRadius);

        //Get the top-left and bottom-right chunks.
        Vector2i topLeftChunk = toChunkVector(topLeftPosition);
        Vector2i bottomRightChunk = toChunkVector(bottomRightPosition);

        //Optimization: If top-left and bottom-right are the same, only do one check and exit.
        if(topLeftChunk.equals(bottomRightChunk))
        {
            chunk.test(topLeftChunk);
            return;
        }

        //Check all chunks and exit out early if a match was found.
        for (int chunkX = topLeftChunk.x; chunkX <= bottomRightChunk.x; chunkX++) {
            for (int chunkZ = topLeftChunk.y; chunkZ <= bottomRightChunk.y; chunkZ++) {
                if(chunk.test(new Vector2i(chunkX, chunkZ)))
                    return;
            }
        }
    }

    /**
     * Helper function to make chunk vectors with co-ordinates.
     * @param source Source vector to use.
     * @return New vector with chunk co-ordinates.
     */
    public static Vector2i toChunkVector(Vector2i source)
    {
        return new Vector2i(
                (source.x - (source.x % ChunkUtil.SIZE)) / ChunkUtil.SIZE,
                (source.y - (source.y % ChunkUtil.SIZE)) / ChunkUtil.SIZE);
    }
}
