import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;


public class DataFilter {
	ArrayList<Hashtable<String, Object>> data = new ArrayList<Hashtable<String, Object>>();
	
	
	public DataFilter(String inputFilePath, String outputFilePath) {
		readFile(inputFilePath);
		filterData();
		writeFile(outputFilePath);
		
		System.out.println(data);
	}
	
	
	private void readFile(String inputFilePath) {
		try {
			Scanner input = new Scanner(new File(inputFilePath));
			
			while(input.hasNextLine()) {
				// Get a line as a string from the scanner, and create a StringTokenizer
				// out of it. Use commas as the delimiter for the tokenizer, then add
				// the values to our collection.
				Hashtable<String, Object> sample = new Hashtable<String, Object>();
				StringTokenizer tok = new StringTokenizer(input.nextLine(), ",");
				sample.put("timestamp", new Integer(tok.nextToken()));
				sample.put("xAxis", new Integer(tok.nextToken()));
				sample.put("yAxis", new Integer(tok.nextToken()));
				data.add(sample);
			}
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Oh noes!\n\n" + e + ":\n" + e.getMessage());
		}
	}
	
	
	// For a very basic start, we'll just take an average of a few (maybe 5?)
	// samples and plop that back into the collection and delete the others. 
	private void filterData() {
		// We're going to do this the ugly way and create a temporary 
		// collection in memory that we'll swap references with our member
		// variable "data" when we're done. This can eat tons of memory.
		ArrayList<Hashtable<String, Object>> tmpData = new ArrayList<Hashtable<String, Object>>();
		int avgSize = 5;
		
		for(int i = 0; i < data.size(); i+=avgSize+1) {
			int xAxisTotal = 0;
			int yAxisTotal = 0;
			Hashtable<String, Object> tmpSample = new Hashtable<String, Object>();
			
			for(int j = 0; j < avgSize && i+j < data.size(); j++) {
				xAxisTotal += new Integer(data.get(i+j).get("xAxis").toString());
				yAxisTotal += new Integer(data.get(i+j).get("yAxis").toString());
			}
			
			tmpSample.put("timestamp", data.get(i).get("timestamp"));
			tmpSample.put("xAxis", xAxisTotal / avgSize);
			tmpSample.put("yAxis", yAxisTotal / avgSize);
			
			tmpData.add(tmpSample);
		}
		
		data = tmpData;
	}
	
	
	// Write the values from the "data" collection to a file in CSV format.
	private void writeFile(String outputFilePath) {
		try {
			FileWriter output = new FileWriter(outputFilePath);
			
			for(int i = 0; i < data.size(); i++) {
				String tmpStr = "";
				tmpStr += data.get(i).get("timestamp") + ",";
				tmpStr += data.get(i).get("xAxis") + ",";
				tmpStr += data.get(i).get("yAxis") + "\n";
				
				System.out.print(tmpStr);
				output.write(tmpStr);
			}
			
			output.flush();
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Oh noes!!\n\n" + e + ":\n" + e.getMessage());
		}
	}

	
	public static void main(String[] args) {
		DataFilter app = new DataFilter(args[0], args[1]);
	}

	
}
