package com.nonogram;

import java.io.File;
import java.lang.reflect.Array;
import java.util.*;

class SolveGame extends GameState {
    private final Solver solver = new Solver();
    // record sets of possible labels separately
    private List<List<Set<Integer>>> rowListOfSet = new ArrayList<>();
    private List<List<Set<Integer>>> colListOfSet = new ArrayList<>();
    // record maps separately
    private List<Map<Integer, Set<Integer>>> rowListOfMapForward = new ArrayList<>();
    private List<Map<Integer, Set<Integer>>> colListOfMapForward = new ArrayList<>();
    private List<Map<Integer, Set<Integer>>> rowListOfMapBackward = new ArrayList<>();
    private List<Map<Integer, Set<Integer>>> colListOfMapBackward = new ArrayList<>();
    // record visited board hash
    Set<Integer> vis = new HashSet<>();
    // record solution
    ArrayList<String> solution = new ArrayList<>();
    int numSolution = 0;
    int SOLUTION_NEED = 50;

    SolveGame(List<List<Integer>> _hintRow, List<List<Integer>> _hintCol,
              int _gameSize) {
        super(_hintRow, _hintCol, _gameSize);
        InitBoardState();
    }

    public void SetSolutionNeed(int need){
        SOLUTION_NEED = need;
    }

    /**
     * Initialize possible label sets and maps of board with solver using hints
     */
    private void InitBoardState() {
        for (List<Integer> hint : hintRow) {
            rowListOfSet.add(solver.InitLine(hint));
            rowListOfMapForward.add(solver.GenerateMap(hint, 1));
            rowListOfMapBackward.add(solver.GenerateMap(hint, -1));
        }
        for (List<Integer> hint : hintCol) {
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
    private void DeductLine(List<Set<Integer>> setLine, int lineNo, Boolean isRow) {
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

    /**
     * Deep copy matrix
     * @param matrix matrix to be copied
     * @param <T> element type
     * @return Copied matrix
     */
    <T> T[][] deepCopy(T[][] matrix) {
        return java.util.Arrays.stream(matrix).map(el -> el.clone()).toArray($ -> matrix.clone());
    }

    /**
     * Deep copy sets of board
     * @return Copied list of list of set
     */
    List<List<Set<Integer>>> CopySetList(List<List<Set<Integer>>> list){
        List<List<Set<Integer>>> copied = new ArrayList<>();
        for(int i = 0; i < BOARD_SIZE; i++){
            ArrayList<Set<Integer>> listInner = new ArrayList<>();
            for(int j = 0; j < BOARD_SIZE; j++){
                Set<Integer> set = new HashSet<>(list.get(i).get(j));
                listInner.add(set);
            }
            copied.add(listInner);
        }
        return copied;
    }

    /**
     * Check whether the game is not solvable
     * @return bool, ture for unsolvable
     */
    public Boolean ErrorState(){
        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
                if(rowListOfSet.get(i).get(j).isEmpty() || colListOfSet.get(i).get(j).isEmpty())
                    return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * For game with multiple solutions, guess is needed, use dfs search
     */
    public void Guess(){
        if(vis.contains(BoardHash())){ // board occurred
            return;
        } else {
            vis.add(BoardHash()); // mark board as visited
        }
        if(IsSolved()){
            numSolution ++;
            String s = GenerateString();
            System.out.println("solution" + numSolution + ":" + s);
            solution.add(s); // record solution by string representation
            ShowBoard();
            return;
        }
        // find unknown cells
        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
                Set<Integer> set = new HashSet<>(rowListOfSet.get(i).get(j));
                if(set.size() > 1){
                    // store current state
                    CellState[][] recBoard = deepCopy(board);
                    List<List<Set<Integer>>> recRowListOfSet = CopySetList(rowListOfSet);
                    List<List<Set<Integer>>> recColListOfSet = CopySetList(colListOfSet);

                    // loop possible value of one cell
                    for(int k: set){
                        rowListOfSet.get(i).get(j).clear();
                        rowListOfSet.get(i).get(j).add(k);
                        SolvePipeline();
                        Guess(); // recursive call

                        // there might be hundreds of solutions, comment it if want to find them all
                        // it can be time consuming, make sure the amount of solution is limited
                        // 50(default) solution is enough for most cases
                        if(numSolution >= SOLUTION_NEED)return;


                        // drop changed lists and rollback, let garbage collector do the job
                        rowListOfSet = recRowListOfSet;
                        colListOfSet = recColListOfSet;

                        board = recBoard;

                    }
                }
            }
        }
    }

}
