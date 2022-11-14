/**
 * 
 */
package bloomFilter;

import java.util.Random;

/**
 * @author jialin
 *
 */
public class NaiveBloomFilter {
	private int filterSize;
	
	private int dataSize = 0;
	
	private int[] array;
	
	private int a;
	
	private int b;

/**
 * constructor for the bloom filter
 * @param setSize
 * @param bitsPerElement
 */
	NaiveBloomFilter(int setSize, int bitsPerElement){
		filterSize = find_prime(setSize*bitsPerElement);
		array = new int[filterSize]; //default 0 for integer array
		addHashF();
	}

/*
 * help function to generate random value of a and b from 0 to P-1
 */
	private void addHashF() {
		Random r = new Random();
		a = r.nextInt(filterSize);
		b = r.nextInt(filterSize);
		
	}

/*
 * 	try to find the next prime number larger than n
 */
	private int find_prime(int n) {
		boolean isFound = false;
		if(n <= 1) {
			return 2;
		}
		int i = n;//check the number after n
		while (!isFound) {
			i++;
			if(is_prime(i)) {
				isFound = true;//break the loop
			}
		}
		return i;
	}
/**
 * check if a number is a prime
 * @param n
 * @return true or false
 */
	private boolean is_prime(int n) {
		if (n <= 1) {
			return false;
		}
		if (n==2||n==3) {
			return true;
		}
		if (n % 2 ==0||n%3 ==0) {
			return false;
		}
		for (int i = 5; i * i <=n; i = i +6) {
			if (n % i ==0) {
				return false;
			}
			if (n % (i + 2) == 0) {
				return false;
			}
		}
		return true;
	}

	public void add(String s) {
		s = s.toLowerCase(); //case insensitive
		int hashV;
		hashV = Math.abs(((a*s.hashCode()) + b)% filterSize);
		array[hashV] = 1;
		dataSize++;
	}
	
	public boolean appears(String s) {
		s = s.toLowerCase(); //case insensitive
		int hashV;
		hashV = Math.abs(((a*s.hashCode()) + b)% filterSize);
			
		if (array[hashV] != 1) {
			return false;
		}
			
		return true;
	}
	
	public int filterSize() {
		return filterSize;
	}
	
	public int dataSize() {
		return dataSize;
	}
	
	public int numHashes() {
		return 1;
	}
}
