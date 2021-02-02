package com.nonogram;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

class SolveGame extends GameState {
    private final Solver solver = new Solver();
    // record sets of possible labels separately
    private final ArrayList<ArrayList<Set<Integer>>> rowListOfSet = new ArrayList<>();
    private final ArrayList<ArrayList<Set<Integer>>> colListOfSet = new ArrayList<>();
    // record maps separately
    private final ArrayList<Map<Integer, Set<Integer>>> rowListOfMapForward = new ArrayList<>();
    private final ArrayList<Map<Integer, Set<Integer>>> colListOfMapForward = new ArrayList<>();
    private final ArrayList<Map<Integer, Set<Integer>>> rowListOfMapBackward = new ArrayList<>();
    private final ArrayList<Map<Integer, Set<Integer>>> colListOfMapBackward = new ArrayList<>();

    SolveGame(ArrayList<ArrayList<Integer>> _hintRow, ArrayList<ArrayList<Integer>> _hintCol, int _gameSize) {
        super(_hintRow, _hintCol, _gameSize);
        InitBoardState();
    }

    /**
     * Initialize possible label sets and maps of board with solver using hints
     */
    private void InitBoardState() {
        for (ArrayList<Integer> hint : hintRow) {
            rowListOfSet.add(solver.InitLine(hint));
            rowListOfMapForward.add(solver.GenerateMap(hint, 1));
            rowListOfMapBackward.add(solver.GenerateMap(hint, -1));
        }
        for (ArrayList<Integer> hint : hintCol) {
            colListOfSet.add(solver.InitLine(hint));
            colListOfMapForward.add(solver.GenerateMap(hint, 1));
            colListOfMapBackward.add(solver.GenerateMap(hint, -1));
        }
    }

    /**
     * Deduct cells with only positive/negative label, determine board state
     *
     * @param setLine label sets of one line
     * @param lineNo  line no to be deducted, set corresponding board state by it
     * @param isRow   Boolean indicates row or column line set is passed
     */
    private void DeductLine(ArrayList<Set<Integer>> setLine, int lineNo, Boolean isRow) {
        for (int i = 0; i < setLine.size(); i++) { // iterate cells in line
            // if cell is determined, skip it
            if ((isRow && board[lineNo][i] != CellState.UNKNOWN)
                    || (!isRow && board[i][lineNo] != CellState.UNKNOWN)) {
                continue;
            }
            int cellSize = setLine.get(i).size();
            int pos = 0;
            int neg = 0;
            for (int k : setLine.get(i)) { // iterate cell elements
                if (k > 0) pos++;
                else neg++;
            }
            if (pos == cellSize) { // all positive, fill cell
                if (isRow) {
                    board[lineNo][i] = CellState.FILLED;
                } else {
                    board[i][lineNo] = CellState.FILLED;
                }
            } else if (neg == cellSize) { // all negative, empty cell
                if (isRow) {
                    board[lineNo][i] = CellState.EMPTY;
                } else {
                    board[i][lineNo] = CellState.EMPTY;
                }
            }
        }
    }

    /**
     * Deduct labels of determined cell, combine results of rows and columns
     *
     * @return changed
     * A boolean tells whether new info is gathered
     */
    private Boolean DeductLabel() {
        Boolean changed = Boolean.FALSE;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == CellState.FILLED) {
                    if (rowListOfSet.get(i).get(j).removeIf(n -> n < 0) ||
                            colListOfSet.get(j).get(i).removeIf(n -> n < 0)) // remove negative
                        changed = Boolean.TRUE;
                } else if (board[i][j] == CellState.EMPTY) {
                    if (rowListOfSet.get(i).get(j).removeIf(n -> n > 0) ||
                            colListOfSet.get(j).get(i).removeIf(n -> n > 0)) // remove positive
                        changed = Boolean.TRUE;
                }
            }
        }
        return changed;
    }

    /**
     * Deduct the whole board with only positive/negative label, determine board state
     */
    private void DeductBoard() {
        for (int i = 0; i < rowListOfSet.size(); i++) {
            DeductLine(rowListOfSet.get(i), i, Boolean.TRUE);
        }
        for (int i = 0; i < colListOfSet.size(); i++) {
            DeductLine(colListOfSet.get(i), i, Boolean.FALSE);
        }
    }


    /**
     * Pass board, trim labels
     */
    private void PassBoard() {
        for (int i = 0; i < rowListOfSet.size(); i++) {
            solver.ForwardPass(rowListOfSet.get(i), rowListOfMapForward.get(i));
            solver.BackwardPass(rowListOfSet.get(i), rowListOfMapBackward.get(i));
        }
        for (int i = 0; i < colListOfSet.size(); i++) {
            solver.ForwardPass(colListOfSet.get(i), colListOfMapForward.get(i));
            solver.BackwardPass(colListOfSet.get(i), colListOfMapBackward.get(i));
        }
    }

    /**
     * Solve pipeline, run until no cell changes state, it's deterministic
     */
    public void SolvePipeline() {
        Boolean changed = Boolean.TRUE;
        while (changed) {
            DeductBoard();
            changed = DeductLabel();
            PassBoard();
        }
    }

}
