package com.nonogram;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

class SolveGameTest {

    // Test by generating hint from image and solve it
    @Test
    void CombinedTest() throws IOException {
        GameState state = new GameState("D:/learn/2020_9/software/img/einstein.jpg",
                50);
        state.ShowBoard();
        System.out.println("hintRow:"+state.hintRow);
        System.out.println("hintCol:"+state.hintCol);

        SolveGame solveGame = new SolveGame(state.hintRow, state.hintCol,
                50);

        long startTime = System.nanoTime();

        solveGame.SolvePipeline();
        solveGame.ShowBoard();
        if(solveGame.ErrorState()){
            System.out.println("Not valid game!");
            return;
        }
        solveGame.Guess();

        long endTime = System.nanoTime();
        long duration = (endTime - startTime)/1000000;  //divide by 1000000 to get milliseconds.

        System.out.println("solve completed in " + duration + "ms");

        assertArrayEquals(state.board, solveGame.board);
    }


}