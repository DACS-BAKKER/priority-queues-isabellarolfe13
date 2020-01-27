/*
SOLVER.JAVA
by Isabella Rolfe and Rocky Xia
01/27/2020
*/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Solver {

    //Variables for SolverTest
    private Board initial;
    private Board initialTwin;
    private MinPQ<SearchNode> solutionFinder;
    private MinPQ<SearchNode> solutionFinderTwin;
    private SearchNode solved;
    private SearchNode solvedTwin;
    private Stack solvedStack;
    private int movesSolution;

    //Constructor for SolverTest
    //The puzzle is solved at construction
    public Solver(Board initial) {
        //Instantiate the objects
        this.initial = initial;
        this.initialTwin = initial.twin();
        this.solutionFinder = new MinPQ<SearchNode>();
        this.solutionFinderTwin = new MinPQ<SearchNode>();
        this.solved = null;
        this.solvedTwin = null;

        //Enqueue SearchNodes with the initial
        solutionFinder.insert(new SearchNode(this.initial, 0, null));
        solutionFinderTwin.insert(new SearchNode(initialTwin, 0, null));

        //Dequeue the first two boards in order to set up while loop
        SearchNode temp = solutionFinder.delMin();
        SearchNode tempTwin = solutionFinderTwin.delMin();

        //While loop that runs as long as there are no solved boards
        while (!temp.current.isGoal() && !tempTwin.current.isGoal()) {

            //Foreach loop that enqueues neighbors of the Board of temp into the minPQ
            //Only enqueues boards that are not the same as the previous one
            for (Board board : temp.current.neighbors()) {
                if (board != null) {
                    if (temp.previous == null) {
                        solutionFinder.insert(new SearchNode(board, temp.moves + 1, temp));
                    } else if (temp.previous != null && !board.equals(temp.previous.current)) {
                        solutionFinder.insert(new SearchNode(board, temp.moves + 1, temp));
                    }
                }
            }

            //Moves temp to the next relevant SearchNode
            temp = solutionFinder.delMin();

            //Foreach loop that enqueues neighbors of the Board of tempTwin into the minPQ
            //Only enqueues boards that are not the same as the previous one
            for (Board board : tempTwin.current.neighbors()) {
                if (board != null) {
                    if (tempTwin.previous == null) {
                        solutionFinderTwin.insert(new SearchNode(board, tempTwin.moves + 1, tempTwin));
                    } else if (tempTwin.previous != null && !board.equals(tempTwin.previous.current)) {
                        solutionFinderTwin.insert(new SearchNode(board, tempTwin.moves + 1, tempTwin));
                    }
                }
            }

            //Moves tempTwin to the next relevant SearchNode
            tempTwin = solutionFinderTwin.delMin();
        }

        //Checks which Board is solved and link it to either solved or solvedTwin
        //The two variables would later be used for the class methods
        //Correspond the solved SearchNode to a solved Board if the original board lead to a solution
        //Correspond the solvedTwin SearchNode to a solved Board if the twin leads to a solution
        if (temp.current.isGoal()) {
            solved = temp;
        } else if (tempTwin.current.isGoal()) {
            solvedTwin = tempTwin;
        }

        //Move through the previous SearchNode of each SearchNode and push them onto the stack of solutions
        if (solved != null) {
            solvedStack = new Stack();
            SearchNode stuff = solved;
            while (stuff.previous != null) {
                solvedStack.push(stuff);
                stuff = stuff.previous;
            }
            solvedStack.push(stuff);
        }
    }

    //Method checking if the Board is solvable
    //Checks whether the solution derived from the original board is null
    //Returns true if the original board led to a solution, false if the twin Board leads to a solution
    public boolean isSolvable() {
        if (solved != null) {
            return true;
        } else {
            return false;
        }
    }

    //Returns the number of the moves required to solve the board
    //Since the moves variable is the number of moves leading to that SearchNode, the method retuns one plus that many moves
    //If original Board is unsolvable, returns -1
    public int moves() {
        if (isSolvable()) {
            return ++solved.moves;
        }
        return -1;
    }

    //Iterate through the Stack of solutions to output the sequence to console
    public Iterable<Board> solution() {
        class StackIterator implements Iterator<Board> {
            private int current = 0;

            public boolean hasNext() {
                return !solvedStack.isEmpty();
            }

            public Board next() {
                SearchNode temp = solvedStack.pop().searchNode;
                if (temp != null) {
                    return temp.current;
                } else {
                    return null;
                }
            }
        }

        class StackIterable implements Iterable<Board> {
            public Iterator<Board> iterator() {
                return new StackIterator();
            }
        }

        return new StackIterable();
    }

    //Test client from the assignment overview
    public static void main(String[] args) {

        /*
        TEST CLIENT TO READ IN PUZZLE FROM A FILE
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                blocks[i][j] = in.readInt();
            }
        }
         */

        //EXAMPLE 1: 3 x 3 (solvable)
        int arr[][] = new int[3][3];
        arr[0][0] = 0;
        arr[0][1] = 1;
        arr[0][2] = 3;
        arr[1][0] = 4;
        arr[1][1] = 2;
        arr[1][2] = 5;
        arr[2][0] = 7;
        arr[2][1] = 8;
        arr[2][2] = 6;

        //EXAMPLE 2: 3 x 3 (unsolvable)
        /*
        int arr[][] = new int[3][3];
        arr[0][0] = 1;
        arr[0][1] = 2;
        arr[0][2] = 3;
        arr[1][0] = 4;
        arr[1][1] = 5;
        arr[1][2] = 6;
        arr[2][0] = 8;
        arr[2][1] = 7;
        arr[2][2] = 0;
         */

        //EXAMPLE 3: 4 x 4 (solvable)
        /*
        int arr[][] = new int[4][4];
        arr[0][0] = 1;
        arr[0][1] = 2;
        arr[0][2] = 3;
        arr[0][3] = 4;
        arr[1][0] = 5;
        arr[1][1] = 6;
        arr[1][2] = 7;
        arr[1][3] = 8;
        arr[2][0] = 0;
        arr[2][1] = 10;
        arr[2][2] = 11;
        arr[2][3] = 12;
        arr[3][0] = 9;
        arr[3][1] = 13;
        arr[3][2] = 14;
        arr[3][3] = 15;
         */

        //EXAMPLE 4: 2 x 2 (solvable)
        /*
        int arr[][] = new int[2][2];
        arr[0][0] = 1;
        arr[0][1] = 2;
        arr[1][0] = 0;
        arr[1][1] = 3;
         */

        //EXAMPLE 5: 2 x 2 (unsolvable)
        /*
        int arr[][] = new int[2][2];
        arr[0][0] = 0;
        arr[0][1] = 2;
        arr[1][0] = 3;
        arr[1][1] = 1;
         */

        Board initial = new Board(arr);
        //solve the puzzle
        Solver solver = new Solver(initial);
        //print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            int x = solver.moves() - 1;
            if (x == 0) {
                System.out.println("The board is already solved!");
            }
            StdOut.println("Minimum number of moves: " + x);
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }

    //Search node for board
    private class SearchNode implements Comparable<SearchNode> {

        //Variables for the SearchNode
        public Board current;
        public int moves;
        public SearchNode previous;

        //Constructor for SearchNode
        public SearchNode(Board current, int moves, SearchNode previous) {
            this.current = current;
            this.moves = moves;
            this.previous = previous;
        }

        //Compare priorities of this SearchNode to another SearchNode
        //Returns 1 if this SearchNode is greater
        //Returns -1 if this SearchNode is lesser
        //Returns 0 if the SearchNodes are equal
        public int compareTo(SearchNode searchNode) {
            if ((current.manhattan() + moves) > (searchNode.current.manhattan() + searchNode.moves)) {
                return 1;
            } else if ((current.manhattan() + moves) < (searchNode.current.manhattan() + searchNode.moves)) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    //Stack class for solution output
    private class Stack {

        //Top node for Stack
        private StackNode top;

        //Size variable for stack
        private int size;

        //Constructor for Stack
        private Stack() {
            this.top = null;
        }

        //Pushes a new SearchNode-containing StackNode onto the Stack
        private void push(SearchNode searchNode) {
            if (top == null) {
                top = new StackNode(searchNode);
            } else {
                StackNode temp = top;
                top = new StackNode(searchNode);
                top.next = temp;
            }
        }

        //Pops and returns the top StackNode
        private StackNode pop() {
            StackNode temp = top;
            top = top.next;
            return temp;
        }

        //Checks if the Stack is empty
        private boolean isEmpty() {
            if (top == null) {
                return true;
            }
            return false;
        }
    }

    //Specialized node for Stack
    private class StackNode {

        //Variables for the StackNode
        private SearchNode searchNode;
        private StackNode next;

        //Constructor for StackNode
        private StackNode(SearchNode searchNode) {
            this.searchNode = searchNode;
            this.next = null;
        }
    }
}