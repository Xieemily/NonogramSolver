package com.nonogram;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GameState state = new GameState();
        state.GenerateGameFromImg("D:/learn/2020_9/software/nonogram2/src/com/nonogram/img/apple.jpg");
        state.ShowBoard();
        state.GenerateHint();
    }
}
