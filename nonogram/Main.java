package com.nonogram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Main {
    public static List<List<Integer>> rowHint = new ArrayList<>();
    public static List<List<Integer>> colHint = new ArrayList<>();
    public static int BOARD_SIZE;

    public static void InputHint(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter board size:");
        BOARD_SIZE = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter row hint:");
        for(int i = 0; i < BOARD_SIZE; i++){
            String[] s = scanner.nextLine().split(" ");
            List<Integer> numbers = new ArrayList<>();
            for(String c:s){
                numbers.add(Integer.parseInt(c));
            }
            rowHint.add(numbers);
        }

        System.out.println("Enter col hint:");
        for(int i = 0; i < BOARD_SIZE; i++){
            String[] s = scanner.nextLine().split(" ");
            List<Integer> numbers = new ArrayList<>();
            for(String c:s){
                numbers.add(Integer.parseInt(c));
            }
            colHint.add(numbers);
        }

    }

    public static void PrintHint(List<List<Integer>> hint){
        for(List<Integer> list:hint){
            if(list.isEmpty()){
                System.out.print("0");
            } else {
                for(int i:list){
                    System.out.print(i+" ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args){
        // print usage
        if(args.length == 0 || args.length > 2){
            System.out.println("-s solve puzzle\n" +
                    "-g generate puzzle from image\n ");
        } else {
            // solve puzzle by hint
            if(args[0].equals("-s") || args[0].equals("-t")){
                // input hint
                InputHint();
                SolveGame solveGame = new SolveGame(rowHint, colHint, BOARD_SIZE);
                solveGame.SolvePipeline();
                if(solveGame.ErrorState()){
                    System.out.println("Error hint, not solvable");
                    return;
                } else {
                    solveGame.Guess();
                    System.out.println(solveGame.numSolution+"solutions found");
                }
            } else {
                // generate hint from image
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter board size:");
                BOARD_SIZE = scanner.nextInt();
                // get path
                scanner.nextLine();
                String path = scanner.nextLine();
                // generate game hint
                GameState gameState;
                try{
                    gameState = new GameState(path, BOARD_SIZE);
                } catch (IOException e){
                    System.out.println("read image error" + e);
                    return;
                }
                //output hint
                System.out.println("Row hint:");
                PrintHint(gameState.hintRow);
                System.out.println("Col hint:");
                PrintHint(gameState.hintCol);
                // show board
                gameState.ShowBoard();
            }
        }

    }
}
