package com.github.subh0m0y.matrix;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static org.testng.Assert.*;

public class BasicMatrixTest {
    private static final int ROWS = 100;
    private static final int COLS = 100;
    private static final double EPS = 1e-15;
    private static Random random;

    @BeforeMethod
    public void setUp() {
        random = new Random();
    }

    private static void changeElement(double[][] data, Random random) {
        data[random.nextInt(ROWS)][random.nextInt(COLS)] *= random.nextGaussian() * 2;
        data[random.nextInt(ROWS)][random.nextInt(COLS)] += random.nextGaussian() + 1;
    }

    @Test
    public void testEqualsAndHashCode() {
        double[][] data = new double[ROWS][COLS];
        Utilities.populate(data, random);
        Matrix matrix1 = new Matrix(data, false);
        Matrix matrix2 = new Matrix(matrix1);
        assertTrue(matrix1.equals(matrix2));
        assertEquals(matrix1.hashCode(), matrix2.hashCode());
        changeElement(data, random);
        assertFalse(matrix1.equals(matrix2));
        assertNotEquals(matrix1.hashCode(), matrix2.hashCode());
    }

    @Test
    public void testConstructors() {
        double[][] data = new double[ROWS][COLS];
        Matrix matrixZero1 = new Matrix(ROWS, COLS);
        Matrix matrixZero2 = new Matrix(data, true);

        assertTrue(matrixZero1.isZero());
        assertTrue(matrixZero2.isZero());
        assertEquals(matrixZero1, matrixZero2);

        Utilities.populate(data, random);

        Matrix matrix1 = new Matrix(data, true);
        Matrix matrix2 = new Matrix(data, false);
        assertEquals(matrix1, matrix2);

        changeElement(data, random);
        assertNotEquals(matrix1, matrix2);

        Matrix matrix3 = new Matrix(matrix1);
        assertEquals(matrix1, matrix3);
    }

    @Test
    public void testFromArray() {
        double[][] data = new double[ROWS][COLS];
        Utilities.populate(data, random);
        double[] linear = new double[ROWS * COLS];
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(data[i], 0, linear, i * COLS, COLS);
        }
        Matrix matrix1 = new Matrix(data, false);
        Matrix matrix2 = Matrix.fromArray(data);
        Matrix matrix3 = Matrix.fromLinearArray(ROWS, COLS, linear);
        assertEquals(matrix1, matrix2);
        assertEquals(matrix2, matrix3);
        changeElement(data, random);
        assertNotEquals(matrix1, matrix2);
        assertNotEquals(matrix1, matrix3);
    }

    @Test
    public void testGet() {
        double[][] data = new double[ROWS][COLS];
        Utilities.populate(data, random);

        Matrix matrix1 = new Matrix(data, false);
        Matrix matrix2 = new Matrix(matrix1);

        assertEquals(matrix1.getRows(), ROWS);
        assertEquals(matrix1.getCols(), COLS);
        assertEquals(matrix2.getRows(), ROWS);
        assertEquals(matrix2.getCols(), COLS);

        assertEquals(matrix1.getRows(), matrix2.getRows());
        assertEquals(matrix1.getCols(), matrix2.getCols());

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                assertEquals(matrix1.get(i, j), matrix2.get(i, j));
            }
        }
        assertThrows(() -> matrix1.get(-1, 0));
        assertThrows(() -> matrix2.get(0, COLS));
    }

    @Test
    public void testIsZero() {
        double[][] data = new double[ROWS][COLS];
        Matrix matrix1 = new Matrix(data, false);
        assertTrue(matrix1.isZero());
        changeElement(data, random);
        assertFalse(matrix1.isZero());
        Matrix matrix2 = Matrix.zero(ROWS, COLS);
        assertTrue(matrix2.isZero());
    }

    @Test
    public void testIsIdentity() {
        double[][] data = new double[ROWS][ROWS];
        Matrix matrix1 = new Matrix(data, false);
        assertFalse(matrix1.isIdentity(EPS));
        for (int i = 0; i < ROWS; i++) {
            data[i][i] = 1;
        }
        assertTrue(matrix1.isSquare());
        assertTrue(matrix1.isIdentity(EPS));
        changeElement(data, random);
        assertFalse(matrix1.isIdentity(EPS));

        Matrix matrix2 = Matrix.identity(ROWS);
        assertTrue(matrix2.isIdentity(EPS));
    }

    @Test
    public void testZeroFill() {
        double[][] data = new double[ROWS][COLS];
        double eps = random.nextDouble() * 0.01;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                data[i][j] = random.nextDouble() * eps;
            }
        }
        Matrix matrix = new Matrix(data, false);
        assertFalse(matrix.isZero());
        matrix.zeroFillBelow(eps);
        assertTrue(matrix.isZero());
    }

    @Test
    public void testTranspose() {
        double[][] data = new double[ROWS][COLS];
        Utilities.populate(data, random);
        Matrix matrix1 = new Matrix(data, false);
        Matrix matrix2 = matrix1.transpose();
        matrix1.transposeInPlace();
        assertEquals(matrix1, matrix2);
    }

    @Test
    public void testAppendingAndSplitting() throws Exception {
        Matrix matrix1 = Matrix.random(ROWS, COLS);
        Matrix matrix2 = Matrix.random(ROWS, COLS);
        Matrix matrixHorizontal = matrix1.appendRight(matrix2);
        Matrix matrixVertical = matrix1.appendBottom(matrix2);
        Matrix[] horizontal = matrixHorizontal.splitAtColumn(COLS);
        Matrix[] vertical = matrixVertical.splitAtRow(ROWS);
        assertEquals(matrix1, horizontal[0]);
        assertEquals(matrix2, horizontal[1]);
        assertEquals(matrix1, vertical[0]);
        assertEquals(matrix2, vertical[1]);
    }

    @Test
    public void testSwapAndGetRow() throws Exception {
        Matrix matrix1 = Matrix.random(ROWS, COLS);
        Matrix matrix2 = new Matrix(matrix1);
        int row1 = random.nextInt(ROWS);
        int row2 = random.nextInt(ROWS);
        matrix1.swapRowsInPlace(row1, row2);
        Matrix matrix3 = matrix2.swapRows(row1, row2);
        assertEquals(matrix1, matrix3);
        assertEquals(matrix1.getRow(row1), matrix2.getRow(row2));
        assertEquals(matrix1.getRow(row2), matrix2.getRow(row1));
    }
}