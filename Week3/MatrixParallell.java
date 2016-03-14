import java.util.concurrent.*;


class MatrixParallell{
	protected CyclicBarrier barrier;
	protected double[][] a;
	protected double[][] b;
	protected double[][] result;
	

	MatrixParallell(double[][] a, double[][] b){
		this.a = a;
		this.b = b;
	}


	public double[][] multiply(int threads){
		int a_row = a.length;
		int a_col = a[0].length;
		int b_row = b.length;
		int b_col = b[0].length;
		
		barrier = new CyclicBarrier(threads+1);
		if (a_col != b_row){
			System.out.println("Wrong array dimensions");
			System.exit(1);
		}
		result = new double[a_row][b_col] ;

		int p1 = 0;
		int p2 = 0;
		for (int i = 0; i < threads; i++){
			p2 = p1+a_row/threads+ ( (a_row%threads > i) ? 1 : 0);
			new Thread(new ParallellMultiply(p1, p2)).start();
			p1 = p2;

			
		}
		
		try{
			barrier.await();
		}
		catch (Exception e){
			System.out.println("Error with CyclicBarrier");
			return null;
		}	
		return result;
	} 

	class ParallellMultiply implements Runnable{
		int p1;
		int p2;
		ParallellMultiply(int p1, int p2){
			this.p1 = p1;
			this.p2 = p2;

		}
		public void run(){
			for (int i = p1; i < p2; i++){
				for (int j = 0; j < b[0].length; j++){
					for (int k = 0; k < b.length; k++){
						result[i][j] = result[i][j] + a[i][k]*b[k][j];
					}
				}
			}
			try{
				barrier.await();
			}
			catch (Exception e){
				System.out.println("Error with CyclicBarrier");
				return;
			}	
		}

	}

}