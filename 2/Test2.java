package com;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dmitriy on 25.11.2015.
 */
public class Test2 {
    public static void main(String[] args) {

        String[] a = {"acb", "abc", "bca"};
        String[] t = {"abc","acb","bca"};

        System.out.println(Arrays.toString(lexicographicalSort(a, "abc")));
        assert Arrays.equals(t,lexicographicalSort(a,"abc"));

        String[] t2 = {"bca", "acb", "abc"};
        System.out.println(Arrays.toString(lexicographicalSort(a, "cba")));
        assert Arrays.equals(t2,lexicographicalSort(a, "cba"));

        String[] c = {"aaa","aa",""};
        String[] t3 = {"", "aa", "aaa"};
        System.out.println(Arrays.toString(lexicographicalSort(c, "a")));
        assert Arrays.equals(t3,lexicographicalSort(c, "a"));
    }

    public static String[] lexicographicalSort(String[] arr, String order) {
        if (arr == null || arr.length == 0 || order == null || order.trim().isEmpty()) return arr;
        HashMap<Character, Integer> weight = new HashMap<Character, Integer>();

        // calc the weight of every char using order
        for (int i = 0; i < order.length(); i++) {
            Character c = order.charAt(i);
            if (weight.containsKey(c)) throw new IllegalArgumentException("Incorrect order");
            weight.put(c,i);
        }

        Arrays.sort(arr, new StringComparator(weight));
        return arr;
    }

    private static final class StringComparator implements Comparator<String> {
        private Map<Character,Integer> weight; // weight of every char

        // cash to store the weights of the strings calculated before
        // store it to improve performance
        private HashMap<String, int[]> w = new HashMap<String, int[]>();

        private StringComparator(Map<Character, Integer> weight) {
            this.weight = weight;
        }

        @Override
        public int compare(String o1, String o2) {
            return compareStrings(getStringWeight(o1),getStringWeight(o2));
        }

        private int compareStrings(int[] w1, int[] w2) {
            for (int i = 0; i < w1.length; i++) { // iterate over every char (it's weight) in the string
                if (w2.length <= i) return 1; // if nothing to compare as there is no more chars in the second string
                if (w1[i] == w2[i]) continue; // chars equal - continue
                if (w1[i] < w2[i]) return -1; // chars are different. compare and return the result
                return 1;
            }
            if (w2.length > w1.length) return -1; // second string is longer but first parts are the same
            return 0;
        }

        // just return the array of weights
        private int[] getStringWeight(String s) {
            if (w.containsKey(s)) return w.get(s);
            int[] sWeight = new int[s.length()];
            for (int i = 0; i < s.length(); i++) {
                sWeight[i] = weight.get(s.charAt(i));
            }
            w.put(s,sWeight);
            return sWeight;
        }
    }

}
