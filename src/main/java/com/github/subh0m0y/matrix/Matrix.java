package com.github.subh0m0y.matrix;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

@SuppressWarnings("WeakerAccess")
public class Matrix {
    private final int rows;
    private final int cols;
    private final double[][] data;

    /**
     * Creates a new matrix with the given data
     *
     * @param data         The raw data to encapsulate.
     * @param makeDeepCopy Whether to copy every element or use the given reference.
     */
    Matrix(final double[][] data, final boolean makeDeepCopy) {
        this.rows = data.length;
        this.cols = this.rows == 0 ? 0 : data[0].length;

        if (makeDeepCopy) {
            this.data = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                System.arraycopy(data[i], 0, this.data[i], 0, cols);
            }
        } else {
            this.data = data;
        }
    }

    /**
     * Creates a new zero matrix of order rows x cols.
     *
     * @param rows The number of rows required.
     * @param cols The number of columns required.
     */
    Matrix(final int rows, final int cols) {
        if (rows < 0) {
            throw new IllegalArgumentException("Invalid number of rows : " + rows);
        }
        if (cols < 0) {
            throw new IllegalArgumentException("Invalid number of columns : " + cols);
        }
        this.rows = rows;
        this.cols = cols;
        data = new double[rows][cols];
    }

    /**
     * Creates a new Matrix that is a copy of the given
     * matrix and is independent of the original.
     *
     * @param matrix The matrix to copy.
     */
    public Matrix(final Matrix matrix) {
        rows = matrix.rows;
        cols = matrix.cols;
        data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(matrix.data[i], 0, data[i], 0, cols);
        }
    }

    public static Matrix fromArray(final double[][] data) {
        return new Matrix(data, true);
    }

    public static Matrix zero(final int rows, final int cols) {
        return new Matrix(rows, cols);
    }

    public static Matrix identity(final int order) {
        double[][] data = new double[order][order];
        for (int i = 0; i < order; i++) {
            data[i][i] = 1;
        }
        return new Matrix(data, false);
    }

    public static Matrix fromLinearArray(final int rows, final int cols,
                                         final double... elements) {
        if (elements.length != rows * cols) {
            throw new IllegalArgumentException("Invalid number of elements: " + elements.length
                    + " Expected: " + rows * cols);
        }
        double[][] data = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(elements, i * cols, data[i], 0, cols);
        }
        return new Matrix(data, false);
    }

    public static Matrix random(final int rows, final int cols) {
        Matrix matrix = new Matrix(rows, cols);
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.data[i][j] = random.nextDouble();
            }
        }
        return matrix;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double get(final int i, final int j) throws IndexOutOfBoundsException {
        if (i < 0 || i >= rows) {
            throw new IndexOutOfBoundsException("Invalid row index : " + i);
        }
        if (j < 0 || j >= cols) {
            throw new IndexOutOfBoundsException("Invalid column index : " + i);
        }
        return data[i][j];
    }

    public double[] getRow(final int row) throws IllegalArgumentException {
        throwIfInvalidIndex(row, rows, "row");
        return Arrays.copyOf(data[row], cols);
    }

    public double[] getColumn(final int column) throws IllegalArgumentException {
        throwIfInvalidIndex(column, cols, "column");
        double[] col = new double[rows];
        for (int i = 0; i < rows; i++) {
            col[i] = data[i][column];
        }
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Matrix)) return false;
        Matrix matrix = (Matrix) o;
        return rows == matrix.rows &&
                cols == matrix.cols &&
                Arrays.deepEquals(data, matrix.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rows, cols);
        result = 31 * result + Arrays.deepHashCode(data);
        return result;
    }

    @Override
    public String toString() {
        if (rows == 0 || cols == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            if (i == 0) {
                builder.append(" /");
            } else if (i == rows - 1) {
                builder.append(" \\");
            } else {
                builder.append("| ");
            }
            builder.append(String.format("%.2e", data[i][0]));
            for (int j = 1; j < cols; j++) {
                builder.append(", ");
                builder.append(String.format("%.2e", data[i][j]));
            }
            if (i == 0) {
                builder.append("\\");
            } else if (i == rows - 1) {
                builder.append("/");
            } else {
                builder.append(" |");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public boolean isZero() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (data[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isSquare() {
        return rows == cols;
    }

    public boolean isUpperTriangular(double eps) {
        if (!isSquare()) {
            return false;
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < i; j++) {
                if (Math.abs(data[i][j]) > eps) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isLowerTriangular(double eps) {
        if (!isSquare()) {
            return false;
        }
        for (int i = 0; i < rows; i++) {
            for (int j = i + 1; j < cols; j++) {
                if (Math.abs(data[i][j]) > eps) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isDiagonal(double eps) {
        return isLowerTriangular(eps) && isUpperTriangular(eps);
    }

    public boolean isIdentity(double eps) {
        if (!isDiagonal(eps)) {
            return false;
        }
        for (int i = 0; i < rows; i++) {
            if (Math.abs(data[i][i] - 1) > eps) {
                return false;
            }
        }
        return true;
    }

    public void transposeInPlace() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < i; j++) {
                double temp = data[i][j];
                data[i][j] = data[j][i];
                data[j][i] = temp;
            }
        }
    }

    public Matrix transpose() {
        Matrix transpose = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transpose.data[j][i] = data[i][j];
            }
        }
        return transpose;
    }

    public void zeroFillBelow(final double eps) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (Math.abs(data[i][j]) < eps) {
                    data[i][j] = 0;
                }
            }
        }
    }


    public void scaleInPlace(final double scale) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] *= scale;
            }
        }
    }

    public Matrix scale(final double scale) {
        Matrix temp = new Matrix(this);
        temp.scaleInPlace(scale);
        return temp;
    }

    private void throwIncompatible(String operation) throws IllegalArgumentException {
        throw new IllegalArgumentException("Given matrix is not compatible with the invoking matrix for "
                + operation);
    }

    public void addInPlace(final Matrix addend) throws IllegalArgumentException {
        if (addend.rows != rows || addend.cols != cols) {
            throwIncompatible("addition");
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] += addend.data[i][j];
            }
        }
    }

    public Matrix add(final Matrix addend) throws IllegalArgumentException {
        Matrix temp = new Matrix(this);
        temp.addInPlace(addend);
        return temp;
    }

    public void subtractInPlace(final Matrix addend) throws IllegalArgumentException {
        if (addend.rows != rows || addend.cols != cols) {
            throwIncompatible("subtraction");
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] -= addend.data[i][j];
            }
        }
    }

    public Matrix subtract(final Matrix addend) throws IllegalArgumentException {
        Matrix temp = new Matrix(this);
        temp.subtractInPlace(addend);
        return temp;
    }

    public void multiplyInPlace(final Matrix multiplicand) throws IllegalArgumentException {
        if (multiplicand.rows != rows || multiplicand.cols != cols) {
            throwIncompatible("addition");
        }
        double[][] product = new double[rows][cols];
        storeProduct(multiplicand, product);
        for (int i = 0; i < rows; i++) {
            System.arraycopy(product[i], 0, data[i], 0, cols);
        }
    }

    public Matrix multiply(final Matrix multiplicand) throws IllegalArgumentException {
        if (multiplicand.rows != cols) {
            throwIncompatible("multiplication");
        }
        double[][] product = new double[rows][cols];
        storeProduct(multiplicand, product);
        return new Matrix(product, false);
    }

    private void storeProduct(Matrix multiplicand, double[][] product) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < multiplicand.cols; j++) {
                product[i][j] = 0;
                for (int k = 0; k < cols; k++) {
                    product[i][j] += data[i][k] * multiplicand.data[k][j];
                }
            }
        }
    }

    public void elementMultiplyInPlace(final Matrix matrix) throws IllegalArgumentException {
        if (matrix.rows != rows || matrix.cols != cols) {
            throwIncompatible("element-wise multiplication");
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] *= matrix.data[i][j];
            }
        }
    }

    public Matrix elementMultiply(final Matrix matrix) throws IllegalArgumentException {
        Matrix temp = new Matrix(this);
        temp.elementMultiplyInPlace(matrix);
        return temp;
    }

    public void elementDivideInPlace(final Matrix matrix) throws IllegalArgumentException {
        if (matrix.rows != rows || matrix.cols != cols) {
            throwIncompatible("element-wise multiplication");
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] /= matrix.data[i][j];
            }
        }
    }

    public Matrix elementDivide(final Matrix matrix) throws IllegalArgumentException {
        Matrix temp = new Matrix(this);
        temp.elementDivideInPlace(matrix);
        return temp;
    }

    public Matrix exponentiate(int power) throws IllegalArgumentException {
        if (!isSquare()) {
            throwIncompatible("exponentiation");
        }
        if (power < 0) {
            throw new IllegalArgumentException("Power cannot be negative.");
        }
        Matrix product = Matrix.identity(rows);
        Matrix x = new Matrix(this);
        while (power > 0) {
            if ((power & 1) == 1) {
                product.multiplyInPlace(x);
            }
            x.multiplyInPlace(x);
            power >>= 1;
        }
        return product;
    }

    public boolean isOrthogonal(double eps) {
        Matrix value = multiply(transpose());
        value.zeroFillBelow(eps);
        return value.isIdentity(eps);
    }

    public Matrix appendRight(final Matrix matrix) {
        if (matrix.rows != rows) {
            throwIncompatible("appending right");
        }
        Matrix store = new Matrix(rows, cols + matrix.cols);
        for (int i = 0; i < rows; i++) {
            System.arraycopy(data[i], 0, store.data[i], 0, cols);
            System.arraycopy(matrix.data[i], 0, store.data[i], cols, matrix.cols);
        }
        return store;
    }

    public Matrix appendBottom(final Matrix matrix) throws IllegalArgumentException {
        if (matrix.cols != cols) {
            throwIncompatible("appending right");
        }
        Matrix store = new Matrix(rows + matrix.rows, cols);
        for (int i = 0; i < rows; i++) {
            System.arraycopy(data[i], 0, store.data[i], 0, cols);
        }
        for (int i = 0; i < matrix.rows; i++) {
            System.arraycopy(matrix.data[i], 0, store.data[i + rows], 0, cols);
        }
        return store;
    }

    private void throwIfInvalidIndex(int value, int limit, String quantity) throws IllegalArgumentException {
        if (value <= 0 || value > limit) {
            throw new IllegalArgumentException("Invalid " + quantity + " index : " + value);
        }
    }

    public Matrix[] splitAtColumn(final int column) throws IllegalArgumentException {
        throwIfInvalidIndex(column, cols, "column");
        int columnResidue = cols - column;
        Matrix left = new Matrix(rows, column);
        Matrix right = new Matrix(rows, columnResidue);
        for (int i = 0; i < rows; i++) {
            System.arraycopy(data[i], 0, left.data[i], 0, column);
            System.arraycopy(data[i], column, right.data[i], 0, columnResidue);
        }
        return new Matrix[]{left, right};
    }

    public Matrix[] splitAtRow(final int row) throws IllegalArgumentException {
        throwIfInvalidIndex(row, rows, "row");
        int rowResidue = rows - row;
        Matrix top = new Matrix(row, cols);
        Matrix bottom = new Matrix(rowResidue, cols);
        for (int i = 0; i < row; i++) {
            System.arraycopy(data[i], 0, top.data[i], 0, cols);
        }
        for (int i = 0; i < rowResidue; i++) {
            System.arraycopy(data[i + row], 0, bottom.data[i], 0, cols);
        }
        return new Matrix[]{top, bottom};
    }

    public void swapRowsInPlace(final int row1, final int row2) throws IllegalArgumentException {
        throwIfInvalidIndex(row1, rows, "row");
        throwIfInvalidIndex(row2, rows, "row");
        double[] row = new double[cols];
        System.arraycopy(data[row1], 0, row, 0, cols);
        System.arraycopy(data[row2], 0, data[row1], 0, cols);
        System.arraycopy(row, 0, data[row2], 0, cols);
    }

    public Matrix swapRows(final int row1, final int row2) throws IllegalArgumentException {
        Matrix temp = new Matrix(this);
        temp.swapRowsInPlace(row1, row2);
        return temp;
    }

    public void swapColumnsInPlace(int col1, int col2) throws IllegalArgumentException {
        throwIfInvalidIndex(col1, rows, "column");
        throwIfInvalidIndex(col2, rows, "column");
        double[] column = new double[rows];
        for (int i = 0; i < rows; i++) {
            column[i] = data[i][col1];
        }
        for (int i = 0; i < rows; i++) {
            data[i][col1] = data[i][col2];
        }
        for (int i = 0; i < rows; i++) {
            data[i][col2] = column[i];
        }
    }

    public Matrix swapColumn(final int col1, final int col2) throws IllegalArgumentException {
        Matrix temp = new Matrix(this);
        temp.swapColumnsInPlace(col1, col2);
        return temp;
    }
}
