package com.nonogram;

import java.io.IOException;

class Main {
    public static void main(String[] args) throws IOException {
        GameState state = new GameState("D:/learn/2020_9/software/img/dog.jpg", 200);
        state.ShowBoard();
        System.out.println("hintRow:"+state.hintRow);
        System.out.println("hintCol:"+state.hintCol);
        SolveGame solveGame = new SolveGame(state.hintRow, state.hintCol, 200);
        solveGame.SolvePipeline();
    }
}
