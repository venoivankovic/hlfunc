package hl9;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Scanner;
//import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.json.JSONException;

public class Main {
	static double LATITUDE = 52.3213251;
	static double LONGITUDE = 4.9530824;
	static int N_OF_CLOSEST_STOPS = 100;
	static Person me;
	ArrayList<Stop> stops;

	public static void main(String[] args) throws IOException, InterruptedException, JSONException {
		me = new Person();
		me.setLatitude(LATITUDE);
		me.setLongitude(LONGITUDE);
		//searchForStation();
		//System.out.println(stops.size());
		//System.out.println(stops.get(2043).stop_name);
		showBusesOnStation();
	}
	
	public static void searchForStation()throws IOException, InterruptedException, JSONException {
		ArrayList<Stop> stops = parseStops();
		System.out.println("Welcome to the catch the bus demo!");
		System.out.println("First you must pick a station.");
		System.out.println("To search with a string, type s.");
		System.out.println("To search based on your location, type l. (Your location can be changed with the LATITUDE and LONGITUDE constants in this class.)");
		Scanner sc = new Scanner(System.in);
		String s = sc.nextLine();
		if(s.equals("s")) {
			ArrayList<Stop> searchStringStops = getSearchStringStops(stops);
			//some station selected
			showBusesOnStation(); //some code sent in, either tpc or stopareacode
		}
		else if (s.equals("l")) {
			ArrayList<Stop> closestStops = getNClosestStops(stops);
			showBusesOnStation(); //some code sent in, either tpc or stopareacode
		}
		else {
			System.out.println("End of program.");
			System.exit(0);
		}
	}
	
	public static ArrayList<Stop> getSearchStringStops(ArrayList<Stop> parsedStops){
		System.out.println("Please type in your search query (name of station):");
		Scanner sc = new Scanner(System.in);
		String query = sc.nextLine();
		sc.close();
		ArrayList<Stop> stops = new ArrayList<Stop>();
		for (int i = 0; i < parsedStops.size(); i++) {
			Stop currentStop = parsedStops.get(i);		
			if (containsIgnoreCase(currentStop.getStop_name(), query)) {
				stops.add(currentStop);
			}
		}
		for (int i = 0; i < stops.size(); i++) {
			System.out.println(i+" "+stops.get(i).getStop_name());
		}
		return stops;
	}
	
	public static ArrayList<Stop> parseStops() throws IOException {
		ArrayList<Stop> stops = new ArrayList<Stop>();
		stopsFromCSVParser stops2 = new stopsFromCSVParser();
		stops = stops2.firstParseStops();
		return stops;
	}
	
	/*
	 * Naive/Temporary approach, go through list one by one and find closest stops
	 */
	public static ArrayList<Stop> getNClosestStops(ArrayList<Stop> parsedStops) throws IOException {
		ArrayList<Stop> stops = new ArrayList<Stop>();
		Stop dummyStop = new Stop();
		dummyStop.setDistance(50000000);
		for (int i = 0; i < N_OF_CLOSEST_STOPS; i++) {
			stops.add(dummyStop);
		}
		double dist = 0;
		for (int i = 0; i < parsedStops.size(); i++) { //For test run change this to 100
			Stop currentStop = parsedStops.get(i);
			//System.out.println(currentStop.getStop_name());
			dist = distance(me.getLatitude(), me.getLongitude(), currentStop.get_Latitude(), currentStop.get_Longitude());
			currentStop.setDistance(dist);
			//System.out.println(currentStop.getStop_name()+" "+currentStop.getDistance());
			Stop lastStop = stops.get(N_OF_CLOSEST_STOPS-1);
			//System.out.println(lastStop.getStop_name()+" "+lastStop.getDistance());
			if(dist<=lastStop.getDistance()) {
				stops = getStops(currentStop, stops);
			}
		}
		for (int i = 0; i < stops.size(); i++) {
			System.out.println(i+" "+stops.get(i).stop_name +" "+ stops.get(i).getDistance());
		}
		return stops;
	}
	
	public static ArrayList<Stop> getStops(Stop currentStop, ArrayList<Stop> stops) {
		for (int j = 0; j < N_OF_CLOSEST_STOPS; j++) {
			Stop listStop = stops.get(j);
			if(currentStop.getDistance()<listStop.getDistance()) {
				stops.add(j, currentStop);
				stops.remove(N_OF_CLOSEST_STOPS);
				return stops;
			}
		}
		//for (int i = 0; i < stops.size(); i++) {
			//System.out.println(stops.get(i).stop_name + " "+ stops.get(i).getDistance());
		//}
		return stops;
	}
	
	public static void getSelectedBusInfo(Station station,Bus bus) throws IOException, InterruptedException, JSONException {
		//check and parse journey?
		//after bus is selected
		//call this every few secs
		//test(station, bus);
		ParsingJSON parseTheJSON = new ParsingJSON();
		System.out.println(bus.getDataOwnerCode()+"_"+ bus.getLocalServiceLevelCode()+"_"+bus.getLinePlanningNumber()+"_"+bus.getJourneyNumber()+"_"+bus.getFortifyOrderNumber());
		String JSONjrnToParse = OVapi.doRequestByJourney(bus.getDataOwnerCode(), bus.getLocalServiceLevelCode(), bus.getLinePlanningNumber(), bus.getJourneyNumber(), bus.getFortifyOrderNumber());		
		//System.out.println(JSONjrnToParse);
		//System.out.println(JSONjrnToParse);
		parseTheJSON.parseJourneyString(bus,JSONjrnToParse);
		
	}
	
	

