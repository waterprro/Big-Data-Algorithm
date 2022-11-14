/**
 * 
 */
package bloomFilter;

import java.util.BitSet;
import java.util.Random;

/**
 * @author jialin
 *
 */
public class BloomFilterRan {

	private int filterSize;
	
	private int dataSize = 0;
	
	private int numHashes;
	
	private BitSet Set;
	
	private int[] a; //store a value of ax+b
	
	private int[] b;

/**
 * constructor for the bloom filter
 * @param setSize
 * @param bitsPerElement
 */
	BloomFilterRan(int setSize, int bitsPerElement){
		filterSize = find_prime(setSize*bitsPerElement);
		Set = new BitSet(filterSize);
		numHashes = (int) Math.round(Math.log(2)*(filterSize/setSize));
		a = new int[numHashes];
		b = new int[numHashes];
		addHashF(numHashes);
	}

/*
 * help function to generate random value of a and b from 0 to P-1
 */
	private void addHashF(int k) {
		Random r = new Random();
		int A, B;
		for (int i = 0; i < k; i++) {
			A = r.nextInt(filterSize);
			B = r.nextInt(filterSize);
			a[i] = A;
			b[i] = B;
		}
		
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
		for(int i = 0; i < numHashes; i++) {
			int A, B;
			A = a[i];
			B = b[i];
			hashV = Math.abs(((A*s.hashCode()) + B)% filterSize);
			
			Set.set(hashV);
		}
		dataSize++;
	}
	
	public boolean appears(String s) {
		s = s.toLowerCase(); //case insensitive
		int hashV;
		for(int i = 0; i < numHashes; i++) {
			int A, B;
			A = a[i];
			B = b[i];
			hashV = Math.abs(((A*s.hashCode()) + B)% filterSize);
			
			if (Set.get(hashV) == false) {
				return false;
			}
			
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
		return numHashes;
	}
}
