class text{
	public static void main(String[] args){
		byte b = (byte)255;
		b = (byte) (b &~(1<<0));

		printbytenice(b);
		
	}

	static void printbytenice(byte b){
		//b = (byte) (b | (1 << 6));
		// Set seventh bit
		//b = (byte) (b & ~(1 << 5));
		// Set sixth bit to zero
		

		//return (b & (1 << bit)) != 0;


		for (int i = 0; i < 8; i++){
			System.out.println((b & (1<<i)) != 0 );
		}

	}
	static byte cross(byte b, int i){
		return (byte)0;
	}
}