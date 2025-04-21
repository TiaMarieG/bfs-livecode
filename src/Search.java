import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Search {
     /**
     * Finds the location of the nearest reachable cheese from the rat's position.
     * Returns a 2-element int array: [row, col] of the closest 'c'. If there are multiple
     * cheeses that are tied for the same shortest distance to reach, return
     * any one of them.
     * 
     * 'R' - the rat's starting position (exactly one)
     * 'o' - open space the rat can walk on
     * 'w' - wall the rat cannot pass through
     * 'c' - cheese that the rat wants to reach
     * 
     * If no rat is found, throws EscapedRatException.
     * If more than one rat is found, throws CrowdedMazeException.
     * If no cheese is reachable, throws HungryRatException
     *
     * oooocwco
     * woowwcwo
     * ooooRwoo
     * oowwwooo
     * oooocooo
     *
     * The method will return [0,4] as the nearest cheese.
     *
     * @param maze 2D char array representing the maze
     * @return int[] location of the closest cheese in row, column format
     * @throws EscapedRatException if there is no rat in the maze
     * @throws CrowdedMazeException if there is more than one rat in the maze
     * @throws HungryRatException if there is no reachable cheese
     */
    public static int[] nearestCheese(char[][] maze) throws EscapedRatException, CrowdedMazeException, HungryRatException {
        // Int array to store the rat's location
        // There should only be one rat in the maze, so the int[] will have a length of 2 - {X, Y}
        int[] start = findRat(maze);

        // Empty queue to store the rat's location
        Queue<int[]> queue = new LinkedList<>();

        // Add the rat's current location to the queue
        queue.add(start);

        // 2D array to keep track of the visited locations in the maze
        boolean[][] visited = new boolean[maze.length][maze[0].length];

        // While the queue isn't empty...
        while(!queue.isEmpty()) {

            // Remove first element from queue and store it in current
            int[] current = queue.poll();
            // Break down the current location into row and column
            int curR = current[0];
            int curC = current[1];

            // Check if the current location is a cheese
            // If it is, return the current location
            if(maze[curR][curC] == 'c') {
                return current;
            }
            // If the current location has already been visited...
            if(visited[curR][curC]) {
                // Skip to the next iteration of the loop
                continue;
            }

            // Mark the current location of the rat as visited
            visited[curR][curC] = true;

            // Check the possible moves the rat can make from the current location
            List<int[]> nextMoves = possibleMoves(maze, current);
            // Add the next possible moves to the queue
            // Go back to the start of the loop - Repeat until the queue is empty
            queue.addAll(nextMoves);
        }

        throw new HungryRatException();
    }

    public static List<int[]> possibleMoves(char[][]maze, int[] currentLoc) {
        // List to store the moves the rat is able to make based on current location
        List<int[]> moves = new ArrayList<>();

        // Directions up, down, left, right
        int[][] steps = {
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1}
        };

        // Breaking down current location into row and column
        int curR = currentLoc[0];
        int curC = currentLoc[1];

        // Loop through strps to find valid next possible moves
        for(int[] step : steps) {
            // New row and column based on the current location and the step being taken
            int newR = curR + step[0];
            int newC = curC + step[1];

            // Check if the new row and column are within bounds of the maze and not a wall
            if(newR >= 0 && newR < maze.length &&
               newC >= 0 && newC < maze[0].length &&
               maze[newR][newC] != 'w') {
                // If within bounds and not a wall, add the new location to the list of moves
                moves.add(new int[]{newR, newC});
               }
        }

        return moves;
    }

    public static int[] findRat(char[][] maze) throws CrowdedMazeException, EscapedRatException {
        // Variable to count the number of rats
        int ratCount = 0;
        // Store the rat's location to be passed back to main 
        //Starting as null since location is unknown
        int[] ratLocation = null;

        // Loop through the 2D array to find the rat ('R')
        for(int r = 0; r < maze.length; r++) {
            for(int c = 0; c < maze[0].length; c++) {
                if(maze[r][c] == 'R') {
                    // Takes the row and column of the rat's location
                    ratLocation = new int[]{r, c};
                    ratCount++;
                }
            }
        }

        // Exception handling
        if(ratCount > 1) throw new CrowdedMazeException();
        if(ratCount == 0) throw new EscapedRatException();

        // Returns the location of the found rat
        return ratLocation;
    }

    public static void main(String[] args){}
}