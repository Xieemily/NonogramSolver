package com.nonogram;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void inputHint() {
    }

    @Test
    void main() {
        String[] args = new String[1];
        Main.main(args);
    }

    @Test
    void mainSolve() {
        String[] args = new String[2];
        args[1] = "-s";
        Main.main(args);
    }

}