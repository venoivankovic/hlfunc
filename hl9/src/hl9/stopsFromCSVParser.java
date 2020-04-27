package hl9;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/*
 * Maybe could also implement searching of stops
 * list by GPS and name here 
 * (for name maybe stemming and lemming 
 * search keyword)
 * also autocomplete on name search is necessary
 * Essentially the functionality shown
 * here: https://drgl.nl/
 */

public class stopsFromCSVParser {
	static ArrayList<Stop> stops;
	
	public stopsFromCSVParser() {
		stops = new ArrayList<Stop>();
	}
	
	public static ArrayList<Stop> firstParseStops() throws IOException{
		String csvFile = "stops.csv";
	    BufferedReader br = null;
	    String line = "";
	    try {
	    	br = new BufferedReader(new FileReader(csvFile));
	    	line = br.readLine();
	    	while(true) {
	    		line = br.readLine();
	    		if(line == null) {
	    			break;
	    		}
	    		Stop stop = new Stop();
	    		String [] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
	    		stop.setStop_id(values[0]);
	    		stop.setStop_code(values[1]);
	    		stop.setStop_name(values[2]);
	    		stop.setStop_lat(values[3]);
	    		stop.setStop_lon(values[4]);
	    		stop.setStop_metadata(values[5]);
	    		for (int i = 5; i < values.length; i++) {
	    			stop.setStop_metadata(values[i]);
				}
	    		stops.add(stop);
	    	}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return stops;
	}

}