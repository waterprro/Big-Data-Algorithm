/**
 * 
 */
package bloomFilter;

import java.util.BitSet;

/**
 * @author jialin
 *
 */
public class BloomFilterFNV {
	
	private int filterSize;
	
	private int dataSize = 0;
	
	private int numHashes;
	
	private BitSet Set;
	
	private long FNV_64PRIME = Long.parseUnsignedLong("109951168211");
	
	private long FNV_64INIT = Long.parseUnsignedLong("14695981039346656037");

/**
 * constructor for the bloom filter
 * @param setSize
 * @param bitsPerElement
 */
	BloomFilterFNV(int setSize, int bitsPerElement){
		filterSize = find_prime(setSize*bitsPerElement);
		Set = new BitSet(filterSize);
		numHashes = (int) Math.round(Math.log(2)*(filterSize/setSize));
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
		String str = s.toLowerCase(); //case insensitive
		int hashV;
		for(int i = 0; i < numHashes; i++) {
			char[] charList = str.toCharArray();
			long valueH = FNV_64INIT;

			for (int j = 0; j < charList.length; j++) {
				valueH ^= charList[j];
				valueH = (long)((valueH*FNV_64PRIME)%Math.pow(2, 64));
			}

			int a,b;
			a = (int) valueH;
			b = (int) (valueH>>32);
			
			hashV = Math.abs((i * a + b) % filterSize);
			
			Set.set(hashV);
		}
		dataSize++;
	}
	
	public boolean appears(String s) {
		String str = s.toLowerCase(); //case insensitive
		int hashV;
		for(int i = 0; i < numHashes; i++) {
			char[] charList = str.toCharArray();
			long valueH = FNV_64INIT;

			for (int j = 0; j < charList.length; j++) {
				valueH ^= charList[j];
				valueH = (long)((valueH*FNV_64PRIME)%Math.pow(2, 64));
			}
			int a,b;
			a = (int) valueH;
			b = (int) (valueH>>32);
			
			hashV = Math.abs((i * a + b) % filterSize);
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
