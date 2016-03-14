import java.util.Random;
import java.lang.Math;
import java.util.Arrays.*;
import java.util.*;
import java.util.concurrent.*;
/*
Run program whith oblig values (tar 2 min):
$ java Main; 

Run program with custom values:
$ java Main <start_Length> <increment_runs> <top> <repeat_runs> <threads>
*/


class Main{
	public static void main(String[] args){
		
		int start_Length, increment_runs, top, repeat_runs, threads;
		if (args.length == 5){
			start_Length = Integer.parseInt(args[0]);
			increment_runs = Integer.parseInt(args[1]);
			top = Integer.parseInt(args[2]);
			repeat_runs = Integer.parseInt(args[3]);
			threads = Integer.parseInt(args[4]);
		}	
		else {
			start_Length = 1000;
			increment_runs = 6;
			top = 40;
			repeat_runs = 9;
			threads = Runtime.getRuntime().availableProcessors();
		}


		Controller batman = new Controller();
		batman.runAll(start_Length, increment_runs, top, repeat_runs, threads);

	}
}

class Controller{
	public void runAll(int start_Length, int increment_runs, int top, int repeat_runs, int threads){
		System.out.printf("Size =         ");
		for (int i = 0; i < increment_runs; i++){
			System.out.printf("%-10d ", start_Length*(int)(Math.pow(10, i)));
		}
		System.out.println();
		runArraySort(start_Length, increment_runs, top, repeat_runs);
		runSequential(start_Length, increment_runs, top, repeat_runs);
		runParallell(start_Length, increment_runs, top, repeat_runs, threads);
		
	}

	public void runArraySort(int start_Length, int increment_runs, int top, int repeat_runs){
		double timelist[] = new double[repeat_runs];
		int a[];
		Random rand = new Random();
		double time_point;
		System.out.printf("Arrays sort    ");
		for (int i = 0; i < increment_runs-1; i ++){
			a = new int[start_Length*(int)(Math.pow(10,i))];
			for (int j = 0; j < repeat_runs; j ++){	
				for (int k = 0; k < a.length; k++){
					a[k] = rand.nextInt(a.length);
				}
				/* Timing array sort */
				time_point = System.nanoTime();
				Arrays.sort(a);
				timelist[j] = System.nanoTime() - time_point;
				
			}
			Arrays.sort(timelist);
			if (repeat_runs%2 == 0){
				System.out.format("%-10.3f ", (timelist[repeat_runs/2]+timelist[repeat_runs/2-1])/1000000);
			}
			else{
				System.out.format("%-10.3f ", timelist[repeat_runs/2]/1000000);
			}
		}
		System.out.println();
	}
	public void runSequential(int start_Length, int increment_runs, int top, int repeat_runs){

		double timelist[] = new double[repeat_runs];
		int a[];
		Random rand = new Random();
		double time_point;
		System.out.printf("Sequential     ");
		for (int i = 0; i < increment_runs; i ++){
			int l = start_Length*(int)(Math.pow(10,i));
			for (int j = 0; j < repeat_runs; j ++){	
				
				Sequential s = new Sequential(l, top);
				
				time_point = System.nanoTime();
				s.search();
				timelist[j] = System.nanoTime() - time_point;
			}
			Arrays.sort(timelist);
			if (repeat_runs%2 == 0){
				System.out.format("%-10.3f ", (timelist[repeat_runs/2]+timelist[repeat_runs/2-1])/1000000);
			}
			else{
				System.out.format("%-10.3f ", timelist[repeat_runs/2]/1000000);
			}

		}
		System.out.println();
	}

