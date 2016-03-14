class MatrixSequential{
	
	static double[][] multiply(double[][] a, double[][] b){
		int a_row = a.length;
		int a_col = a[0].length;
		int b_row = b.length;
		int b_col = b[0].length;
		if (a_col != b_row){
			System.out.println("Wrong array dimensions");
			System.exit(1);
		}
		double[][] result = new double[a_row][b_col] ;

		for (int i = 0; i < a_row; i++){
			for (int j = 0; j < b_col; j++){
				for (int k = 0; k < b_row; k++){
					result[i][j] = result[i][j] + a[i][k]*b[k][j];
				}
			}
		}


		return result;
	} 
}


