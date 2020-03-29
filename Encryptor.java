import java.util.Arrays;
import java.util.Scanner;

public class Encryptor {
	public static void main(String[] args) {
		System.out.println("Option (1): Encrypt a message and see it's key and inverse matrix to decrypt yourself or by computer");
		System.out.println("Option (2): Enter a encrypted message and program decrypts for you with a inverse matrix you enter");
		System.out.println("Option (3): Enter a encrypted message and program decrypts for you with the original key that was used to encrypt message");
		Scanner scanner = new Scanner(System.in);
		
		switch (Integer.parseInt(scanner.nextLine())) {
		case 1:
			option1(scanner);
			break;
		case 2:
			option2(scanner);
			break;
		case 3:
			option3(scanner);
		}
		
		 		
		
		
	}
	public static void option3(Scanner scanner) {
		System.out.println("number of rows in encrypted message: ");
		int row = Integer.parseInt(scanner.nextLine());
		System.out.println("number of cols in encrypted message: ");
		int col = Integer.parseInt(scanner.nextLine());
		
		double[][] matrix = getMatrix(row, col, scanner);
		
		System.out.println("Now enter the key that was used to encrypt the message");
		double[][] key = getMatrix(col, col, scanner);
		
		System.out.println("Here is the inverse matrix of key");
		double[][] inverse = getInverse(key);
		ArrayPrinter(inverse);
		
		System.out.println("Here is the secret message");
		double[][] orig = encryptor(matrix, inverse);
		ArrayPrinter(orig);
		
		System.out.println("Here is the message in string: " + translator(orig));
		
	}
 	public static void option2(Scanner scanner) {
		System.out.println("number of rows in encrypted message: ");
		
		int row = Integer.parseInt(scanner.nextLine());
		System.out.println("number of cols in encrypted message: ");
		int col = Integer.parseInt(scanner.nextLine());
		double[][] matrix = getMatrix(row,col, scanner);
		System.out.println("Now enter the inverse matrix");
		double[][] inverse = getMatrix(col, col, scanner);
		
		double[][] orig = encryptor(matrix, inverse);
		System.out.println("Here is the secret message in matrix form");
		ArrayPrinter(orig);
		System.out.println("Here is the message in string form: " + translator(orig));
	}
	public static void option1(Scanner scanner) {
		/*
		 * int[][] test = new int[2][2]; test[0] = new int[] {7,-2}; test[1] = new int[]
		 * {3,5}; double[][] temp = getInverse(test); for (int i = 0; i < temp.length;
		 * i++) { System.out.println(Arrays.toString(temp[i])); }
		 */
		System.out.println("Enter a message to be encrypted: ");
		String message = scanner.nextLine();
		double[][] matrix = matrify(message);
		
		System.out.println("Message in matrix form");
		ArrayPrinter(matrix);
		
		double[][] key = generator(matrix.length);
		System.out.println("Key matrix");
		ArrayPrinter(key);

		System.out.println("Encrypted matrix");
		double[][] encrypted = encryptor(matrix, key);
		ArrayPrinter(encrypted);
		
		System.out.println("Here is inverse matrix, try multipyling it with the encrypted matrix to get your hidden message:");
		double[][] inverse = getInverse(key);
		ArrayPrinter(inverse);
		
		double[][] orig = encryptor(encrypted, inverse);
		ArrayPrinter(orig);
	}
	public static String translator(double[][] matrix) {
		String ans = "";
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] == -1) {
					ans += " ";
				} else {
					ans += (char)(Math.ceil(matrix[i][j]) + 97);
				}
			}
		}
		return ans;
	}
	
	//space or empty character represents -1, a-z is 0,1,2...25
	//puts the message into square matrix form
	public static double[][] matrify(String str) {
		int dim = (int)Math.ceil(Math.sqrt(str.length()));
		double[][] arr = new double[dim][dim];
		int counter = 0;
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if (counter >= str.length()) {
					arr[i][j] = -1;
				} else {
					arr[i][j] = str.substring(counter, counter + 1).toLowerCase().charAt(0) - 97;
					counter++;
				}
			}
		}
		return arr;
	}
	public static void ArrayPrinter(double[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				System.out.print(array[i][j] + " ");
			}
			System.out.println();
		}
	}
	//fills the array with all values except those of the current column and row
	public static double[][] filler(double[][] arr, int i, int j) {
		double[][] ret = new double[arr.length - 1][arr.length - 1];
		int rct = 0;
		int cct = 0;
		for (int a = 0; a < arr.length; a++) {
			for (int b = 0; b < arr.length; b++) {
				if (a != i && b != j) {
					ret[rct][cct] = arr[a][b];
					cct++;
					if (cct == ret.length) {
						rct++;
						cct = 0;
					}
				}
			}
		}
		return ret;
	}
	
	//returns the matrix of minors
	public static double[][] matrixOfMinors(double[][] arr) {
		//find matrix of minors
		double[][] ret = new double[arr.length][arr.length];
		for (int i = 0; i < arr[0].length; i++) {
			for (int j = 0; j < arr.length; j++) {
				double[][] subMatrix = filler(arr, i, j);
				ret[i][j] = determinant(subMatrix);
			}
		}
		return ret;
	}
	
	//finds the inverse of the key matrix
	public static double[][] getInverse(double[][] arr) {
		double[][] matrixMinors = new double[arr.length][arr.length];
		double[][] newMat = new double[arr.length][arr.length];
		if (arr.length == 2) {
			double det = determinant(arr);
			newMat[0][0] = (double)arr[1][1] / det;
			newMat[1][1] = (double)arr[0][0] / det;
			newMat[0][1] = arr[0][1] * -1;
			newMat[1][0] = arr[1][0] * -1;
			newMat[0][1] /= det;
			newMat[1][0] /= det;
		} else {
			matrixMinors = matrixOfMinors(arr);
			double det = determinant(arr);
			//turn into matrix of cofactors
			for (int i = 0; i < arr.length; i++) {
				for (int j = 0; j < arr.length; j++) {
					if ((i + j) % 2 == 1) {
						matrixMinors[i][j] *= -1;
					}
				}
			}
			//transpose & divide
			for (int i = 0; i < arr.length; i++) {
				for (int j = 0; j < arr.length; j++) {
					newMat[i][j] = matrixMinors[j][i];
					newMat[i][j] /= det;
				}
			}
		}
		return newMat;
	}
	
	//gets determinant of matrix
	public static double determinant(double[][] arr) {
		int det = 0;
		if (arr.length == 2) {
			return arr[0][0] * arr[1][1] - arr[0][1] * arr[1][0];
		}
		for (int i = 0; i < arr[0].length; i++) {
			double[][] subMatrix = filler(arr, 0, i);
			if (i % 2 == 0) {
				det += arr[0][i] * determinant(subMatrix);
			} else {
				det -= arr[0][i] * determinant(subMatrix);
			}
		}
		return det;
	}
	//generates a key matrix;
	public static double[][] generator(int dim) {
		double[][] arr = new double[dim][dim];
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr.length; j++) {
				arr[i][j] = (int)(Math.random() * 25) + 1;
			}
		}
		if (determinant(arr) == 0) {
			return generator(dim);
		}
		return arr;	
	}
	
	// return new matrix by multiplying message by key matrix
    public static double[][] encryptor(double[][] a, double[][] b) {
        int m1 = a.length;
        int n1 = a[0].length;
        int m2 = b.length;
        int n2 = b[0].length;
        double[][] c = new double[m1][n2];
        for (int i = 0; i < m1; i++)
            for (int j = 0; j < n2; j++)
                for (int k = 0; k < n1; k++)
                    c[i][j] += a[i][k] * b[k][j];
        for (int i =0; i < c.length; i++) {
        	for (int j = 0; j < c[0].length; j++) {
        		//c[i][j] %= 26;
        	}
        }
        return c;
    }
    public static double[][] getMatrix(int row, int col, Scanner scanner) {
    	double[][] matrix = new double[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col ; j++) {
				System.out.print("Number in row: " + i + " col: " + j );
				matrix[i][j] = Double.parseDouble(scanner.nextLine());
			}
		}
		return matrix;
    }
}
