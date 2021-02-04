package com.nonogram;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SolverTest {
    GameState state = new GameState(10);
    private final Solver solver= new Solver();

    // covers hint = [1]
    @Test
    void TestLabeling1(){
        List<Integer> hint = new ArrayList<>();
        hint = Arrays.asList(1);
        List<Integer> labeling = new ArrayList<>();
        labeling = Arrays.asList(-1,-1,2,-3,-3);
        assertEquals(labeling, solver.GenerateLabeling(hint));
    }

    // covers hint = [2, 1]
    @Test
    void TestLabeling(){

        List<Integer> hint = new ArrayList<>();
        List<Integer> arr = new ArrayList<>();
        hint = Arrays.asList(2, 1);
        arr = Arrays.asList(-1, -1, 2, 3, -4, -4, 5, -6, -6);
        assertEquals(arr, solver.GenerateLabeling(hint), "Labeling");
    }

    // covers shift = -1
    // map {-1=[-1], 2=[-1], 3=[2], 4=[3], -5=[4, -5], 6=[-5], 7=[6], 8=[7], 9=[8], -10=[9, -10]}
    @Test
    void TestGenerateMapPrevious(){
        List<Integer> hint = new ArrayList<>();
        hint = Arrays.asList(3, 4);
        System.out.println(solver.GenerateMap(hint, -1));
    }

    // covers shift = 1
    // map {-1=[-1, 2], 2=[3], 3=[4], 4=[-5], -5=[-5, 6], 6=[7], 7=[8], 8=[9], 9=[-10], -10=[-10]}
    @Test
    void TestGenerateMapFront(){
        List<Integer> hint = new ArrayList<>();
        hint = Arrays.asList(3, 4);
        System.out.println(solver.GenerateMap(hint, 1));
    }

    // hint = [2, 7]
    @Test
    void TestInitLine(){
        ArrayList<Integer> hint = new ArrayList<>();
        hint.add(2); hint.add(7);
        System.out.println(solver.InitLine(hint));
    }

    // covers hint = []
    @Test
    void TestInitLineEmpty(){
        ArrayList<Integer> hint = new ArrayList<>();
        System.out.println(solver.InitLine(hint));
    }

    // covers hint = [1]
    @Test
    void TestInitLineOne(){
        ArrayList<Integer> hint = new ArrayList<>();
        hint.add(1);
        System.out.println(solver.InitLine(hint));
    }

    // covers hint = [10]
    @Test
    void TestInitLineFull(){
        ArrayList<Integer> hint = new ArrayList<>();
        hint.add(10);
        System.out.println(solver.InitLine(hint));
    }

    // covers hint = [1]
    @Test
    void forwardPassOne() {
        List<Integer> hint = new ArrayList<>();
        hint = Arrays.asList(1);
        List<Integer> labeling = solver.GenerateLabeling(hint);
        System.out.println(labeling);
        List<Set<Integer>> lineSet = solver.InitLine(hint);
        System.out.println(lineSet);
        Map<Integer, Set<Integer>> map = solver.GenerateMap(hint, 1);
        System.out.println(map);
        lineSet.get(0).remove(-1);
        List<Set<Integer>> lineSetPassed = solver.ForwardPass(lineSet, map);
        System.out.println(lineSetPassed);
    }

    @Test
    void forwardPass() {
        List<Integer> hint = new ArrayList<>();
        hint.add(3);
        hint.add(4);
        List<Integer> labeling = solver.GenerateLabeling(hint);
        List<Set<Integer>> lineSet = solver.InitLine(hint);
        System.out.println(lineSet);
        Map<Integer, Set<Integer>> map = solver.GenerateMap(labeling, 1);
        System.out.println(map);
        lineSet.get(0).remove(2);
        List<Set<Integer>> lineSetPassed = solver.ForwardPass(lineSet, map);
        System.out.println(lineSetPassed);
    }

    @Test
    void backwardPass() {
        List<Integer> hint = new ArrayList<>();
        hint.add(3);
        hint.add(4);
        List<Integer> labeling = solver.GenerateLabeling(hint);
        List<Set<Integer>> lineSet = solver.InitLine(hint);
        System.out.println(lineSet);
        Map<Integer, Set<Integer>> map = solver.GenerateMap(labeling, -1);
        System.out.println(map);
        lineSet.get(9).remove(-10);
        List<Set<Integer>> lineSetPassed = solver.BackwardPass(lineSet, map);
        System.out.println(lineSetPassed);
    }
}