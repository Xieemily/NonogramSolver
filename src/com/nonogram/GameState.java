package com.nonogram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Record game state, construct by hints or image.
 */
class GameState {
    static int BOARD_SIZE_ROW;
    static int BOARD_SIZE_COL;
    CellState[][] board;
    List<List<Integer>> hintRow = new ArrayList<>();
    List<List<Integer>> hintCol = new ArrayList<>();
    /**
     * For test. Should never be used.
     */
    public GameState(int _gameSizeRow, int _gameSizeCol) {
        BOARD_SIZE_ROW = _gameSizeRow;
        BOARD_SIZE_COL = _gameSizeCol;
        board = new CellState[BOARD_SIZE_ROW][BOARD_SIZE_COL];
        for (int i = 0; i < BOARD_SIZE_ROW; i++)
            for (int j = 0; j < BOARD_SIZE_COL; j++)
                board[i][j] = CellState.UNKNOWN;
    }

    /**
     * Generate game from hint.
     *
     * @param _hintRow     list of list of hint
     * @param _hintCol     list of list of hint
     * @param _gameSizeRow board row length
     * @param _gameSizeCol board col length
     */
    public GameState(List<List<Integer>> _hintRow, List<List<Integer>> _hintCol,
              int _gameSizeRow, int _gameSizeCol) {
        BOARD_SIZE_ROW = _gameSizeRow;
        BOARD_SIZE_COL = _gameSizeCol;
        hintRow = _hintRow;
        hintCol = _hintCol;
        board = new CellState[BOARD_SIZE_ROW][BOARD_SIZE_COL];
        for (int i = 0; i < BOARD_SIZE_ROW; i++)
            for (int j = 0; j < BOARD_SIZE_COL; j++)
                board[i][j] = CellState.UNKNOWN;
    }

    /**
     * Generate game when image is specified, hint is generated.
     *
     * @param path         image path
     * @param _gameSizeRow board length y
     * @param _gameSizeCol board length x
     * @throws IOException input error
     */
    GameState(String path, int _gameSizeRow, int _gameSizeCol) throws IOException {
        BOARD_SIZE_ROW = _gameSizeRow;
        BOARD_SIZE_COL = _gameSizeCol;
        board = new CellState[BOARD_SIZE_ROW][BOARD_SIZE_COL];
        GenerateGameFromImg(path);
        GenerateHint();
    }

    public static int getBoardSizeRow() {
        return BOARD_SIZE_ROW;
    }

    public static int getBoardSizeCol() {
        return BOARD_SIZE_COL;
    }

    public void setBoard(CellState state, int x, int y) {
        board[x][y] = state;
    }

    void ShowBoard() {
        // output pretty square symbol if possible by change to the following utf-8 code
        String[] utf = {"\u2B1B", "\u2B1C", "\u2B50"};

        // output "x" as default
        for (int i = 0; i < BOARD_SIZE_ROW; i++) {
            // print value of i th row
            for (int j = 0; j < BOARD_SIZE_COL; j++) {
                if (board[i][j] == CellState.FILLED)
                    System.out.print("x "); // utf[0]
                else if (board[i][j] == CellState.EMPTY)
                    System.out.print("  "); // utf[1]
                else
                    System.out.print("? "); // utf[2]
            }
            System.out.println();
        }
    }

    /**
     * Check whether the game is solved or not.
     *
     * @return bool, ture for solved
     */
    public Boolean IsSolved() {
        for (int i = 0; i < BOARD_SIZE_ROW; i++) {
            for (int j = 0; j < BOARD_SIZE_COL; j++) {
                if (board[i][j] == CellState.UNKNOWN)
                    return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    /**
     * Generate board from image by calling image process class method.
     *
     * @param path image path
     * @throws IOException error read
     */
    private void GenerateGameFromImg(String path) throws IOException {
        ImgProcess imgProcess = new ImgProcess();
        imgProcess.ProcessImage(path, BOARD_SIZE_ROW, BOARD_SIZE_COL);

        // copy processed image array to board
        for (int i = 0; i < GameState.getBoardSizeRow(); i++)
            for (int j = 0; j < GameState.getBoardSizeCol(); j++) {
                if (imgProcess.rec[i][j] == 1) {
                    board[i][j] = CellState.FILLED;
                } else
                    board[i][j] = CellState.EMPTY;
            }

    }

    /**
     * Generate hint from board, used when image input available.
     */
    private void GenerateHint() {
        int cnt = 0; // counter for continuous filled cells
        // row
        for (CellState[] cellStates : board) {
            List<Integer> arrayList = new ArrayList<>();
            for (int j = 0; j < GameState.getBoardSizeCol(); j++) {
                if (cellStates[j] == CellState.FILLED) {
                    cnt++; // count continuous cells
                    if (j == GameState.getBoardSizeCol() - 1) {
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
        for (int i = 0; i < GameState.getBoardSizeCol(); i++) {
            ArrayList<Integer> arrayList = new ArrayList<>();
            for (int j = 0; j < GameState.getBoardSizeRow(); j++) {
                if (board[j][i] == CellState.FILLED) {
                    cnt++;
                    if (j == GameState.getBoardSizeRow() - 1) {
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
     * Generate string representation for board, 0 for empty, 1 for filled, 2 for undefined.
     *
     * @return string of board state
     */
    String GenerateString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < BOARD_SIZE_ROW; i++) {
            for (int j = 0; j < BOARD_SIZE_COL; j++) {
                if (board[i][j] == CellState.UNKNOWN)
                    str.append(2);
                else if (board[i][j] == CellState.FILLED)
                    str.append(1);
                else
                    str.append(0);
            }
        }
        return str.toString();
    }

    /**
     * Generate hash of board.
     *
     * @return hash value
     */
    int BoardHash() {
        return Arrays.deepHashCode(board);
    }

    enum CellState {
        FILLED,
        EMPTY,
        UNKNOWN
    }

}
