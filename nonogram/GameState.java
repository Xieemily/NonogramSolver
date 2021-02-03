package com.nonogram;

import javax.swing.text.StyledEditorKit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GameState {
    static int BOARD_SIZE;

    enum CellState {
        FILLED,
        EMPTY,
        UNKNOWN
    }

    CellState[][] board;

    ArrayList<ArrayList<Integer>> hintRow = new ArrayList<>();
    ArrayList<ArrayList<Integer>> hintCol = new ArrayList<>();

    GameState(ArrayList<ArrayList<Integer>> _hintRow, ArrayList<ArrayList<Integer>> _hintCol, int _gameSize) {
        BOARD_SIZE = _gameSize;
        hintRow = _hintRow;
        hintCol = _hintCol;
        board = new CellState[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                board[i][j] = CellState.UNKNOWN;
    }

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

    public CellState getBoard(int x, int y) {
        return board[x][y];
    }

    void ShowBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            // print value of i th row
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == CellState.FILLED)
                    System.out.print(" x ");
                else if (board[i][j] == CellState.EMPTY)
                    System.out.print("   ");
                else
                    System.out.print(" ? ");
            }
            System.out.println();
            // print horizontal line
//            for (int j = 0; j < BOARD_SIZE; j++) System.out.print("---+");
//            System.out.println();
        }
    }

    public Boolean IsSolved(){
        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
                if(board[i][j] == CellState.UNKNOWN)
                    return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

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

    private void GenerateHint() {
        int cnt = 0; // count continuous filled cells
        // row
        for (CellState[] cellStates : board) {
            ArrayList<Integer> arrayList = new ArrayList<>();
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
        // column
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
