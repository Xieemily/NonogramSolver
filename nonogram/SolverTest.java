package com.nonogram;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SolverTest {
    Solver solver= new Solver();

    // covers hint = [1]
    @Test
    public void TestLabeling1(){

        ArrayList<Integer> hint = new ArrayList<>();
        hint.add(1);
        System.out.println(solver.GenerateLabeling(hint));
    }

    // covers hint = [2, 1]
    @Test
    public void TestLabeling(){

        ArrayList<Integer> hint = new ArrayList<>();
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(-1);arr.add(-1);arr.add(2);arr.add(3);arr.add(-4);arr.add(-4);arr.add(5);arr.add(-6);arr.add(-6);
        hint.add(2);hint.add(1);
        assertEquals(arr, solver.GenerateLabeling(hint), "Labeling");
    }

    // covers shift = -1
    @Test
    public void TestGenerateMapPrevious(){
        ArrayList<Integer> hint = new ArrayList<>();
        hint.add(3);
        hint.add(4);
        System.out.println(solver.GenerateMap(hint, -1));
    }

    // covers shift = 1
    @Test
    public void TestGenerateMapFront(){
        ArrayList<Integer> labeling = new ArrayList<>();
        labeling.add(-1);labeling.add(-1);labeling.add(2);labeling.add(3);labeling.add(4);labeling.add(-5);
        labeling.add(-5);labeling.add(6);labeling.add(7);labeling.add(8);labeling.add(-9);labeling.add(-9);
        System.out.println(solver.GenerateMap(labeling, 1));
    }

    @Test
    public void TestInitLine(){
        ArrayList<Integer> hint = new ArrayList<>();
        hint.add(2); hint.add(7);
        System.out.println(solver.InitLine(hint));
    }

    // covers hint = []
    @Test
    public void TestInitLineEmpty(){
        ArrayList<Integer> hint = new ArrayList<>();
        System.out.println(solver.InitLine(hint));
    }

    // covers hint = [1]
    @Test
    public void TestInitLineOne(){
        ArrayList<Integer> hint = new ArrayList<>();
        hint.add(1);
        System.out.println(solver.InitLine(hint));
    }

    // covers hint = [10]
    @Test
    public void TestInitLineFull(){
        ArrayList<Integer> hint = new ArrayList<>();
        hint.add(10);
        System.out.println(solver.InitLine(hint));
    }

    // covers hint = [1]
    @Test
    void forwardPassOne() {
        ArrayList<Integer> hint = new ArrayList<>();
        hint.add(1);
        ArrayList<Integer> labeling = solver.GenerateLabeling(hint);
        System.out.println(labeling);
        ArrayList<Set<Integer>> lineSet = solver.InitLine(hint);
        System.out.println(lineSet);
        Map<Integer, Set<Integer>> map = solver.GenerateMap(hint, 1);
        System.out.println(map);
        lineSet.get(0).remove(-1);
        ArrayList<Set<Integer>> lineSetPassed = solver.ForwardPass(lineSet, map);
        System.out.println(lineSetPassed);
    }

    @Test
    void forwardPass() {
        ArrayList<Integer> hint = new ArrayList<>();
        hint.add(3);
        hint.add(4);
        ArrayList<Integer> labeling = solver.GenerateLabeling(hint);
        ArrayList<Set<Integer>> lineSet = solver.InitLine(hint);
        System.out.println(lineSet);
        Map<Integer, Set<Integer>> map = solver.GenerateMap(labeling, 1);
        System.out.println(map);
        lineSet.get(0).remove(2);
        ArrayList<Set<Integer>> lineSetPassed = solver.ForwardPass(lineSet, map);
        System.out.println(lineSetPassed);
    }

    @Test
    void backwardPass() {
        ArrayList<Integer> hint = new ArrayList<>();
        hint.add(3);
        hint.add(4);
        ArrayList<Integer> labeling = solver.GenerateLabeling(hint);
        ArrayList<Set<Integer>> lineSet = solver.InitLine(hint);
        System.out.println(lineSet);
        Map<Integer, Set<Integer>> map = solver.GenerateMap(labeling, -1);
        System.out.println(map);
        lineSet.get(9).remove(-10);
        ArrayList<Set<Integer>> lineSetPassed = solver.BackwardPass(lineSet, map);
        System.out.println(lineSetPassed);
    }
}