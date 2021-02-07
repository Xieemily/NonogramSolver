package com.nonogram;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Main {
    public static final List<List<Integer>> rowHint = new ArrayList<>();
    public static final List<List<Integer>> colHint = new ArrayList<>();
    public static int BOARD_SIZE_ROW;
    public static int BOARD_SIZE_COL;

    /**
     * Input hint from file or command line.
     * @param path specified hint file path, null if use cmd line
     * @throws FileNotFoundException input file not found
     */
    public static void InputHint(String path) throws FileNotFoundException {
        Scanner scanner;

        if(path == null){
            scanner = new Scanner(System.in);
            System.out.println("Enter board size:");
        } else {
            scanner = new Scanner(new File(path));
        }

        BOARD_SIZE_ROW = scanner.nextInt();
        BOARD_SIZE_COL = scanner.nextInt();
        scanner.nextLine();

        // hint split by space(or comma)
        // eg. hint of row1: 2 4
        if(path == null) {
            System.out.println("Enter row hint:");
        }

        InputHintList(scanner, BOARD_SIZE_ROW, rowHint);

        if(path == null) {
            System.out.println("Enter col hint:");
        }
        InputHintList(scanner, BOARD_SIZE_COL, colHint);

    }

    /**
     * Simplify row/column hint input process, called in method InputHint()
     * @param scanner scanner
     * @param boardSize hint list amount
     * @param rowHint list of list hint
     */
    private static void InputHintList(Scanner scanner, int boardSize, List<List<Integer>> rowHint) {
        for(int i = 0; i < boardSize; i++){
            String[] s = scanner.nextLine().split("[\\s|,]");
            List<Integer> numbers = new ArrayList<>();
            for(String c:s){
                numbers.add(Integer.parseInt(c));
            }
            rowHint.add(numbers);
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

    /**
     * Write generated hint to output file.
     * @param path output file
     * @param hintRow hint of rows
     * @param hintCol hint of columns
     */
    public static void WriteHint(String path, List<List<Integer>> hintRow,
                                 List<List<Integer>> hintCol){
        FileIO.CreateFile(path);
        FileIO.WriteToFile("Row hint:\n");
        WriteHintList(hintRow);
        FileIO.WriteToFile("Col hint:\n");
        WriteHintList(hintCol);
    }

    private static void WriteHintList(List<List<Integer>> hintList) {
        for(List<Integer> hint:hintList){
            if(hint.isEmpty()){
                FileIO.WriteToFile("0\n");
            } else {
                for(int i:hint){
                    FileIO.WriteToFile(i+" ");
                }
            }
            FileIO.WriteToFile("\n");
        }
    }

    public static void main(String[] args){
        // print usage
        if(args.length == 0 || args.length > 3){
            System.out.println("""
                    -s [input file] [output file] solve puzzle from hint input
                    -g [image file] [output file] generate hint from image
                    -t [input file] [output file] test game
                    \s""");
            return;
        }

        // -s and -t mode, solve puzzle by hint
        if(args[0].equals("-s") || args[0].equals("-t")){
            // input hint
            String path;
            if(args.length >= 2)path = args[1];
            else path = null;
            try{
                InputHint(path);
            } catch (FileNotFoundException e){
                System.out.println("File not found!" + e);
                return;
            }
            // solve game with deterministic solver and clock
            SolveGame solveGame = new SolveGame(rowHint, colHint, BOARD_SIZE_ROW, BOARD_SIZE_COL);
            long startTime = System.nanoTime();
            solveGame.SolvePipeline();
            long endTime = System.nanoTime();
            long duration = (endTime - startTime)/1000000;  //divide by 1000000 to get milliseconds.
            System.out.println("deterministic solve completed in " + duration + "ms");
            // error hint
            if(solveGame.ErrorState()){
                System.out.println("Error hint, not solvable");
                return;
            }
            // test mode, output and terminate
            if(args[0].equals("-t")) {
                solveGame.ShowBoard();
                if(args.length == 3){
                    // output code to file
                    FileIO.CreateFile(args[2]);
                    FileIO.WriteToFile(solveGame.GenerateString());
                }
                return;
            }
            // solve completely
            solveGame.Guess();

            if(args.length == 3){
                // output solution code to file
                FileIO.CreateFile(args[2]);
                for(int i = 0; i < solveGame.solution.size(); i++)
                    FileIO.WriteToFile("solution " + (i+1) + ":\n" +
                            solveGame.solution.get(i)+"\n");
            }
            System.out.println(solveGame.numSolution+" solutions found");
        } else {
            // -g mode
            if(args.length < 2){
                System.out.println("Please enter image path!");
                return;
            }
            // generate hint from image
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter board size:");
            BOARD_SIZE_ROW = scanner.nextInt();
            BOARD_SIZE_COL = scanner.nextInt();
            // get path
            String path = args[1];
            // generate game hint
            GameState gameState;
            try{
                gameState = new GameState(path, BOARD_SIZE_ROW, BOARD_SIZE_COL);
            } catch (IOException e){
                System.out.println("read image error" + e);
                return;
            }
            //output hint
            System.out.println("Row hint:");
            PrintHint(gameState.hintRow);
            System.out.println("Col hint:");
            PrintHint(gameState.hintCol);
            if(args.length == 3) {
                WriteHint(args[2], gameState.hintRow, gameState.hintCol);
            }
            // show board
             gameState.ShowBoard();
        }


    }
}
