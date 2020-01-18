/* *****************************************************************************
 *  Name: Arjoban Singh
 *  Date: Jan 17, 19
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class Solver {
    // private Board toSolve;
    // private int N;
    private MinPQ<SearchNode> priorityQueue = new MinPQ<>();
    private MinPQ<SearchNode> forUnslovable = new MinPQ<>();
    private boolean solvable;
    private SearchNode finalNode;

    private class SearchNode implements Comparable<SearchNode>
    {
        private Board board;
        private SearchNode previous;
        private int moves;
        private int priority;

        public SearchNode(Board board, SearchNode previous, int moves)
        {
            this.board = board;
            this.previous = previous;
            this.moves = moves;
            this.priority = board.manhattan() + moves;
        }
        @Override
        public int compareTo(SearchNode o)
        {
            if (this.priority < o.priority) return -1;
            if (this.priority > o.priority) return 1;

            return 0;
        }
    }

    /* private SearchNode cur;
    private SearchNode cur2;

    public Solver(Board initial)
    {
        cur  = new SearchNode(initial, null, 0);
        cur2 = new SearchNode(initial.twin(), null, 0);

        priorityQueue.insert(cur);
        forUnslovable.insert(cur2);

        while (!cur.board.isGoal() && !cur2.board.isGoal())
        {
            cur = priorityQueue.delMin();
            cur2 = forUnslovable.delMin();

            for (Board i: cur.board.neighbors())
            {
                if (cur.previous == null || !i.equals(cur.previous.board))
                {
                    priorityQueue.insert(new SearchNode(i, cur, cur.moves+1));
                }
            }

            for (Board i: cur2.board.neighbors())
            {
                if (cur2.previous == null || !i.equals(cur2.previous.board))
                {
                    forUnslovable.insert(new SearchNode(i, cur2, cur2.moves+1));
                }
            }
        }

        if (cur.board.isGoal())
            solvable = true;
        else
            solvable = false;
    } */


    // find a solution to the initial board (using the A* algorithm)
     public Solver(Board initial)
    {
        if (initial == null) throw new IllegalArgumentException();
        // this.N = initial.dimension();
        // this.toSolve = initial;
        SearchNode head = new SearchNode(initial, null, 0);
        priorityQueue.insert(head);
        SearchNode twinOfHead = new SearchNode(initial.twin(), null, 0);
        forUnslovable.insert(twinOfHead);

        while (!priorityQueue.isEmpty() && !forUnslovable.isEmpty())
        {

            SearchNode parent = priorityQueue.delMin();
            SearchNode parentTwin = forUnslovable.delMin();



            if (parentTwin.board.isGoal())
            {
                // unsolvable
                solvable = false;
                return;
            }

            if (parent.board.isGoal())
            {
                finalNode = parent;
                solvable = true;
                return;
            }
            for (Board child: parent.board.neighbors())
            {
                // SearchNode childNode = new SearchNode(child, parent, parent.moves + 1);
                if (parent.previous == null || !child.equals(parent.previous.board))
                {
                priorityQueue.insert(new SearchNode(child, parent, parent.moves + 1));
                }
            }

            for (Board child: parentTwin.board.neighbors())
            {
                // SearchNode childNode = new SearchNode(child, parentTwin, parentTwin.moves + 1);
                if (parentTwin.previous == null || !child.equals(parentTwin.previous.board))
                {
                    forUnslovable.insert(new SearchNode(child, parentTwin, parentTwin.moves + 1));
                }
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable()
    {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves()
    {
        if (isSolvable())
        {
        return finalNode.moves;
        }
        return -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution()
    {
        if (!isSolvable()) return null;
        LinkedList<Board> iterable = new LinkedList<>();

        SearchNode solutionNode = finalNode;

        while(solutionNode != null)
        {
            iterable.addFirst(solutionNode.board);
            solutionNode = solutionNode.previous;
        }
        return iterable;
    }


    // test client (see below)
    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }

    }
}
