/*
BOARD.JAVA
by Isabella Rolfe and Rocky Xia
01/27/2020
 */
import java.util.*;

public class Board {
    private int[][] board;
    private int row;
    private int col;
    private int moves;


    //constructs a board from an N-by-N array of blocks
    //where  blocks [i][j] = block in row i, column j
    public Board(int[][] blocks) {
        row = blocks.length;
        col = blocks.length;
        board = new int[row][col];
        for (int x = 0; x < row; x++) {
            for (int y = 0; y < col; y++) {
                board[x][y] = blocks[x][y];
            }
        }
    }

    //board dimension N
    public int dimensions() {
        return row;
    }

    //number of blocks out of place
    public int hamming() {
        int count = 0;
        //add moves and number of blocks in right spot
        int[][] sol = solutionBoard();
        for (int x = 0; x < row; x++) {
            for (int y = 0; y < col; y++) {
                if (board[x][y] != sol[x][y]) {
                    if (!(x == col - 1 && y == col - 1)) {
                        count++;
                    }
                }
            }
        }
        return count + moves;
    }

    //returns the board that is the solution
    public int[][] solutionBoard() {
        int arr[][] = new int[row][col];
        for (int x = 0; x < row; x++) {
            for (int y = 0; y < col; y++) {
                arr[x][y] = (y + 1) + (x * col);
            }
        }
        return arr;
    }

    //sum of Manhattan distances between blocks and goals
    public int manhattan() {
        int count = 0;
        //add moves and distance of each block from correct solution
        //distance-take absolute value of x1-x2 + y1-y2
        int[][] sol = solutionBoard();
        for (int x = 0; x < row; x++) {
            for (int y = 0; y < col; y++) {
                int num = board[x][y];
                if (num != 0) {
                    int colForSol = (num - 1) % row;
                    int rowForSol = (num - 1) / col;
                    count += Math.abs((y - colForSol)) + Math.abs((x - rowForSol));
                }
            }
        }
        return count + moves;
    }

    //returns if this board is the goal board
    public boolean isGoal() {
        int check = 1;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                if (check == board.length * board.length) {
                    if (board[row][col] != 0) {
                        return false;
                    }
                } else {
                    if (board[row][col] != check) {
                        return false;
                    } else {
                        check++;
                    }
                }
            }
        }
        return true;
    }

    //a board that is obtained bu exchanging any pair of blocks
    public Board twin() {
        Board twin = new Board(board);
        //check for first and second position equal 0
        if (twin.board[0][0] == 0) {
            int temp = twin.board[0][1];
            twin.board[0][1] = twin.board[1][0];
            twin.board[1][0] = temp;
        } else if (twin.board[0][1] == 0) {
            int temp = twin.board[0][0];
            twin.board[0][0] = twin.board[1][0];
            twin.board[1][0] = temp;
        } else {
            int temp = twin.board[0][0];
            twin.board[0][0] = twin.board[0][1];
            twin.board[0][1] = temp;
        }
        return twin;
    }

    //checks to see if board equals board y
    public boolean equals(Board y) {
        for (int x = 0; x < row; x++) {
            for (int i = 0; i < col; i++) {
                if (y.board[x][i] != board[x][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    //all neighboring boards (iterable utilized in solver class)
    public Iterable<Board> neighbors() {
        class NeighborsIterator implements Iterator<Board> {
            int current = 1;

            public boolean hasNext() {
                return current <= 4;
            }

            public Board next() {
                int rowSpace = 0;
                int colSpace = 0;
                for (int row = 0; row < board.length; row++) {
                    for (int col = 0; col < board.length; col++) {
                        if (board[row][col] == 0) {
                            rowSpace = row;
                            colSpace = col;
                        }
                    }
                }

                if (current == 1) {
                    current++;
                    if (rowSpace > 0) {
                        return swap(rowSpace, colSpace, rowSpace - 1, colSpace);
                    }
                } else if (current == 2) {
                    current++;
                    if (colSpace < board.length - 1) {
                        return swap(rowSpace, colSpace, rowSpace, colSpace + 1);
                    }
                } else if (current == 3) {
                    current++;
                    if (rowSpace < board.length - 1) {
                        return swap(rowSpace, colSpace, rowSpace + 1, colSpace);
                    }
                } else if (current == 4) {
                    current++;
                    if (colSpace > 0) {
                        return swap(rowSpace, colSpace, rowSpace, colSpace - 1);
                    }
                }
                return null;
            }

            private Board swap(int rowSpace, int colSpace, int rowTarget, int colTarget) {
                Board swapped = new Board(board);
                swapped.board[rowSpace][colSpace] = swapped.board[rowTarget][colTarget];
                swapped.board[rowTarget][colTarget] = 0;
                return swapped;
            }
        }

        class NeighborsIterable implements Iterable<Board> {
            public Iterator<Board> iterator() {
                return new NeighborsIterator();
            }
        }

        return new NeighborsIterable();
    }

    //string representation of board
    public String toString() {
        String output = "";
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                output += board[row][col] + " ";
            }
            output += "\n";
        }
        return output;
    }

    //unit tests
    public static void main(String[] args) {
        int sol[][] = new int[3][3];
        sol[0][0] = 1;
        sol[0][1] = 2;
        sol[0][2] = 3;
        sol[1][0] = 4;
        sol[1][1] = 5;
        sol[1][2] = 6;
        sol[2][0] = 7;
        sol[2][1] = 8;
        sol[2][2] = 0;
        Board board1 = new Board(sol);
        System.out.println(board1.toString());
        System.out.println(board1.twin());
        System.out.println(board1.isGoal());
        System.out.println(board1.hamming());
        System.out.println(board1.manhattan());
    }
}