
import java.util.*;
import java.lang.Math.*;
import java.util.concurrent.*;

class Main{
	public static void main(String[] args){
		double timepoint;
		int n = (int)2.00e7; 


		EratosthenesSilSequential s = new EratosthenesSilSequential(n);

		timepoint = System.nanoTime();
		s.generatePrimesByEratosthenes();
		System.out.printf("Seq prime:  %-10.3f\n", (System.nanoTime()-timepoint)/1000000);

		timepoint = System.nanoTime();
		s.factorizeLast100(true);
		System.out.printf("Seq fact:  %-10.3f\n", (System.nanoTime()-timepoint)/1000000);
		
		EratosthenesSilParallell p = new EratosthenesSilParallell(n, 4);

		timepoint = System.nanoTime();
		p.generatePrimesByEratosthenes();
		System.out.printf("Par prime:  %-10.3f\n", (System.nanoTime()-timepoint)/1000000);
		
		timepoint = System.nanoTime();
		p.factorizeLast100(true);
		System.out.printf("Par fact:  %-10.3f\n", (System.nanoTime()-timepoint)/1000000);
		

	}
}







class EratosthenesSilParallell {
	byte [] bitArr;          
	int  maxNum;
	boolean primesComplete = false; 
	CyclicBarrier barrier;  
	int threads_total;

	EratosthenesSilParallell (int maxNum, int threads) {
		this.maxNum = maxNum;
		bitArr = new byte [(maxNum/16)+1];
		barrier = new CyclicBarrier(threads+1);
		threads_total = threads;
	} 
	public void generatePrimesByEratosthenes() {

		setAllBitsToOne();
		setBitToZero(1);   

		// Starting new threads
		for (int t = 0; t < threads_total; t++){
			new Thread(new generateParallell(t)).start();
		}

		// Barrier main thread
		try{
			barrier.await();
		}
		catch (Exception e){
			System.out.println("Error with CyclicBarrier");
			return;
		}
		primesComplete = true;
	} 

	class generateParallell implements Runnable{
		int thread_num;
		generateParallell(int t){
			thread_num = t;
		}
		public void run(){
			for (int i = 3+thread_num; i < maxNum; i+= threads_total){
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


	public void printFact(long num){

		ArrayList<Long> factList = factorize(num);
		System.out.printf("%d == ", num);
		for (int i = 0; i < factList.size()-1; i ++){
			System.out.printf("%d * ",factList.get(i));
		}
		System.out.printf("%d\n", factList.get(factList.size()-1));	
	}	
	public void factorizeLast100(boolean printIt){
		long startpoint = (long)maxNum*(long)maxNum-100;
		for (long i = 0; i < 100; i ++){
			if (printIt){
				printFact(startpoint+i);
			}
			else{
				factorize(startpoint+i);
			}
		}
	}
		


	public ArrayList<Long> factorize(long num){
		if (!primesComplete){
			System.out.println("Generate primes before factorize");
			System.exit(1);
		}

		ArrayList<Long> factList = new ArrayList<Long>(); 
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

	public void printAllPrimes(){
		System.out.printf("All primes found:\n1, 2, ");
		for (int i = 1; i < maxNum; i++){
			if (getBit(i)){
				System.out.printf("%d, ", i);
			}
		}
		System.out.println();
	}

	public void printLast100(){
		
		int i = maxNum;
		int c = 100;
		while (i > 0 && c > 0){
			if (getBit(i)){
				c--;
				System.out.println(i);
			}
			i --;
		}
	}

	/* ------------ Private help methods ------------ */ 
	private void setAllBitsToOne() {
		for (int i = 0; i < bitArr.length; i++) {
			bitArr[i] = (byte)255;
		}
	}

	private void setBitToZero(int bit) {
		if (bit % 2 == 0){
			return;
		}
		byte b = bitArr[bit/16];
		b = (byte) (b &~(1<< (bit%16/2))) ;
		bitArr[bit/16] = b;
	} 

	private boolean getBit(int i){
		if (i%2 == 0){
			return false;
		}
		return (bitArr[i/16] & (1 << i%16/2)) != 0;
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








class EratosthenesSilSequential {
	byte [] bitArr;          
	int  maxNum;
	boolean primesComplete = false;               

	EratosthenesSilSequential (int maxNum) {
		this.maxNum = maxNum;
		bitArr = new byte [(maxNum/16)+1];
	} 
	public void generatePrimesByEratosthenes() {

		setAllBitsToOne();
		setBitToZero(1);     
		for (int i = 3; i <= maxNum; i ++){
			if (getBit(i) && !isPrime(i)){
				setMultiplumToZero(i);
			}
		}
		primesComplete = true;
	} 
	public void printFact(long num){

		ArrayList<Long> factList = factorize(num);
		System.out.printf("%d == ", num);
		for (int i = 0; i < factList.size()-1; i ++){
			System.out.printf("%d * ",factList.get(i));
		}
		System.out.printf("%d\n", factList.get(factList.size()-1));	
	}	
		
	

	public ArrayList<Long> factorize(long num){
		if (!primesComplete){
			System.out.println("Generate primes before factorize");
			System.exit(1);
		}

		ArrayList<Long> factList = new ArrayList<Long>(); 
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

	public void factorizeLast100(boolean printIt){
		long startpoint = (long)maxNum*(long)maxNum-100;
		for (long i = 0; i < 100; i ++){
			if (printIt){
				printFact(startpoint+i);
			}
			else{
				factorize(startpoint+i);
			}
		}
	}



	public void printAllPrimes(){
		System.out.printf("All primes found:\n1, 2, ");
		for (int i = 1; i < maxNum; i++){
			if (getBit(i)){
				System.out.printf("%d, ", i);
			}
		}
		System.out.println();
	}

	public void printLast100(){
		
		int i = maxNum;
		int c = 100;
		while (i > 0 && c > 0){
			if (getBit(i)){
				c--;
				System.out.println(i);
			}
			i --;
		}
	}

	/* ------------ Private help methods ------------ */ 
	private void setAllBitsToOne() {
		for (int i = 0; i < bitArr.length; i++) {
			bitArr[i] = (byte)255;
		}
	}

	private void setBitToZero(int bit) {
		if (bit % 2 == 0){
			return;
		}
		byte b = bitArr[bit/16];
		b = (byte) (b &~(1<< (bit%16/2))) ;
		bitArr[bit/16] = b;
	} 

	private boolean getBit(int i){
		if (i%2 == 0){
			return false;
		}
		return (bitArr[i/16] & (1 << i%16/2)) != 0;
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