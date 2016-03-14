class MatrixUtility{
	static void printArray(double[][] a){
		for (int i = 0; i < a.length; i ++){
			for (int j = 0; j < a[0].length; j ++){
				System.out.printf("%8.2f", a[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
}