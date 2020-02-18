import java.io.*;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import static java.lang.Math.*;


public class Main {
    static int size = 0;
    static double[][] m = new double[size][size+1];
    static double e = 0;
    static double[] po = new double[size];
    static int k = 0;

    public static void input(Scanner scanner) throws InputMismatchException, FileNotFoundException, IOException{
        System.out.println("Выберите тип ввода(0 - из файла, 1 - с клавиатуры): ");
        switch (scanner.next()){
            case ("0"): {
                System.out.println("Введите полный путь к файлу: ");
                String path = scanner.next();
                FileReader fr = new FileReader(path);
                BufferedReader reader = new BufferedReader(fr);
                String line = reader.readLine();
                size = Integer.parseInt(line);
                if (size > 20 || size <=0){
                    System.out.println("Неправильный размер");
                    System.exit(0);
                }
                m = new double[size][size+1];
                for (int i=0; i< size; i++) {
                    line = reader.readLine();
                    String[] array = line.split(" ");
                    for (int j=0; j<=size; j++
                    ) {
                        m[i][j] = Integer.parseInt(array[j]);
                    }
                }
                line = reader.readLine().replace(",",".");
                e = Double.parseDouble(line);
                po = new double[size];
                break;
            }
            case ("1"): {
                System.out.println("Размер вводимой матрицы: ");
                size = scanner.nextInt();
                while (size > 20 || size <=0){
                    System.out.println("Размер неправильный");
                    System.out.println("Размер вводимой матрицы: ");
                    size = scanner.nextInt();
                }
                m = new double[size][size + 1];
                System.out.println("Матрица:");
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size + 1; j++) {
                        m[i][j] = scanner.nextDouble();
                    }
                }
                po = new double[size];
                System.out.println("Точность решения");
                e = scanner.nextDouble();
                break;
            }
            default:{
                System.out.println("Wrong answer. Try again.");
                input(scanner);
                break;
            }
        }
    }
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            PrintWriter printWriter = new PrintWriter(System.out);
            scanner.useLocale(new Locale("Russian"));
            input(scanner);
            if (checkDiagonal(size, m)) {
                System.out.println("Диагональное преобладание выполняется");
            } else {
                m = makeDiagonal(size, m);
                if (m == null) {
                    System.out.println("Диагональное преобладание не выполняется");
                    System.exit(0);
                } else {
                    System.out.println("Матрица преобразована.");
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size + 1; j++) {
                            System.out.print(m[i][j] + " ");
                        }
                        System.out.println();
                    }
                }
            }
            double[] previousVariableValues = new double[size];
            double[] currentVariableValues = new double[size];
            for (int i = 0; i < size; i++) {
                previousVariableValues[i] = m[i][size] / m[i][i];
                currentVariableValues[i] = m[i][size] / m[i][i];
            }
            while (true) {
                k++;
                for (int i = 0; i < size; i++) {
                    double x = 0;
                    for (int j = 0; j < size; j++) {
                        if (j != i) {
                            x -= m[i][j] * currentVariableValues[j];
                        }
                    }
                    x += m[i][size];
                    currentVariableValues[i] = x / m[i][i];
                }
                double error = 0.0;


                for (int i = 0; i < size; i++) {
                    if (error < abs(currentVariableValues[i] - previousVariableValues[i])) {
                        error = abs(currentVariableValues[i] - previousVariableValues[i]);
                    }
                    po[i] = abs(currentVariableValues[i] - previousVariableValues[i]);

                }
                if (error < e) {

                    break;
                }
                for (int o = 0; o < size; o++) {
                    previousVariableValues[o] = currentVariableValues[o];
                }
            }
            System.out.println("Погрешности");
            for (int i = 0; i < size; i++) {
                System.out.println(po[i] + " ");
            }
            System.out.println("Итерации " + k);
            System.out.println("Вычисленные неизвестные:");
            for (int i = 0; i < size; i++) {
                printWriter.print(previousVariableValues[i] + "\n");
            }
            scanner.close();
            printWriter.close();

        } catch (ArithmeticException e) {
            System.out.println("Ошибка вычислений");
        } catch (InputMismatchException e) {
            System.out.println("К сожалению введены не те данные ;(");
        } catch (FileNotFoundException e){
            System.out.println("Нет такого файла");
        } catch(IOException e){
            System.out.println("Ошибка ввода-вывода");
        } catch (Exception e){
            System.out.println("Something wrong");
        }
    }


    public static boolean checkDiagonal(int n, double[][] matrix){
        double normal = 0;
        for (int j = 0; j < n; j++) {
            double sum = 0;
            normal = abs(matrix[j][j]);
            for (int i = 0; i < n; i++) {
                sum += abs(matrix[i][j]);
            }
            if (normal < sum-normal){
                return false;
            }
        }
        return true;
    }


    public static double[][] makeDiagonal(int n, double[][] matrix) {

        boolean[][] mat = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < n; j++) {
                sum += abs(matrix[i][j]);
            }
            for (int j = 0; j < n; j++) {
                if (2 * abs(matrix[i][j]) >= sum) {
                    mat[i][j] = true;
                }
            }
        }

        double[][] patch_1 = new double[n][n];
        for (int i = 0; i < n; i++) {
            boolean found = false;
            for (int j = 0; j < n; j++) {
                if (mat[j][i]) {
                    found = true;
                    patch_1[i] = matrix[j];
                    break;
                }
            }
            if (!found){
                return null;
            }

        }
        matrix = patch_1;
        return matrix;
    }
}
