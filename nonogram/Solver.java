package com.nonogram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Solver {
    private GameState state;

    Solver(GameState _state){
        state = _state;
    }

    enum CellState{
        FILLED,
        EMPTY,
        UNDEFINED,
        ERROR
    }

    private void ForwardPass(){

    }

    private Map<Integer, ArrayList<Integer>> GenerateMap(ArrayList<Integer> hint){
        Map<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
        for (Iterator<Integer> it = hint.iterator(); it.hasNext(); ) {
            int i = it.next();


        }
        return map;
    }

    private ArrayList<ArrayList<Integer>> SolveRow(int[] row, Map<Integer, ArrayList<Integer>> map){
        ArrayList<ArrayList<Integer>> solution = new ArrayList<ArrayList<Integer>>();

        return solution;
    }
}
