package chjees.tools.test;

import com.hypixel.hytale.math.util.ChunkUtil;
import org.joml.Vector2i;
import org.joml.Vector3i;

import java.util.ArrayList;

public class ChunkMaths {
    public static final int chunkSize = ChunkUtil.SIZE;

    static void main() {
        IO.println("Chunk math test.");
        IO.println();

        int searchRange = 30 / 2;

        //Get the origin.
        Vector3i originPosition = new Vector3i(43, 64, 87);
        Vector2i originChunk = new Vector2i(
                (originPosition.x - (originPosition.x % chunkSize)) / chunkSize,
                (originPosition.z - (originPosition.z % chunkSize)) / chunkSize);

        IO.println(String.format("originChunk: %s, %s", originChunk.x, originChunk.y));
        IO.println(String.format("indexChunk: %s", ChunkUtil.indexChunk(originChunk.x, originChunk.y)));
        IO.println(String.format("indexChunk (Origin): %s", ChunkUtil.indexChunkFromBlock(originPosition.x, originPosition.y)));
        IO.println();

        //Get the top-left and bottom-right positions.
        Vector2i topLeftPosition = new Vector2i(
                originPosition.x - searchRange,
                originPosition.z - searchRange);
        Vector2i bottomRightPosition = new Vector2i(
                originPosition.x + searchRange,
                originPosition.z + searchRange);

        IO.println(String.format("Top-Left: %s, %s", topLeftPosition.x, topLeftPosition.y));
        IO.println(String.format("Bottom-Right: %s, %s", bottomRightPosition.x, bottomRightPosition.y));
        IO.println();

        //Get the chunks for the positions.
        Vector2i topLeftChunk = toChunkVector(topLeftPosition);
        Vector2i bottomRightChunk = toChunkVector(bottomRightPosition);

        IO.println(String.format("Top-Left (Chunk): %s, %s :: %s", topLeftChunk.x, topLeftChunk.y, ChunkUtil.indexChunk(topLeftChunk.x, topLeftChunk.y)));
        IO.println(String.format("Bottom-Right (Chunk): %s, %s :: %s", bottomRightChunk.x, bottomRightChunk.y, ChunkUtil.indexChunk(bottomRightChunk.x, bottomRightChunk.y)));
        IO.println();

        ArrayList<Vector2i> chunks = new ArrayList<>();

        //Compute all chunks we need to check.
        for (int chunkX = topLeftChunk.x; chunkX <= bottomRightChunk.x; chunkX++) {
            for (int chunkZ = topLeftChunk.y; chunkZ <= bottomRightChunk.y; chunkZ++) {
                chunks.add(new Vector2i(chunkX, chunkZ));
            }
        }

        IO.println(String.format("Chunks: %s", chunks));
    }

    public static Vector2i toChunkVector(Vector2i source)
    {
        return new Vector2i(
                (source.x - (source.x % chunkSize)) / chunkSize,
                (source.y - (source.y % chunkSize)) / chunkSize);
    }
}
