/**
 * 
 */
package bloomFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * @author jialin
 *
 */
public class FalsePositives {
	
	private static String[] wordsList;
	
	public static void main(String[] args) throws FileNotFoundException {
		wordsList = new String[3868];
		File file = new File("words.txt");
		Scanner scan = new Scanner(file);
		int i = 0;
		while(scan.hasNextLine()) {
			String s = scan.nextLine();
			wordsList[i] = s;
			i++;
		}
		scan.close();
		
		double[][] FNVFalsePositive = new double[50][3];
		double[][] RanFalsePositive = new double[50][3];
		double[][] MultiFalsePositive = new double[50][3];
		double[][] NaiveFalsePositive = new double[50][3];
		double[] FNVaverage = new double[3];
		double[] RanAverage = new double[3];
		double[] MultiAverage = new double[3];
		double[] NaiveAverage = new double[3];
		int[] bits = {4, 8, 10};
		
		for (int j = 0; j < 50; j++) {
			double FalsePositiveRate[] = new double[4];
			FalsePositiveRate = getRate(4);
			FNVFalsePositive[j][0] = FalsePositiveRate[0];
			RanFalsePositive[j][0] = FalsePositiveRate[1];
			MultiFalsePositive[j][0] = FalsePositiveRate[2];
			NaiveFalsePositive[j][0] = FalsePositiveRate[3];
			
			FalsePositiveRate = getRate(8);
			FNVFalsePositive[j][1] = FalsePositiveRate[0];
			RanFalsePositive[j][1] = FalsePositiveRate[1];
			MultiFalsePositive[j][1] = FalsePositiveRate[2];
			NaiveFalsePositive[j][1] = FalsePositiveRate[3];
			
			FalsePositiveRate = getRate(10);
			FNVFalsePositive[j][2] = FalsePositiveRate[0];
			RanFalsePositive[j][2] = FalsePositiveRate[1];
			MultiFalsePositive[j][2] = FalsePositiveRate[2];
			NaiveFalsePositive[j][2] = FalsePositiveRate[3];
		}
		
		FNVaverage = getAverage(FNVFalsePositive);
		RanAverage = getAverage(RanFalsePositive);
		MultiAverage = getAverage(MultiFalsePositive);
		NaiveAverage = getAverage(NaiveFalsePositive);
		
		for (int j = 0; j < 3; j++) {
			System.out.println("The average false positive rate for "+bits[j]+" using FNV is "+FNVaverage[j]);
			System.out.println("The average false positive rate for "+bits[j]+" using Ran is "+RanAverage[j]);
			System.out.println("The average false positive rate for "+bits[j]+" using Multi is "+MultiAverage[j]);
			System.out.println("The average false positive rate for "+bits[j]+" using Naive is "+NaiveAverage[j]);
		}
		
		
	}

	private static double[] getAverage(double[][] FalsePositive) {
		double[] average = new double[FalsePositive[0].length];
		for( int i = 0; i <FalsePositive[0].length; i++) {
			double sum = 0;
			for( int j = 0; j < FalsePositive.length; j++) {
				sum += FalsePositive[j][i];
			}
			average[i] = sum / FalsePositive.length ;
		}
		
		return average;
	}

	private static double[] getRate(int bits) {
		BloomFilterFNV fnv = new BloomFilterFNV(wordsList.length, bits);
		BloomFilterRan ran = new BloomFilterRan(wordsList.length, bits);
		MultiMultiBloomFilter multi = new MultiMultiBloomFilter(wordsList.length, bits);
		NaiveBloomFilter naive = new NaiveBloomFilter(wordsList.length, bits);
		int fnvN = 0, ranN = 0, multiN = 0, naiveN = 0;
		
		for (int i = 0; i < wordsList.length; i++) {
			fnv.add(wordsList[i]);
			ran.add(wordsList[i]);
			multi.add(wordsList[i]);
			naive.add(wordsList[i]);
		}
		
		Random rand = new Random();
		for (int i = 0; i < wordsList.length; i++) {
			String s = wordsList[i] + Integer.toString(rand.nextInt(3000000));
			if (fnv.appears(s)) {
				fnvN++;
			}
			if (ran.appears(s)) {
				ranN++;
			}
			if (multi.appears(s)) {
				multiN++;
			}
			if (naive.appears(s)) {
				naiveN++;
			}
			
		}
		double[] rates = {(double)fnvN / wordsList.length, (double)ranN / wordsList.length, (double)multiN / wordsList.length, (double)naiveN / wordsList.length};
		
		return rates;
		
		
		
	}
}
