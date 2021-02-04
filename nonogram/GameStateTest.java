package com.nonogram;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameStateTest {

    // unknown board
    @Test
    void GenerateStringUnknownTest(){
        String str = "2222";
        GameState state = new GameState(2);
        assertEquals(str, state.GenerateString());
    }

    // empty board
    @Test
    void GenerateStringEmptyTest(){
        String str = "0000";
        GameState state = new GameState(2);
        for(int i = 0; i < state.getBoardSize(); i++){
            for(int j = 0; j < state.getBoardSize(); j++){
                state.setBoard(GameState.CellState.EMPTY, i, j);
            }
        }
        assertEquals(str, state.GenerateString());
    }

    // mixed board
    @Test
    void GenerateStringMixedTest(){
        String str = new String("200000100");
        GameState state = new GameState(3);
        for(int i = 0; i < state.getBoardSize(); i++){
            for(int j = 0; j < state.getBoardSize(); j++){
                state.setBoard(GameState.CellState.EMPTY, i, j);
            }
        }
        state.setBoard(GameState.CellState.UNKNOWN, 0, 0);
        state.setBoard(GameState.CellState.FILLED, 2, 0);
        assertEquals(str, state.GenerateString());
    }

}