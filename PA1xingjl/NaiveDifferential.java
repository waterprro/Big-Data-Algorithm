/**
 * 
 */
package bloomFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * @author jialin
 *
 */
public class NaiveDifferential {
	
	private String dataFileName;
	
	private String diffFileName;
	
	private Hashtable<String, String> diff;
	
	public NaiveDifferential(String dataFileName, String diffFileName) throws FileNotFoundException {
		this.dataFileName = dataFileName;
		this.diffFileName = diffFileName;
		
		// store records in a hash table
		diff = new Hashtable<String, String>();
		Scanner scan = new Scanner(new File(this.diffFileName));
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			Scanner scanline = new Scanner(line);
			String key = "";
			String value = "";
			int i = 0;
			
			while (scanline.hasNext()) {
				if (i < 4) {
					key += scanline.next()+ " ";
				}
				else {
					value += scanline.next()+ " ";
				}
				i++;
			}
			scanline.close();
			diff.put(key.trim(), value.trim());
		}
		scan.close();
	}
	
	public String retrieveRecord(String key) throws FileNotFoundException {
		String res = null;
		if (diff.containsKey(key)) {
			res = key + " "+diff.get(key);
		}
		else {
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
