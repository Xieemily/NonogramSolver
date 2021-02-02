package com.nonogram;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GameState state = new GameState("D:/learn/2020_9/software/img/eagle2.png", 10);
        state.ShowBoard();
        System.out.println("hintRow:"+state.hintRow);
        System.out.println("hintCol:"+state.hintCol);
        SolveGame solveGame = new SolveGame(state.hintRow, state.hintCol, 10);
        solveGame.SolvePipeline();
    }
}
