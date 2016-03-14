import java.util.Random;
import java.lang.Math;
import java.util.Arrays.*;
import java.util.*;
import java.util.concurrent.*;


class Main{
	public static void main(String[] args){
		
		int n = 4;
		int return_size = 40;
		int threads  = Runtime.getRuntime().availableProcessors();
		System.out.format("Available Processors: %d\n", threads);
		double[] sequential_times = new double[6];
		double[] parallell_times = new double[6];
		double[] arrys_sort_times = new double[6];
		double[] temp_times = new double[9];
		int[] a;
		Sequential s;
		Parallell p;
		double time_point;
		Random rand = new Random();


		System.out.format("                      ------ Time table in ms ------\n\n");
		System.out.format("Size =         1000       10k        100k       1m         10m        100m\n");
		System.out.format("Arrays sort:   ");
		for (int i = 0; i < n; i ++){
			for (int j = 0; j < 9; j ++){	
				a = new int[1000*(int)(Math.pow(10,i))];
				for (int k = 0; k < a.length; k++){
					a[k] = rand.nextInt(a.length*1000);
				}

				/* Timing array sort */
				time_point = System.nanoTime();
				Arrays.sort(a);
				temp_times[j] = System.nanoTime() - time_point;

				
			}
			Arrays.sort(temp_times);
			arrys_sort_times[i] = temp_times[4]; 
			System.out.format("%-10.3f ", temp_times[4]/1000000);
		}
		System.out.format("\nSequential:    ");
		for (int i = 0; i < n; i ++){
			for (int j = 0; j < 9; j ++){
				s = new Sequential(1000*(int)(Math.pow(10,i)), return_size);
				
				/* Timing sequential search */
				time_point = System.nanoTime();
				s.search();
				temp_times[j] = System.nanoTime() - time_point;	
			}
			Arrays.sort(temp_times);
			sequential_times[i] = temp_times[4]; 
			System.out.format("%-10.3f ", temp_times[4]/1000000);
		}
		System.out.format("\nParallell:     ");
		for (int i = 0; i < n; i ++){
			for (int j = 0; j < 9; j ++){
				p = new Parallell(1000*(int)(Math.pow(10,i)), return_size, threads);

				/* Timing parallell search */
				time_point = System.nanoTime();
				p.searchParallell();
				temp_times[j] = System.nanoTime() - time_point;

			}
			Arrays.sort(temp_times);
			parallell_times[i] = temp_times[4]; 
			System.out.format("%-10.3f ", temp_times[4]/1000000);

		}
		System.out.format("\n\n");

		
		
	}
}

class Parallell{
	private int[] data;
	private int top;
	private int threads;
	private int[] combined_results;
	private CyclicBarrier barrier ;

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
	void searchParallell(){
		int[] datapart;
		int start, end;
		System.out.println(threads);
		for (int i = 0; i < threads; i++){
	
			start = (data.length/threads)*i;
			end = (data.length/threads)*(i+1);
			
			datapart = Arrays.copyOfRange(data, start, end);
			new Thread(new ParallellSearch(datapart)).start();
		} 
		try{
			barrier.await();
		}
		catch (Exception e){
			System.out.println("Error with CyclicBarrier");
			return;
		}

	}

	class ParallellSearch implements Runnable{
		int[] data_chunk;
		int[] toplist = new int[top];
		ParallellSearch(int[] datapart){
			data_chunk = datapart;
		}
		public void run(){
			insertSort(top-1);
			search();
		}
		private void search(){
	
			for (int i = top; i < data_chunk.length; i++){
				if (data_chunk[i] > data_chunk[top-1]){
					serach_hit(i);
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

		private void insertSort(int sort_to){
			int temp;
			int j;

			for (int i = 0; i < sort_to; i++){
				temp = data_chunk[i+1];
				j = i;
				while(i >= 0 && data_chunk[i] < temp){
					data_chunk[i+1] = data_chunk[i];
					i--;
				}
				data_chunk[i+1] = temp;

			}
		}
		private void serach_hit(int hit){
			int temp = data_chunk[hit];
			data_chunk[top-1] = data_chunk[hit];
			data_chunk[hit] = temp;
			insertSort(top-1);
		}		
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
		insertSort(top-1);
	}

	public int[] get_data(int size){
		return data;
	}
	public void print(int size){
		for (int i = 0; i < size; i++){
			System.out.println(data[i]);
		}
	}
	public void search(){
		for (int i = top; i < data.length; i++){
			if (data[i] > data[top-1]){
				serach_hit(i);
			}
		}
	}

	private void insertSort(int sort_to){
		int temp;
		int j;

		for (int i = 0; i < sort_to; i++){
			temp = data[i+1];
			j = i;
			while(i >= 0 && data[i] < temp){
				data[i+1] = data[i];
				i--;
			}
			data[i+1] = temp;

		}
	}

	// For testing
	private void serach_hit(int hit){
		int temp = data[hit];
		data[top-1] = data[hit];
		data[hit] = temp;
		insertSort(top-1);
	}

}

