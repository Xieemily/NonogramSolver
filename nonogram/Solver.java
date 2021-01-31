package com.nonogram;

import java.util.*;

public class Solver {
    private GameState state;

    Solver(GameState _state){
        state = _state;
    }


    private void ForwardPass(){

    }

    /**
     * Generate labeling given hint of one line, in order to get map.
     * Use the same negative number for empty cells(since its length is changeable),
     * continuous number for filled cells, this make labeling for filled cells fixed
     * for example: hint[3,4], assume 10 cells each line
     * labeling[-1, -1, 2, 3, 4, -5, -5, 6, 7, 8, 9, -10, -10]
     *
     * @param hint
     *            hint list of one line
     * @return labeling
     *
     */
    public static ArrayList<Integer> GenerateLabeling(ArrayList<Integer> hint){
        ArrayList<Integer> labeling = new ArrayList<Integer>();
        int n = 1; // count
        for (Integer i : hint) {
            labeling.add(-n);
            labeling.add(-n); // negative value twice because -1 can appear after -1
            for(int k = 0; k < i; k++){
                labeling.add(n+k+1);
            }
            n += i+1;
        }
        labeling.add(-n);
        labeling.add(-n);
        return labeling;
    }

    /**
     * Generate a map of labeling, shift labeling by 1 or -1.
     * For example:
     * [-1, -1, 2, 3, 4, -5, -5, 6, 7, -8, -8] can be shifted by 1 to get a forward map
     * [-1, 2,  3, 4, -5, -5, 6, 7, -8, -8]
     * map: -1->-1, -1->2, 2->3, 3->4, ... -5->(-5,6), ...
     *
     * @param labeling
     *          hint list of one line
     * @param shift
     *          size of shift. Requires [-1, 1]
     * @return map
     *          a map indicate what number can appear after/before cell n
     *
     */
    public static Map<Integer, Set<Integer>> GenerateMap(ArrayList<Integer> labeling, int shift){
        Map<Integer, Set<Integer>> map = new HashMap<Integer, Set<Integer>>();
        for(int i = 0; i < labeling.size(); i++){
            int tmp_label = labeling.get(i);
            int nxt_label; // shifted labeling
            // get shifted label if exists
            try{
                nxt_label = labeling.get(i+shift);
            } catch (IndexOutOfBoundsException e){
                continue; // no corresponding label, ignore
            }
            // add shifted label to current label's map set
            if(map.containsKey(tmp_label)){ // add to set
                map.get(tmp_label).add(nxt_label);
            } else { // initialize a set
                Set<Integer> set = new HashSet<>();
                set.add(nxt_label);
                map.put(tmp_label, set);
            }
        } // end for
        return map;
    }

    /**
     * Get possible label sets of one line, consider left/right most situation and take labels in between
     *
     * @param labeling
     *          labeling list of one line
     * @param hint
     *          hint list of one line
     * @return map
     *          a map indicate what number can appear in each cell
     *
     */
    public static ArrayList<Set<Integer>> InitLine(ArrayList<Integer> hint, ArrayList<Integer> labeling){
        ArrayList<Set<Integer>> lineSet = new ArrayList<Set<Integer>>();
        // calculate minimum length of block
        int sumBlock = 0;
        for(int i:hint)sumBlock += i;
        sumBlock += hint.size()<=1 ? 0:hint.size()-1;
        // moves needed to get to right most position
        int move = GameState.getBoardSize() - sumBlock;
        // cell label in between
        for(int cell = 0; cell < GameState.getBoardSize(); cell++){
            Set<Integer> cellSet = new HashSet<>();
            for(int i = 0; i <= move; i++){ // get value in between
                // get label of shifted right most position
                int label;
                try {
                    label = labeling.get(2 + cell - i); // plus 2 to get to left most position [-1, -1, *2* ...]
                } catch (IndexOutOfBoundsException e){
                    if(2 + cell -i < 0) { // first group of blank, label -1
                        label = -1;
                    } else { // last group of blank, last label
                        label = labeling.get(labeling.size() - 1);
                    }
                }
                // add to set of current cell
                cellSet.add(label);
            }
            lineSet.add(cellSet);
        }

        return lineSet;
    }

    /**
     * Deduct cells with only positive/negative label
     *
     * @param labeling
     *          labeling list of one line
     * @param hint
     *          hint list of one line
     * @return map
     *          a map indicate what number can appear in each cell
     *
     */
    public static void Deduction(int no, ArrayList<Set<Integer>> setLine){

    }


}
