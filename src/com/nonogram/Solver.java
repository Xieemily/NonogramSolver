package com.nonogram;

import java.util.*;

/**
 * Process needed to solve the game.
 */
class Solver {
    /**
     * Generate labeling given hint of one line, in order to get map.
     * Use the same negative number for empty cells(since its length is changeable),
     * continuous number for filled cells, this make labeling for filled cells fixed
     * for example: hint[3,4], assume 10 cells each line
     * labeling[-1, -1, 2, 3, 4, -5, -5, 6, 7, 8, 9, -10, -10]
     *
     * @param hint hint list of one line
     * @return labeling
     */
    public List<Integer> GenerateLabeling(List<Integer> hint) {
        List<Integer> labeling = new ArrayList<>();
        // empty line
        if (hint.isEmpty()) {
            labeling.add(-1);
            labeling.add(-1);
            return labeling;
        }
        // normal line
        int n = 1; // count
        for (Integer i : hint) {
            labeling.add(-n);
            labeling.add(-n); // negative value twice because -1 can appear after -1
            for (int k = 0; k < i; k++) {
                labeling.add(n + k + 1);
            }
            n += i + 1;
        }
        labeling.add(-n);
        labeling.add(-n);
        return labeling;
    }

    /**
     * Generate labeling given hint of one line, in order to get map.
     * Use the same negative number for empty cells(since its length is changeable),
     * continuous number for filled cells, this make labeling for filled cells fixed.
     * For example: hint[3,4], assume 10 cells each line
     * labeling[-1, -1, 2, 3, 4, -5, -5, 6, 7, 8, 9, -10, -10]
     *
     * @param hint hint list of one line
     * @return labeling
     */
    private List<Integer> GenerateSingleLabeling(List<Integer> hint) {
        List<Integer> labeling = new ArrayList<>();
        // empty line
        if (hint.isEmpty()) {
            labeling.add(-1);
            return labeling;
        }
        // normal line
        int n = 1; // count
        for (Integer i : hint) {
            labeling.add(-n); // negative value twice because -1 can appear after -1
            for (int k = 0; k < i; k++) {
                labeling.add(n + k + 1);
            }
            n += i + 1;
        }
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
     * @param hint  hint list of one line
     * @param shift size of shift. Requires [-1, 1]
     * @return map
     * a map indicate what number can appear after/before cell n
     */
    public Map<Integer, Set<Integer>> GenerateMap(List<Integer> hint, int shift) {
        List<Integer> labeling = GenerateLabeling(hint);
        Map<Integer, Set<Integer>> map = new HashMap<>();
        for (int i = 0; i < labeling.size(); i++) {
            int tmp_label = labeling.get(i);
            int nxt_label; // shifted labeling
            // get shifted label if exists
            try {
                nxt_label = labeling.get(i + shift);
            } catch (IndexOutOfBoundsException e) {
                continue; // no corresponding label, ignore
            }
            // add shifted label to current label's map set
            if (map.containsKey(tmp_label)) { // add to set
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
     * @param hint hint list of one line
     * @return lineSet
     * a list of sets, each set contains the labels that could appear in one cell
     */
    public List<Set<Integer>> InitLine(List<Integer> hint, Boolean isRow) {
        List<Integer> labeling = GenerateSingleLabeling(hint);
        List<Set<Integer>> lineSet = new ArrayList<>();
        // calculate length of block
        int sumBlock = 0;
        // board size
        int boardLen;
        if(isRow){
                boardLen = GameState.getBoardSizeCol();
        } else {
            boardLen = GameState.getBoardSizeRow();
        }
        for (int i : hint) sumBlock += i;
        // moves needed to get to right most position
        int move = boardLen - sumBlock - hint.size() + 1;
        // cell label in between
        for (int cell = 0; cell < boardLen; cell++) {
            Set<Integer> cellSet = new HashSet<>();
            for (int i = 0; i <= move; i++) { // get value in between
                // get label of shifted right most position
                int label;
                try {
                    label = labeling.get(1 + cell - i); // plus 1 to get to left most position [-1, *2* ...]
                } catch (IndexOutOfBoundsException e) {
                    if (1 + cell - i < 0) { // first group of blank, label -1
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
     * Forward pass one line, get possible next cell's labels by mapping current cell's labels, intersect them
     *
     * @param lineSet list of sets in one line
     * @param map     map of labels of this line
     * @return lineSet
     * list of sets after forward pass, each set contains the labels that could appear in one cell
     */
    public List<Set<Integer>> ForwardPass(List<Set<Integer>> lineSet, Map<Integer, Set<Integer>> map) {
        Set<Integer> available = new HashSet<>();
        for (Set<Integer> set : lineSet) {
            if (!available.isEmpty()) { // not first one
                set.retainAll(available); // intersect with available label
                available.clear();
            }
            for (int i : set) {
                available.addAll(map.get(i)); // available labels of next cell
            }
        }
        return lineSet;
    }

    /**
     * Backward pass one line, get possible previous cell's labels by mapping(-1 shift map)
     * current cell's labels, intersect them
     *
     * @param lineSet list of sets in one line
     * @param map     map of labels of this line
     * @return lineSet
     * list of sets after forward pass, each set contains the labels that could appear in one cell
     */
    public List<Set<Integer>> BackwardPass(List<Set<Integer>> lineSet, Map<Integer, Set<Integer>> map) {
        Collections.reverse(lineSet); // reverse line
        ForwardPass(lineSet, map);
        Collections.reverse(lineSet);
        return lineSet;
    }
}