	public static void showBusesOnStation() throws IOException, InterruptedException, JSONException {
		// Let's assume the user selected a bus station either through his GPS location
		// or through search
		// In both cases the timingPointCode of the station is returned
		String tpc = "30000114";
		ParsingJSON parseTheJSON = new ParsingJSON();
		String JSONtpcToParse = OVapi.doRequestByTimeCodePoint(tpc);
		System.out.println(JSONtpcToParse);
		parseTheJSON.parseTimingPointCodeString(JSONtpcToParse, tpc);
		Station station = parseTheJSON.station;
		System.out.println(station.getTimingPointName() +" "+ station.getLatitude() +" "+ station.getLongitude());
		ArrayList<Bus> buses = parseTheJSON.buses;
		
		for (int i = 0; i < buses.size(); i++) {
			Bus aBus = buses.get(i);
			System.out.println(i +" #"+ aBus.getLinePublicNumber() +"  "+  aBus.getLineName() + "  "+ aBus.getExpectedDepartureTimeAsTimeString());
		}
		System.out.println("\nPick my bus");
		Scanner sc = new Scanner(System.in);
		String s = sc.nextLine();
		int result = Integer.parseInt(s);
		
		Bus arbitrarilyChosenBus = buses.get(result);
		getSelectedBusInfo(station,arbitrarilyChosenBus);
		/*
		 * So at this point we have a station with some info (GPS is most important
		 * here), we had that from the stops csv too but since the tpc request returns a
		 * station it is included
		 * 
		 * We also have a much more useful arraylist of bus objects from which the user
		 * would choose his bus
		 */
	}
	
	/*
	 * Do request by journey after a bus is chosen every 15 seconds, i.e. call api request every 15 seconds
	 */
	
	

	public static void test(Station station, Bus bus) {
		Person me = new Person();
		me.setLatitude(LATITUDE);
		me.setLongitude(LONGITUDE);
		
		//System.out.println(station.getLatitude());
		//System.out.println(station.getLongitude());
		
		double dist = distance(me.getLatitude(), me.getLongitude(), station.getLatitude(), station.getLongitude());
		System.out.println("Distance between me and station is: "+dist+" meters");
		
		//System.out.println("Distance between bus and station is: " + distance(bus.getLatitude(), bus.getLongitude(), station.getLatitude(), station.getLongitude())+" meters");
		
		//OK so the api does not give the location of the bus but of the station
		
		//System.out.println("The target arrival time is: " + bus.getTargetArrivalTimeAsTimeString());
		//System.out.println("The target departure time is: " + bus.getTargetDepartureTimeAsTimeString());
		//System.out.println("The expected arrival time is: " + bus.getExpectedArrivalTimeAsTimeString());
		
		System.out.println("The expected departure time is: " + bus.getExpectedDepartureTimeAsTimeString());
		long diff = calculateTimeDifference(bus.expectedDepartureTimeAsTime);
		System.out.println("Time to get to station is: " + diff + " seconds.");
		hapticFeedback(dist, diff);
	}
	
	public static void hapticFeedback(double dist, long diff) {
		String decision = "";
		double speed = dist/diff;
		System.out.println("You need a speed of: " + speed + " m/s to get to the bus on time.");
		if(speed < 0.01) {
			decision = "Sleep, relax, you have time";
		}
		else if(speed >= 0.01 && speed < 0.1) {
			decision = "Wake up, eat, take shower";
		}
		else if(speed >= 0.1 && speed < 0.8) {
			decision = "Get ready";
		}
		else if(speed >= 0.8 && speed < 1.2) {
			decision = "Start heading";
		}
		else if(speed >= 1.2 && speed < 2) {
			decision = "Hurry up";
		}
		else if(speed >= 2  && speed < 3.7) {
			decision = "RUN!";
		}
		else if(speed >= 3.7 && speed < 12.27) {
			decision = "SPRINT";
		}
		else if(speed >= 12.27) {
			decision = "You may or may not be late";
		}
		System.out.println(decision);
	}

	public static double distance(double lat1, double lon1, double lat2, double lon2) { 
		lon1 = Math.toRadians(lon1);
		lon2 = Math.toRadians(lon2);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);
		double dlon = lon2 - lon1;
		double dlat = lat2 - lat1;
		double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
		double c = 2 * Math.asin(Math.sqrt(a));
		double r = 6371;
		return (c * r)*1000;
	}
	
	public static long calculateTimeDifference(LocalDateTime exp) {
	    LocalDateTime now = LocalDateTime.now();
	    return ChronoUnit.SECONDS.between(now, exp);
	}
	
	public static boolean containsIgnoreCase(String str, String subString) {
        return str.toLowerCase().contains(subString.toLowerCase());
    }

}
