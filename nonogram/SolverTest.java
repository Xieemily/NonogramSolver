package com.nonogram;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SolverTest {


    // covers hint = [2, 1]
    @Test
    public void TestLabeling(){
        ArrayList<Integer> hint = new ArrayList<>();
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(-1);arr.add(-1);arr.add(2);arr.add(3);arr.add(-4);arr.add(-4);arr.add(5);arr.add(-6);arr.add(-6);
        hint.add(2);hint.add(1);
        assertEquals(arr, Solver.GenerateLabeling(hint), "Labeling");
    }

    // covers shift = -1
    @Test
    public void TestGenerateMapPrevious(){
        ArrayList<Integer> labeling = new ArrayList<>();
        labeling.add(-1);labeling.add(-1);labeling.add(2);labeling.add(3);labeling.add(4);labeling.add(-5);
        labeling.add(-5);labeling.add(6);labeling.add(7);labeling.add(8);labeling.add(-9);labeling.add(-9);
        System.out.println(Solver.GenerateMap(labeling, -1));
    }

    // covers shift = 1
    @Test
    public void TestGenerateMapFront(){
        ArrayList<Integer> labeling = new ArrayList<>();
        labeling.add(-1);labeling.add(-1);labeling.add(2);labeling.add(3);labeling.add(4);labeling.add(-5);
        labeling.add(-5);labeling.add(6);labeling.add(7);labeling.add(8);labeling.add(-9);labeling.add(-9);
        System.out.println(Solver.GenerateMap(labeling, 1));
    }

    @Test
    public void TestInitLine(){
        ArrayList<Integer> hint = new ArrayList<>();
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(-1);arr.add(-1);arr.add(2);arr.add(3);arr.add(4);arr.add(5);arr.add(-6);arr.add(-6);
        arr.add(7);arr.add(8);arr.add(9);arr.add(-10);arr.add(-10);
        hint.add(4); hint.add(3);
        System.out.println(Solver.InitLine(hint, arr));
    }
}