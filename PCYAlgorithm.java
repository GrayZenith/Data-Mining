import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
//int dataSize = 8800; 
public class PCYAlgorithm {
	public static void main(String[] args) throws Exception {	
	    
	    //Variable to store name of textfile.
		String fname = "retail.txt";	
		
		//Declaration of Variables used for calculating Run time.	
		long startTime, endTime, runTime;
		startTime = System.currentTimeMillis();	//store the starting time		
		
        //Mapping used to map the elements in the first pass
		Map<Integer, Integer> firstPassMap = new HashMap<>();
		//Mapping used to map the elements in the second pass
		Map<String, Integer> secondPassMap = new HashMap<>();
		
		//Mapping used to map the elements in the bucket for PCY
		Map<Integer,Integer> bucketCounter = new HashMap<Integer, Integer>();
		Set<Integer> bucketFrequency = new HashSet<Integer>();
		
		//Initailising Variable named dataSize of type int to store the maximum data size allowed	
		int dataSize = 8800; 
        //Initailising variable named supThreshold to store the minimum Support value
		int supThreshold = (int) (dataSize*0.01);		

		// Begining of the FIRST Pass (Same as Apriori Algorithm.)
		BufferedReader reader = new BufferedReader(new FileReader(fname));
		String line = reader.readLine();     //Start reading the file line by line
		for(int i=0; i<dataSize; i++) {		 //Iterate until we reach the maximum data size	
			if(!line.trim().equals(" ")) {        // seperate out the data values and
				String[] values = line.split(" ");	// store in string array called values			
				
				for(String word : values) {        // Iterate through the values ignore the word 
					if(word == null || word.trim().equals("")) {    // if null or space
						continue;
					}

					Integer nKey = Integer.parseInt(word);	 // else we create a key for the "word"				
					if(firstPassMap.containsKey(nKey)) {
						firstPassMap.put(nKey, firstPassMap.get(nKey)+1);  //if the map contains n key then store it in the map
					}else {
						firstPassMap.put(nKey, 1); //We put 1 since first time seeing this value
					}
				}

				for(int k=0; k<values.length; k++) {  // Iterate through the array values
					for(int c=k+1; c<values.length; c++) {
						Integer item1 = Integer.parseInt(values[k].trim());  //Convert both strings at c and k position
						Integer item2 = Integer.parseInt(values[c].trim());   // to Item type
						Integer hashed = hashingFunction(item1, item2);
						if(bucketCounter.containsKey(hashed)) {      // If the second pass map contains the key
							bucketCounter.put(hashed, bucketCounter.get(hashed)+1);   //store it in the map
						}else {
							bucketCounter.put(hashed, 1); //or put 1 since first time seeing this value
						}
					}
				}
			}
			line = reader.readLine();
		}
		//loop which counts the number of frequency set after FirstPass
		for(int itemNumber : firstPassMap.keySet()) {  //iterate through the first pass Map
			int count = firstPassMap.get(itemNumber);   //check if value is greater than the support threshhold
			if(count > supThreshold)
				bucketFrequency.add(itemNumber); //Add to the bucket
		}
	
		
		//Begining of the SECOND Pass
		reader = new BufferedReader(new FileReader(fname));	
		line = reader.readLine();

		for(int i=0; i <dataSize; i ++) {	//Iterate until we reach the maximum data size			
			if(!line.trim().equals(" ")) {    // seperate out the data values and
				String[] values = line.split(" ");		 // store in string array called values		
				for(int j=0; j<values.length; j++) {       //Go through the strings pair at a time
					
					for(int k=j+1; k<values.length; k++) {
						Integer item1 = Integer.parseInt(values[j].trim());  //Convert both to Item type
						Integer item2 = Integer.parseInt(values[k].trim());
						
						if(bucketFrequency.contains(item1)  && bucketFrequency.contains(item2) ) {
							String key = item1 + "*" + item2;   //Concatenate the two items
							if(secondPassMap.containsKey(key)) {    // If the second pass map contains the key
								secondPassMap.put(key, secondPassMap.get(key)+1);   //store it in the map
							}else {
								secondPassMap.put(key, 1); //or put 1 since first time seeing this value
							}
						}						
					}
				}				
			}
			line = reader.readLine();
		}	
		
		Set<String> dFreq = new HashSet<String>();
		//loop which counts the number of frequency set after SecondPass
		for(String key : secondPassMap.keySet()) {   //iterate through the first pass Map
			int count = secondPassMap.get(key);
			if(count > supThreshold)         //check if value is greater than the support threshhold
				dFreq.add(key);
		}

		//Displaying the size of the dFrequency Set			
		System.out.println("The number of frequencies after the second pass (dFreq): " + dFreq.size());
		
		//Getting the end time
		endTime = System.currentTimeMillis();	
		//Calculating the total run time	
		runTime = endTime - startTime;
		//Displaying RunTime
		System.out.println("The run time is : " + runTime);		
		return;		
	}
	// Defining the Integer Hash Function
	public static Integer hashingFunction(Integer x, Integer y) {
		Integer hashed = (x+y)%1000;    // We choose to use the formula (x+y) % 1000
		return hashed;
	}
}
