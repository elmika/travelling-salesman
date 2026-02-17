package com.elmika.tsp.application;

public class PermutationsIterator {
    private int n;
    private Integer[] current;
    private boolean hasNext;

    public PermutationsIterator(int n) {
        this.n = n;
        this.current = new Integer[n];
        for (int i = 0; i < n; i++) {
            current[i] = i + 1;
        }
        this.hasNext = true;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public Integer[] next() {
        if (!hasNext) {
            return null;
        }
        Integer[] result = current.clone();
        do {
            int k = current.length - 2;
            while (k >= 1 && current[k] >= current[k + 1]) {
                k--;
            }
            if (k < 1) {
                hasNext = false;
                return result;
            }
            int l = current.length - 1;
            while (l >= 0 && current[k] >= current[l]) {
                l--;
            }
            Integer temp = current[k];
            current[k] = current[l];
            current[l] = temp;
            int left = k + 1;
            int right = current.length - 1;
            while (left < right) {
                temp = current[left];
                current[left] = current[right];
                current[right] = temp;
                left++;
                right--;
            }
        } while (current[0] != 1);
        return result;
    }
}
