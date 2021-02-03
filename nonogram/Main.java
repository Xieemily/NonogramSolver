package com.nonogram;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

class Main {
    public static void main(String[] args) throws IOException {
        GameState state = new GameState("D:/learn/2020_9/software/img/multi2.jpg", 4);
        state.ShowBoard();
        System.out.println("hintRow:"+state.hintRow);
        System.out.println("hintCol:"+state.hintCol);
        SolveGame solveGame = new SolveGame(state.hintRow, state.hintCol, 4);


        long startTime = System.nanoTime();
        solveGame.SolvePipeline();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime)/1000000;  //divide by 1000000 to get milliseconds.

        solveGame.ShowBoard();
        solveGame.Guess();

        for(String str: solveGame.solution){
            System.out.println(str);
        }
        System.out.println("solve completed in " + duration + "ms");
    }
}
