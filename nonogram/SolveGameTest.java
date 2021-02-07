package com.nonogram;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

public class SolveGameTest {

    // Test by generating hint from image and solve it
    // square board
    @Test
    public void CombinedTestSquare() throws IOException {
        GameState state = new GameState("D:/learn/2020_9/software/img/einstein.jpg",
                50, 50);
        state.ShowBoard();
        System.out.println("hintRow:"+state.hintRow);
        System.out.println("hintCol:"+state.hintCol);

        SolveGame solveGame = new SolveGame(state.hintRow, state.hintCol,
                50, 50);

        SolveAndClock(state, solveGame);
    }

    // rectangle board
    @Test
    public void CombinedTestRec() throws IOException {
        GameState state = new GameState("D:/learn/2020_9/software/img/einstein.jpg",
                50, 51);

        SolveGame solveGame = new SolveGame(state.hintRow, state.hintCol,
                50, 51);

        SolveAndClock(state, solveGame);
    }

    // multiple solutions 40*40
    @Test
    public void CombinedTestMultiMid() throws IOException {
        GameState state = new GameState("D:/learn/2020_9/software/img/einstein.jpg",
                40, 40);

        SolveGame solveGame = new SolveGame(state.hintRow, state.hintCol,
                40, 40);

        GuessAndClock(state, solveGame);
    }

    // multiple solutions 100*70
    @Test
    public void CombinedTestMultiRec() throws IOException {
        GameState state = new GameState("D:/learn/2020_9/software/img/einstein.jpg",
                100, 70);

        SolveGame solveGame = new SolveGame(state.hintRow, state.hintCol,
                100, 70);

        GuessAndClock(state, solveGame);
    }

    // multiple solutions 100*100
    @Test
    public void CombinedTestMulti() throws IOException {
        GameState state = new GameState("D:/learn/2020_9/software/img/einstein.jpg",
                100, 100);

        SolveGame solveGame = new SolveGame(state.hintRow, state.hintCol,
                100, 100);

        GuessAndClock(state, solveGame);
    }

    // multiple solutions 200*100
    @Test
    public void CombinedTestMultiMid2() throws IOException {
        GameState state = new GameState("D:/learn/2020_9/software/img/einstein.jpg",
                200, 100);

        SolveGame solveGame = new SolveGame(state.hintRow, state.hintCol,
                200, 100);

        GuessAndClock(state, solveGame);
    }

    // multiple solutions 200*200
    @Test
    public void CombinedTestMultiLarge() throws IOException {
        GameState state = new GameState("D:/learn/2020_9/software/img/einstein.jpg",
                200, 200);

        SolveGame solveGame = new SolveGame(state.hintRow, state.hintCol,
                200, 200);

        GuessAndClock(state, solveGame);
    }

    private void SolveAndClock(GameState state, SolveGame solveGame) {
        long startTime = System.nanoTime();

        solveGame.SolvePipeline();
        if(solveGame.ErrorState()){
            System.out.println("Not valid game!");
            return;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime)/1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("solve completed in " + duration + "ms");

        assertArrayEquals(state.board, solveGame.board);
    }

    private void GuessAndClock(GameState state, SolveGame solveGame) {
        long startTime = System.nanoTime();

        solveGame.SolvePipeline();
        if(solveGame.ErrorState()){
            System.out.println("Not valid game!");
            return;
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime)/1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("solve completed in " + duration + "ms");

        solveGame.Guess();

        endTime = System.nanoTime();
        duration = (endTime - startTime)/1000000;  //divide by 1000000 to get milliseconds.
        System.out.println("guess completed in " + duration + "ms");

    }

}