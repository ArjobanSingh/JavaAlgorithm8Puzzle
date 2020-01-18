/* *****************************************************************************
 *  Name: Arjoban Singh
 *  Date: jan 17, 19
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;

public class Board {

    private int[][] board;
    private final int N;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    // assuming 2 <= n < 128
    public Board(int[][] tiles)
    {
        N = tiles.length;
        if (N < 2 || N >= 128) throw new IndexOutOfBoundsException();

        board = new int[N][N];
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                board[i][j] = tiles[i][j];
            }
        }

    }

    // string representation of this board
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");

        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                s.append(String.format("%2d ", board[i][j]));
            }

            s.append("\n");
        }

        return s.toString();
    }


    // board dimension n
    public int dimension() { return N; }

    // number of tiles out of place
    public int hamming()
    {
        int hamming = 0;
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if (board[i][j] == 0)
                {
                    continue;
                }
                if (numberForRowAndCol(i + 1, j + 1) != board[i][j])
                {
                    hamming += 1;
                }
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan()
    {
        int manhattan = 0;
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if (board[i][j] == 0) continue;

                // required row in which board[i][j] element should be
                int row = (board[i][j] - 1) / N;

                // required column in which board[i][j] element should be
                int col = (board[i][j] - 1) % N;

                // this will give us the distance from correct row in positive value
                int rowMovesRequired = Math.abs(i - row);

                // this will give us the distance from correct column in positive value
                int colMovesRequired = Math.abs(j - col);
                int total = rowMovesRequired + colMovesRequired;
                manhattan += total;

            }
        }
        return manhattan;
    }

    // does this board equal y?
    public boolean equals(Object y)
    {

        if (y == null) return false;
        if (!(y.getClass() == this.getClass()))
        {
            return false;
        }
        if (y == this) return true;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;

        for (int i = 0; i < that.dimension(); i++)
        {
            for (int j = 0; j < that.dimension(); j++)
            {
                if (that.board[i][j] != this.board[i][j])
                {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors()
    {
        Queue<Board> neighbours = new Queue<>();

        int indexIforZero = 0;
        int indexJforZero = 0;

        outerloop:
        for (int i = 0; i < this.dimension(); i++)
        {
            for (int j = 0; j < this.dimension(); j++)
            {
                if (board[i][j] == 0)
                {
                    indexIforZero = i;
                    indexJforZero = j;
                    break outerloop;
                }
            }
        }

        // it has upper neigthbour;
        if (indexIforZero - 1 >= 0)
        {
            neighbours.enqueue(makeBoard("top", indexIforZero, indexJforZero));
        }

        // it has bottom neigthbour;
        if (indexIforZero + 1 < this.dimension())
        {
            neighbours.enqueue(makeBoard("bottom", indexIforZero, indexJforZero));
        }

        // it has left neigthbour;
        if (indexJforZero - 1 >= 0)
        {
            neighbours.enqueue(makeBoard("left", indexIforZero, indexJforZero));
        }

        // it has right neigthbour;
        if (indexJforZero + 1 < this.dimension())
        {
            neighbours.enqueue(makeBoard("right", indexIforZero, indexJforZero));
        }

        return neighbours;
    }

    private Board makeBoard(String neighnorToChange, int indexIForZero, int indexJForZero)
    {
        if (neighnorToChange.equals("top"))
        {
            int[][] clonedArray = copyArray();
            int helper = clonedArray[indexIForZero - 1][indexJForZero];
            clonedArray[indexIForZero - 1][indexJForZero] = 0;
            clonedArray[indexIForZero][indexJForZero] = helper;
            Board thisBoard = new Board(clonedArray);
            return thisBoard;
        }
        else if (neighnorToChange.equals("bottom"))
        {
            int[][] clonedArray = copyArray();
            int helper = clonedArray[indexIForZero + 1][indexJForZero];
            clonedArray[indexIForZero + 1][indexJForZero] = 0;
            clonedArray[indexIForZero][indexJForZero] = helper;
            Board thisBoard = new Board(clonedArray);
            return thisBoard;
        }
        else if (neighnorToChange.equals("left"))
        {
            int[][] clonedArray = copyArray();
            int helper = clonedArray[indexIForZero][indexJForZero - 1];
            clonedArray[indexIForZero][indexJForZero - 1] = 0;
            clonedArray[indexIForZero][indexJForZero] = helper;
            Board thisBoard = new Board(clonedArray);
            return thisBoard;
        }
        else
        {
            int[][] clonedArray = copyArray();
            int helper = clonedArray[indexIForZero][indexJForZero + 1];
            clonedArray[indexIForZero][indexJForZero + 1] = 0;
            clonedArray[indexIForZero][indexJForZero] = helper;
            Board thisBoard = new Board(clonedArray);
            return thisBoard;
        }
    }

    private int[][] copyArray()
    {
        int[][] newArray = new int[this.dimension()][this.dimension()];
        for (int i = 0; i < this.dimension(); i++)
        {
            for (int j = 0; j < this.dimension(); j++)
            {
                newArray[i][j] = board[i][j];
            }
        }
        return newArray;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin()
    {
        int[][] copy = copyArray();

        if (N <= 1) return new Board(copy);


        int row = 0;
        int col = 0;
        int value = 0;
        int lastValue = board[0][0];


        zerosearch:
        for (row = 0; row < N; row++)
        {
            for (col = 0; col < N; col++)
            {
                value = board[row][col];
                if (value != 0 && lastValue != 0 && col > 0) break zerosearch;
                lastValue = value;
            }
        }

        copy[row][col]     = lastValue;
        copy[row][col - 1] = value;

        return new Board(copy);



        /*
        int[][] copy = copyArray();
        int i = StdRandom.uniform(dimension() - 1);
        int j = StdRandom.uniform(dimension() - 1);
        int firstVal = copy[i][j];
        int secondVal = copy[i][j + 1];

        if (firstVal != 0 && secondVal != 0)
        {
            copy[i][j] = secondVal;
            copy[i][j + 1] = firstVal;

        }
        else
        {
            firstVal = copy[i + 1][j];
            secondVal = copy[i + 1][j + 1];
            copy[i + 1][j] = secondVal;
            copy[i + 1][j + 1] = firstVal;
        }

        return new Board(copy);
        */
    }


    // is this board the goal board?
    public boolean isGoal()
    {
        return hamming() == 0;
    }


    private int numberForRowAndCol(int row, int col)
    {
        return ((row - 1) * this.N) + col;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] twoDArray = new int[3][3];
        int[][] other =twoDArray;

        twoDArray[0][0] = 8;
        twoDArray[0][1] = 1;
        twoDArray[0][2] = 3;
        twoDArray[1][0] = 4;
        twoDArray[1][1] = 0;
        twoDArray[1][2] = 2;
        twoDArray[2][0] = 7;
        twoDArray[2][1] = 6;
        twoDArray[2][2] = 5;
        Board board = new Board(twoDArray);
        Board secondBoard = new Board(other);
        System.out.println(board.toString());
        System.out.println(board.hamming());
        System.out.println(board.manhattan());
        System.out.println(board.dimension());
        System.out.println(board.twin());
        System.out.println(board.neighbors());
        System.out.println(board.isGoal());
        System.out.println(board.equals(secondBoard));
}
}
