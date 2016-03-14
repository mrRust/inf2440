
import java.util.*;
import java.lang.Math.*;

class Main{
	public static void main(String[] args){
		Controller c = new Controller();
		c.testrunPrimesSeq();

	}
}


class Controller{
	public void runAll(int start_len, int increment_runs, int repeat_runs, int threads){
		
		
	}

	public void runPrimesSeq(int start_len, int increment_runs, int repeat_runs){
		System.out.println("Primes sequential: ");
		repeat_runs -= (repeat_runs%2+1);
		double timelist[] = new double[repeat_runs];
		double time_point;
		for (int i = 0; i < increment_runs-1; i ++){
			for (int j = 0; j < repeat_runs; j ++){	
				
			}
			
		}
		System.out.println();
	}
	public void runFactSeq(int start_Length, int increment_runs, int top, int repeat_runs){
		System.out.println("Fact sequential:   ");
		repeat_runs -= (repeat_runs%2+1);
		double timelist[] = new double[repeat_runs];
		double time_point;
		for (int i = 0; i < increment_runs-1; i ++){
			for (int j = 0; j < repeat_runs; j ++){	
				
			}
			
		}
		System.out.println();
	}
	public void testrunPrimesSeq(){
		System.out.println("Primes test run sequential: ");
		EratosthenesSilSequential e = new EratosthenesSilSequential(100);
		e.generatePrimesByEratosthenes();
		e.printAllPrimes();
	}

	public void runPrimesPar(int start_len, int increment_runs, int repeat_runs){
		System.out.println("Primes parallell:  ");
		repeat_runs -= (repeat_runs%2+1);
		double timelist[] = new double[repeat_runs];
		double time_point;
		for (int i = 0; i < increment_runs-1; i ++){
			for (int j = 0; j < repeat_runs; j ++){	
				
				
			}
			
		}
		System.out.println();
	}
	public void runFactPar(int start_Length, int increment_runs, int top, int repeat_runs){
		System.out.println("Fact parallell:    ");
		repeat_runs -= (repeat_runs%2+1);
		double timelist[] = new double[repeat_runs];
		double time_point;
		for (int i = 0; i < increment_runs-1; i ++){
			for (int j = 0; j < repeat_runs; j ++){	
				
				
			}
			
		}
		System.out.println();
	}
	public void testrunPrimesPar(){
		System.out.println("Primes test run parallell:  ");
	}
}
class FactSequential{
	public void printFact(long num){

		ArrayList<Long> factList = factorize(num);
		System.out.printf("%d == ", num);
		for (int i = 0; i < factList.size()-1; i ++){
			System.out.printf("%d * ",factList.get(i));
		}
		System.out.printf("%d\n", factList.get(factList.size()-1));	
	}	
		
	

	public ArrayList<Long> factorize(long num){
		System.out.printf("factorize %d\n",num );
		ArrayList<Long> factList = new ArrayList<Long>(); 
		for (long i = 2; i <= (long)Math.sqrt(num); i++){
			while (num % i == 0){
				factList.add(i);
				num /= i;
			}
		}
		factList.add(num);
		return factList;
	}
}


class EratosthenesSilSequential {
	byte [] bitArr ;          
	int  maxNum;               

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