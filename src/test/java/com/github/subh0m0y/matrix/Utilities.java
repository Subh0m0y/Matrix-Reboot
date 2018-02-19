package com.github.subh0m0y.matrix;

import java.util.Random;

public class Utilities {
    public static void populate(double[][] data, Random random) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = random.nextGaussian();
            }
        }
    }
}
