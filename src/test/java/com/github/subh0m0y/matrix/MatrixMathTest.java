package com.github.subh0m0y.matrix;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static org.testng.Assert.*;

public class MatrixMathTest {

    private static final int ROWS = 100;
    private static final int COLS = 100;
    private static final int POWER_BOUND = 1000;
    private static final int COUNT = 10;
    private static final double EPS = 1e-15;

    private static Random random;

    @BeforeMethod
    public void setUp() {
        random = new Random();
    }

    @Test
    public void testAddition() {
        double[][] data1 = new double[ROWS][COLS];
        double[][] data2 = new double[ROWS][COLS];
        Utilities.populate(data1, random);
        Utilities.populate(data2, random);
        Matrix matrix1 = new Matrix(data1, false);
        Matrix matrix2 = new Matrix(data1, false);
        Matrix matrix3 = matrix1.add(matrix2);
        matrix1.addInPlace(matrix2);
        assertEquals(matrix1, matrix3);
    }

    @Test
    public void testSubtraction() {
        double[][] data1 = new double[ROWS][COLS];
        double[][] data2 = new double[ROWS][COLS];
        Utilities.populate(data1, random);
        Utilities.populate(data2, random);
        Matrix matrix1 = new Matrix(data1, false);
        Matrix matrix2 = new Matrix(data1, false);
        Matrix matrix3 = matrix1.subtract(matrix2);
        matrix1.subtractInPlace(matrix2);
        assertEquals(matrix1, matrix3);
    }

    @Test
    public void testElementMultiply() {
        double[][] data1 = new double[ROWS][COLS];
        double[][] data2 = new double[ROWS][COLS];
        Utilities.populate(data1, random);
        Utilities.populate(data2, random);
        Matrix matrix1 = new Matrix(data1, false);
        Matrix matrix2 = new Matrix(data1, false);
        Matrix matrix3 = matrix1.elementMultiply(matrix2);
        matrix1.elementMultiplyInPlace(matrix2);
        assertEquals(matrix1, matrix3);
    }

    @Test
    public void testElementDivide() {
        double[][] data1 = new double[ROWS][COLS];
        double[][] data2 = new double[ROWS][COLS];
        Utilities.populate(data1, random);
        Utilities.populate(data2, random);
        Matrix matrix1 = new Matrix(data1, false);
        Matrix matrix2 = new Matrix(data1, false);
        Matrix matrix3 = matrix1.elementDivide(matrix2);
        matrix1.elementDivideInPlace(matrix2);
        assertEquals(matrix1, matrix3);
    }

    @Test
    public void testMultiplication() {
        double[][] data1 = new double[ROWS][ROWS];
        double[][] data2 = new double[ROWS][ROWS];
        Utilities.populate(data1, random);
        Utilities.populate(data2, random);
        Matrix matrix1 = new Matrix(data1, false);
        Matrix matrix2 = new Matrix(data1, false);
        Matrix matrix3 = matrix1.multiply(matrix2);
        matrix1.multiplyInPlace(matrix2);
        assertEquals(matrix1, matrix3);
    }

    @Test
    public void testScale() {
        double[][] data1 = new double[ROWS][ROWS];
        double scale = random.nextDouble();
        Utilities.populate(data1, random);
        Matrix matrix1 = new Matrix(data1, false);
        Matrix matrix2 = matrix1.scale(scale);
        matrix1.scaleInPlace(scale);
        assertEquals(matrix1, matrix2);
    }

    @Test
    public void testExponentiation() {
        double[][] data = new double[ROWS][ROWS];
        Utilities.populate(data, random);
        int power = POWER_BOUND;

        Matrix matrix1 = new Matrix(data, false);
        Matrix matrix2 = new Matrix(data, true);
        Matrix product = Matrix.identity(ROWS);
        for (int i = 0; i < power; i++) {
            product.multiplyInPlace(matrix1);
        }
        matrix2 = matrix2.exponentiate(power);
        assertEquals(product, matrix2);
    }

    @Test
    public void testOrthogonal() {
        for (int i = 0; i < COUNT; i++) {
            Matrix matrix = Matrix.identity(ROWS);
            assertTrue(matrix.isOrthogonal(EPS));
            double theta = random.nextDouble() * Math.PI * 2;
            double cos = Math.cos(theta);
            double sin = Math.sin(theta);
            matrix = Matrix.fromLinearArray(2, 2, cos, -sin, sin, cos);
            assertTrue(matrix.isOrthogonal(EPS));
            matrix = Matrix.fromLinearArray(2, 2, cos, sin, sin, -cos);
            assertTrue(matrix.isOrthogonal(EPS));
        }
    }
}