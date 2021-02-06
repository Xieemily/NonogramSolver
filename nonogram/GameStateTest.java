package com.nonogram;

import com.nonogram.GameState;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameStateTest {

    // unknown board
    @Test
    public void GenerateStringUnknownTest(){
        String str = "2222";
        GameState state = new GameState(2, 2);
        assertEquals(str, state.GenerateString());
    }

    // empty board
    @Test
    public void GenerateStringEmptyTest(){
        String str = "0000";
        GameState state = new GameState(2, 2);
        for(int i = 0; i < GameState.getBoardSizeRow(); i++){
            for(int j = 0; j < GameState.getBoardSizeCol(); j++){
                state.setBoard(GameState.CellState.EMPTY, i, j);
            }
        }
        assertEquals(str, state.GenerateString());
    }

    // mixed board
    @Test
    public void GenerateStringMixedTest(){
        String str = new String("200000100");
        GameState state = new GameState(3, 3);
        for(int i = 0; i < GameState.getBoardSizeRow(); i++){
            for(int j = 0; j < GameState.getBoardSizeCol(); j++){
                state.setBoard(GameState.CellState.EMPTY, i, j);
            }
        }
        state.setBoard(GameState.CellState.UNKNOWN, 0, 0);
        state.setBoard(GameState.CellState.FILLED, 2, 0);
        assertEquals(str, state.GenerateString());
    }

}