package chjees.tools.test;

/**
 * <p>Spiral pattern testing class.</p>
 * <p><b>Note:</b> This is a barebones example with no fancy techniques.</p>
 */
public class GeneratePattern {
    static void main() {
        IO.println("Generating spreading pattern for insertion.");
        IO.println();
        //# Argument
        int maxDistance = 50;
        //# Working variables
        int currentDistance = 0;
        int currentSquare = 1; //3 x 3
        int currentX = 0;
        int currentZ = -1;
        //## Indicates the "direction" we are adding towards.
        int direction = 1; //0=Up, 1=Right, 2=Down, 3=Left
        while (currentDistance < maxDistance)
        {
            IO.println(String.format("%d :: (%d, %d)", currentDistance, currentX, currentZ));
            //Set direction and clamp
            switch (direction) {
                case 0: //Up
                {
                    currentZ--;
                    if(currentZ < -currentSquare)
                    {
                        currentSquare++;
                        direction = 1; //Switch direction
                    }
                }
                break;
                case 1: //Right
                {
                    currentX++;
                    if(currentX == currentSquare)
                        direction = 2; //Switch direction
                }
                break;
                case 2: //Down
                {
                    currentZ++;
                    if(currentZ == currentSquare)
                        direction = 3; //Switch direction
                }
                break;
                case 3: //Left
                {
                    currentX--;
                    if(currentX == -currentSquare)
                        direction = 0; //Switch direction
                }
            }
            currentDistance++;
        }
    }
}
