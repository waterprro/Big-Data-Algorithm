/**
 * 
 */
package bloomFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author jialin
 *
 */
public class EmpericalComparison {
	
	private static BloomDifferential bloomDiff;
	
	private static NaiveDifferential naiveDiff;

	public static void main(String[] args) throws FileNotFoundException {
		
		bloomDiff = new BloomDifferential("database.txt",
				"DiffFile.txt", 4);
		naiveDiff = new NaiveDifferential("database.txt",
				"DiffFile.txt");
		
		long[] res = new long[2];
		long totalBloom = 0;
		long totalNaive = 0;
		long begin;
		long stop;
		File grams = new File("grams.txt");
		Scanner scan = new Scanner(grams);
		
			String key = scan.nextLine();
			String resS = null;
			begin = System.currentTimeMillis();
			resS = bloomDiff.retrieveRecord(key.trim());
			stop = System.currentTimeMillis();
			
			long time = stop - begin;
			
			totalBloom += time;
			
			begin = System.currentTimeMillis();
			resS = naiveDiff.retrieveRecord(key.trim());
			stop = System.currentTimeMillis();
			
			long time2 = stop - begin;
			
			totalNaive += time2;
		res[0] = totalBloom ;
		res[1] = totalNaive ;
		
		System.out.println("Bloom Differential average time is : "+res[0]);
		System.out.println("Naive Differential average time is : "+res[1]);
		
	}
}