	public void runParallell(int start_Length, int increment_runs, int top, int repeat_runs, int threads){
		double timelist[] = new double[repeat_runs];
		int a[];
		Random rand = new Random();
		double time_point;
		System.out.printf("Parallell      ");
		for (int i = 0; i < increment_runs; i ++){
			int l = start_Length*(int)(Math.pow(10,i));
			for (int j = 0; j < repeat_runs; j ++){	
				
				Parallell p = new Parallell(l, top, threads);
				/* Timing array sort */
				time_point = System.nanoTime();
				p.searchParallell();
				timelist[j] = System.nanoTime() - time_point;

				
			}
			Arrays.sort(timelist);
			if (repeat_runs%2 == 0){
				System.out.format("%-10.3f ", (timelist[repeat_runs/2]+timelist[repeat_runs/2-1])/1000000);
			}
			else{
				System.out.format("%-10.3f ", timelist[repeat_runs/2]/1000000);
			}
		}
		System.out.println();
	}

}

class Sequential{
	private int[] data;
	private int top;
	Sequential(int size, int return_size){
		data = new int[size];
		top = return_size;
		Random rand = new Random();

		for (int i = 0; i < size; i++){
			data[i] = rand.nextInt(size*1000);
		}
		insertSort(0, top-1);
	}
	
	public void search(){
		int temp;
		int c = 0;
		for (int i = top; i < data.length; i++){
			if (data[i] > data[top-1]){
				temp = data[i];
				data[i] = data[top-1];
				data[top-1] = temp;
				insertSort(0, top-1);
				c++;
 			}
		}
	
	}
	public void printTop(int num){
		System.out.println();
		for (int i = 0; i < num; i++){
			System.out.printf("%d, ", data[i]);
		}
		System.out.println();
	}
	
	void insertSort (int v, int h){
		int i , t ;
		for (int k = v; k < h; k++){
			t = data[k+1];
			i = k;
			while (i >= v && data[i] < t){
				data[i+1] = data[i];
				i--; 
			}
			data[i+1] = t; 
		} 
	}
}

/*
Exercise 2, solve problem parallell
*/
class Parallell{
	private int[] data;
	private int top;
	private int threads;
	private int[] combined_results;
	private CyclicBarrier barrier;
	

	Parallell(int size, int return_size, int t){
		data = new int[size];
		top = return_size;
		threads = t;
		Random rand = new Random();
		barrier = new CyclicBarrier(threads + 1);
		combined_results = new int[threads*top];

		for (int i = 0; i < size; i++){
			data[i] = rand.nextInt(size*1000);
		}
		
	}
	public void printTop(int num){
		for (int i = 0; i < num; i++){
			System.out.printf("%d ", data[i]);
		}
		System.out.println();
	}
	public void printComb(){
		for (int i = 0; i < top; i++){
			System.out.printf("%d ", combined_results[i]);
		}
		System.out.println();
	}
	/* 
	Initalizing and start threds
	*/ 
	void searchParallell(){
		
		int start, end;

		for (int i = 0; i < threads; i++){
			
			start = (data.length/threads)*i;
			end = (data.length/threads)*(i+1)-1;
			
			new Thread(new ParallellSearch(start, end, i)).start();
		} 
		try{
			barrier.await();
		}
		catch (Exception e){
			System.out.println("Error with CyclicBarrier");
			return;
		}
	}

	/*
	Class to do the search in parallell
	*/
	class ParallellSearch implements Runnable{
		int p1, p2;
		int[] toplist = new int[top];
		int t;


		ParallellSearch(int start, int end, int thread){
			
			p1 = start;
			p2 = end;
			t = thread;
		}
		public void run(){
			insertSort(p1, p1+top-1);
			search();
			for (int i = 0; i < top; i++){
				combined_results[t*top+i] = data[p1+i];
			}

		}

		private void search(){
			int temp;
			
			int c = 0;
			for (int i = top+p1; i < p2; i++){
				if (data[i] > data[p1+top-1]){
					temp = data[i];
					data[i] = data[p1+top-1];
					data[p1+top-1] = temp;
					insertSort(p1, p1+top-1);
					c ++;
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
		

		void insertSort (int v, int h){
			int i , t ;
			for (int k = v; k < h; k++){
				t = data[k+1];
				i = k;
				while (i >= v && data[i] < t){
					data[i+1] = data[i];
					i--; 
				}
				data[i+1] = t; 
			} 
		}
	}
}





