import java.util.*;
import java.io.*;

public class Driver {
	public static void main(String[] args) {
		ArrayList<String> list = readFile(new File("StressTest.txt")); //StressTest.txt
		HashedMap<String,Integer> hashMap = new HashedMap<String,Integer>();
		for (String s:list) {
			Integer count = (Integer)hashMap.get(s); //Returns null when doesnt Exist
			if (count==null) {count=0;} //Init for case ^
			hashMap.put(s,++count);
		}
		//Set<String> keys = hashMap.keySet();
		//for (String key:keys) {System.out.println(hashMap.size()+" Removed "+key+"-"+hashMap.remove(key)+"\n");}
		System.out.println(hashMap); 
		System.out.println("TOTAL SIZE: "+hashMap.size()+"/"+hashMap.capacity());
	}
	public static ArrayList readFile(File f) {
		try{
			BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
			ArrayList<String> returnList = new ArrayList<String>();
			String line;
			while((line=bufferedReader.readLine())!=null) {returnList.add(line);}
			bufferedReader.close();
			return returnList;
		} catch(IOException e) {return null;}
	}
}
