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
public class BloomDifferential {
	
	private BloomFilterFNV bfFNV;
	
	private String dataFileName;
	
	private String diffFileName;
	
	private int diffSize = 0;
	
	private int bitsPerElement;
	
	public BloomDifferential(String dataFileName, String diffFileName, int bitsPerElement) throws FileNotFoundException {
		this.dataFileName = dataFileName;
		this.diffFileName = diffFileName;
		this.bitsPerElement = bitsPerElement;
		
		File diffFile = new File(diffFileName);
		
		//scan the diff file and get the size of it
		try {
			Scanner scan = new Scanner(diffFile);
			while(scan.hasNextLine()) {
				scan.nextLine();
				diffSize++;
			}
			scan.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		createFilter();
		
	}

	private void createFilter() throws FileNotFoundException {
		Scanner scan = new Scanner(new File(diffFileName));
		bfFNV = new BloomFilterFNV(diffSize, bitsPerElement);
		
		while(scan.hasNextLine()) { //add key to the bloom filter
			String str = scan.nextLine();
			Scanner scanStr = new Scanner(str);
			String key[] = new String[4];
			int i = 0;
			
			while(scanStr.hasNext()&&i<4) {
				key[i] = scanStr.next();
			}
			scanStr.close();
			String s = "";
			for (int j = 0; j < 3; j++) {
				s += key[j]+" ";
			}
			s += key[3];
			bfFNV.add(s);	
		}
	}
	
	public String retrieveRecord(String key) throws FileNotFoundException {
		String res = null;
		if (bfFNV.appears(key)) {
			Scanner scan = new Scanner(new File(diffFileName));
			while (scan.hasNextLine()) {
				String str = scan.nextLine();
				Scanner scanStr = new Scanner(str);
				String keyList[] = new String[4];
				int i = 0;
				
				while(scanStr.hasNext()&&i<4) {
					keyList[i] = scanStr.next();
				}
				scanStr.close();
				String s = "";
				for (int j = 0; j < 3; j++) {
					s += keyList[j]+" ";
				}
				s += keyList[3];
				if (s.equals(key)) {
					res = str.trim();
					break;
				}
			}
		}
		if (res == null) {
			Scanner scanData = new Scanner(new File(dataFileName));
			while (scanData.hasNextLine()) {
				String str = scanData.nextLine();
				Scanner scanStr = new Scanner(str);
				String keyList[] = new String[4];
				int i = 0;
				
				while(scanStr.hasNext()&&i<4) {
					keyList[i] = scanStr.next();
				}
				scanStr.close();
				String s = "";
				for (int j = 0; j < 3; j++) {
					s += keyList[j]+" ";
				}
				s += keyList[3];
				if (s.equals(key)) {
					res = str.trim();
					break;
				}
			}
		}
		
		return res;
	}
}
