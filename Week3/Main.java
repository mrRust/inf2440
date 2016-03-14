import java.util.Random;

class Main{
	public static void main(String[] args) {
		int a_row = Integer.parseInt(args[0]);
		int a_col = Integer.parseInt(args[1]);
		int b_row = Integer.parseInt(args[2]);
		int b_col = Integer.parseInt(args[3]);
		double[][] a = new double[a_row][a_col];
		double[][] b = new double[b_row][b_col];
		Random rand = new Random();
		for (int i = 0; i < a_row; i ++){
			for (int j = 0; j < a_col; j ++){
				a[i][j] = 1+rand.nextDouble()*9;
			}
		}
		for (int i = 0; i < b_row; i ++){
			for (int j = 0; j < b_col; j ++){
				b[i][j] = 1+rand.nextDouble()*9;
			}
		} 

		//MatrixUtility.printArray(a);
		//MatrixUtility.printArray(b);

		MatrixParallell p = new MatrixParallell(a, b);

		double time = System.nanoTime();
		MatrixSequential.multiply(a, b);
		double seqTime = (System.nanoTime()-time)/1000000;
		System.out.printf("Awalible prossesors : %d\n",Runtime.getRuntime().availableProcessors());
		System.out.printf("Seqtime = %f\n",seqTime);

		for (int i = 2; i < 11; i ++){
			time = System.nanoTime();
			p.multiply(i);
			time = (System.nanoTime()-time)/1000000;
			System.out.printf("Prossesors: %d  Time: %f  Speedup: %f\n", i, time, seqTime/time);
		}
		time = System.nanoTime();
		MatrixSequential.multiply(a, b);
		seqTime = (System.nanoTime()-time)/1000000;
		System.out.printf("Seqtime = %f\n",seqTime);

		//MatrixUtility.printArray(r1);
		//MatrixUtility.printArray(r2);
	}




}