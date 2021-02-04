package com.nonogram;

import javax.swing.text.StyledEditorKit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameState {
    static int BOARD_SIZE;

    enum CellState {
        FILLED,
        EMPTY,
        UNKNOWN
    }

    CellState[][] board;

    List<List<Integer>> hintRow = new ArrayList<>();
    List<List<Integer>> hintCol = new ArrayList<>();

    /**
     * For test
     */
    GameState(int _gameSize){
        BOARD_SIZE = _gameSize;
        board = new CellState[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                board[i][j] = CellState.UNKNOWN;
    }

    /**
     * Generate game from hint
     * @param _hintRow list of list of hint
     * @param _hintCol list of list of hint
     * @param _gameSize board length, assume board is square
     */
    GameState(List<List<Integer>> _hintRow, List<List<Integer>> _hintCol, int _gameSize) {
        BOARD_SIZE = _gameSize;
        hintRow = _hintRow;
        hintCol = _hintCol;
        board = new CellState[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                board[i][j] = CellState.UNKNOWN;
    }

    /**
     * Generate game when image is specified, hint is generated
     * @param path image path
     * @param _gameSize board length, assume board is square
     * @throws IOException input error
     */
    GameState(String path, int _gameSize) throws IOException {
        BOARD_SIZE = _gameSize;
        board = new CellState[BOARD_SIZE][BOARD_SIZE];
        GenerateGameFromImg(path);
        GenerateHint();
    }


    void Move(int x, int y) {
        board[x][y] = CellState.FILLED;
    }

    void Remove(int x, int y) {
        board[x][y] = CellState.EMPTY;
    }

    public static int getBoardSize() {
        return BOARD_SIZE;
    }

    public void setBoard(CellState state, int x, int y) {
        board[x][y] = state;
    }

    void ShowBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            // print value of i th row
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == CellState.FILLED)
                    System.out.print("\u2B1B");
                else if (board[i][j] == CellState.EMPTY)
                    System.out.print("\u2B1C");
                else
                    System.out.print("\u2B50");
            }
            System.out.println();
            // print horizontal line
//            for (int j = 0; j < BOARD_SIZE; j++) System.out.print("---+");
//            System.out.println();
        }
    }

    /**
     * Check whether the game is solved or not
     * @return bool, ture for solved
     */
    public Boolean IsSolved(){
        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
                if(board[i][j] == CellState.UNKNOWN)
                    return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    /**
     * Generate board from image by calling image process class method
     * @param path image path
     * @throws IOException error read
     */
    private void GenerateGameFromImg(String path) throws IOException {
        ImgProcess imgProcess = new ImgProcess();
        imgProcess.ProcessImage(path, BOARD_SIZE);

        // copy processed image array to board
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board.length; j++) {
                if (imgProcess.rec[i][j] == 1) {
                    board[i][j] = CellState.FILLED;
                } else
                    board[i][j] = CellState.EMPTY;
            }

    }

    /**
     * Generate hint from board, used when image input available
     */
    private void GenerateHint() {
        int cnt = 0; // counter for continuous filled cells
        // row
        for (CellState[] cellStates : board) {
            List<Integer> arrayList = new ArrayList<>();
            for (int j = 0; j < board.length; j++) {
                if (cellStates[j] == CellState.FILLED) {
                    cnt++; // count continuous cells
                    if (j == board.length - 1) {
                        arrayList.add(cnt); // last cell is filled, store cnt
                        cnt = 0;
                    }
                } else { // this cell is empty, store number of previous continuous cells
                    if (cnt != 0) arrayList.add(cnt);
                    cnt = 0; // reset cnt
                }
            }
            hintRow.add(arrayList);
        } // end for
        cnt = 0;
        // column, same as above
        for (int i = 0; i < board.length; i++) {
            ArrayList<Integer> arrayList = new ArrayList<>();
            for (int j = 0; j < board.length; j++) {
                if (board[j][i] == CellState.FILLED) {
                    cnt++;
                    if (j == board.length - 1) {
                        arrayList.add(cnt); // last cell
                        cnt = 0;
                    }
                } else {
                    if (cnt != 0) arrayList.add(cnt);
                    cnt = 0;
                }
            }
            hintCol.add(arrayList);
        } // end for
    } // end GenerateHint

    /**
     * Generate string representation for board, 0 for empty, 1 for filled, 2 for undefined
     * @return string of board state
     */
    String GenerateString(){
        String str = new String();
        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
                if(board[i][j] == CellState.UNKNOWN)
                    str += 2;
                else if(board[i][j] == CellState.FILLED)
                    str += 1;
                else
                    str += 0;
            }
        }
        return str;
    }

    /**
     * Generate hash for board
     * @return hash value
     */
    int BoardHash(){
        return Arrays.deepHashCode(board);
    }

}
