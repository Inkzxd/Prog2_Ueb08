import java.lang.reflect.Array;

public class Matrix<T extends Number> {
    private T[][] data;
    private int rows;
    private int cols;

    public Matrix(Class<T> clazz, int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = (T[][]) Array.newInstance(clazz, rows, cols);
    }

    public void setElement(int row, int col, T value) {
        data[row][col] = value;
    }

    public T getElement(int row, int col) {
        return data[row][col];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("[");
            for (int j = 0; j < cols; j++) {
                sb.append(data[i][j]);
                if (j < cols - 1) {
                    sb.append(" ");
                }
            }
            sb.append("]\n");
        }
        return sb.toString();
    }

    public static void main (String[] args) {
        Matrix<Integer> m = new Matrix<>(Integer.class, 2, 2);
        m.setElement(0, 0, 1);
        m.setElement(0, 1, 2);
        m.setElement(1, 0, 3);
        m.setElement(1, 1, 4);
        System.out.println(m);

        Matrix<Double> m2 = new Matrix<>(Double.class, 2, 2);
        m2.setElement(0, 0, 1.1);
        m2.setElement(0, 1, 2.2);
        m2.setElement(1, 0, 3.3);
        m2.setElement(1, 1, 4.4);
        System.out.println(m2);
    }
}