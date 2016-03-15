
import java.util.*;
import java.lang.Math.*;
import java.util.concurrent.*;

class Main{
	public static void main(String[] args){
		double timepoint;
		int n = (int)2.00e2; 
		int threads = Runtime.getRuntime().availableProcessors();
		boolean print = true;
		
		EratosthenesSil e = new EratosthenesSil(n);
		System.out.printf("n = %d, threads = %d (Time are in ms)\n", n, threads);

		timepoint = System.nanoTime();
		e.generatePrimesByEratosthenes();
		System.out.printf("EratosthenesSil Sequential: %-10.3f\n", (System.nanoTime()-timepoint)/1000000);
		
		e.printLast100();

		timepoint = System.nanoTime();
		e.factorize(print);
		System.out.printf("Factorization Sequential:   %-10.3f\n", (System.nanoTime()-timepoint)/1000000);
		
		timepoint = System.nanoTime();
		e.generatePrimesByEratosthenes(threads);
		System.out.printf("EratosthenesSil Parallell:  %-10.3f\n", (System.nanoTime()-timepoint)/1000000);
		
		e.printLast100();

		timepoint = System.nanoTime();
		e.factorize(print, threads);
		System.out.printf("Factorization Parallell:    %-10.3f\n", (System.nanoTime()-timepoint)/1000000);
		

	}
}







class EratosthenesSil{
	byte [] bitarray;          
	int  maxNum;
	CyclicBarrier barrier;  
	int tt; // Total threds

	EratosthenesSil(int maxNum) {
		this.maxNum = maxNum;
		bitarray = new byte [(maxNum/16)+1];
	}


	/* --- Generate primes sequential --- */
	public void generatePrimesByEratosthenes() {
		setAllBitsToOne();
		setBitToZero(1);     
		for (int i = 3; i <= maxNum; i ++){
			if (getBit(i) && !isPrime(i)){
				setMultiplumToZero(i);
			}
		}
	} 
	/* --- Generate primes in parallell --- */
	public void generatePrimesByEratosthenes(int total_threads) {
		this.tt = total_threads;
		setAllBitsToOne();
		setBitToZero(1);
		barrier = new CyclicBarrier(tt+1);
		for (int t = 0; t < tt; t++){
			new Thread(new generateParallell(t)).start();
		}
		try {
			barrier.await();
		}
		catch (Exception e){
			System.exit(1);
		}
	} 

	class generateParallell implements Runnable{
		int thread_num;
		generateParallell(int t){
			thread_num = t;
		}
		public void run(){
			for (int i = 3+thread_num; i < maxNum; i += tt){
				if (getBit(i) && !isPrime(i)){
					setMultiplumToZero(i);
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




	/* --- Factorize sequential --- */ 
	public void factorize(boolean print){
		long startpoint = (long)maxNum*(long)maxNum-100;
		ArrayList factList;
		for (long i = 0; i < 100; i ++){
			factList = factorizeSingle(startpoint+i);
			if (print){
				printFact(factList);
			}
		}
	}

	public ArrayList<Long> factorizeSingle(long num){
		ArrayList<Long> factList = new ArrayList<Long>(); 
		factList.add(num);
		for (int i = 2; i <= (int)Math.sqrt(num); i++){
			if (getBit(i)){
				while (num % i == 0){
					factList.add((long)i);
					num /= i;
				}
			}
		}
		factList.add(num);
		return factList;
	}

	/* --- Factorize in parallell --- */
	public void factorize(boolean print, int total_threads){
		this.tt = total_threads;
		long startpoint = (long)maxNum*(long)maxNum-100;
		ArrayList factList;
		for (long i = 0; i < 100; i ++){
			factList = factorizeSingleParallell(startpoint+i);
			if (print){
				printFact(factList);
			}
		}
	}

	public ArrayList<Long> factorizeSingleParallell(long num){
		ArrayList<Long> factList = new ArrayList<Long>();
		factList.add(num); 
		barrier = new CyclicBarrier(tt+1);
		for (int t = 0; t < tt; t++){
			new Thread(new FactorizeParallell(t, num, factList)).start();
		}
		try {
			barrier.await();
		}
		catch (Exception e){
			System.exit(1);
		}
		return factList;
	}

	class FactorizeParallell implements Runnable{
		int thread_num; 
		long num;
		ArrayList<Long> factList;
		ArrayList<Long> localFactList;
		long startnum;
		FactorizeParallell(int t, long n, ArrayList<Long> f){
			thread_num = t;
			factList = f;
			num = n;
			startnum = n;
			localFactList = new ArrayList<Long>();
		}
		public void run(){
			for (int i = 2+thread_num; i <= (int)(Math.sqrt(startnum)); i += tt){

				//System.out.printf(" %d ", i);
				
				if (getBit(i)){
					while (num % i == 0){
						localFactList.add((long)i);
						num /= i;

					}
				}
			}

			//System.out.println(localFactList);
			combinelists(factList, localFactList);
			
			try {
				barrier.await();
			}
			catch (Exception e){
				System.out.println("Error with CyclicBarrier");
				System.exit(1);
			}
		}
	} 


	
	public void printLast100(){	
		int i = maxNum;
		int c = 100;
		System.out.println("Last 100 primes found:");
		while (i > 0 && c > 0){
			if (getBit(i)){
				c--;
				System.out.printf("%15d,", i);
				if (c % 10 == 0){
					System.out.println();
				}
			}
			i --;
		}
		System.out.println();
	}
	/* ------------ Private help methods to factorize --------- */
	private void printFact(ArrayList<Long> factList){
		System.out.printf("%d == ", factList.get(0));
		for (int i = 1; i < factList.size()-1; i++){
			System.out.printf("%d * ",factList.get(i));
		}
		System.out.printf("%d\n", factList.get(factList.size()-1));	
	}

	private synchronized void combinelists(ArrayList<Long> factList, ArrayList<Long> localFactList){
		factList.addAll(localFactList);
	}

	/* ------------ Private help methods to primes ------------ */ 
	private void setAllBitsToOne() {
		for (int i = 0; i < bitarray.length; i++) {
			bitarray[i] = (byte)255;
		}
	}

	private void setBitToZero(int bit) {
		if (bit % 2 == 0){
			return;
		}
		byte b = bitarray[bit/16];
		b = (byte) (b &~(1<< (bit%16/2))) ;
		bitarray[bit/16] = b;
	} 

	private boolean getBit(int i){
		if (i%2 == 0){
			return false;
		}
		return (bitarray[i/16] & (1 << i%16/2)) != 0;
	}

	public boolean isPrime(int i){
		for (int counter = 3; counter <= Math.sqrt(i); counter += 2){
			if (i%counter == 0){
				return false;
			}
		}
		return true;
	}

	private void setMultiplumToZero(int i){
		for (int j = 1; j*i <= maxNum; j++){
			setBitToZero(i*j);
		}
	} 
}