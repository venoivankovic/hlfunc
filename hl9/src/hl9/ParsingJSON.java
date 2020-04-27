package hl9;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.*;

public class ParsingJSON {
	
	ArrayList<Bus> buses = new ArrayList<Bus>();
	Station station;
	ArrayList<Pass> passes = new ArrayList<Pass>();
	
	
	public ParsingJSON() {
		buses = new ArrayList<Bus>();
		station = new Station();
		passes = new ArrayList<Pass>();
	}
	
	//please ignore how horribly written the function below is, I will make it nicer
	
	public void parseJourneyString(Bus bus, String journeyString) throws JSONException {
		String requestString = bus.getDataOwnerCode()+"_"+ bus.getLocalServiceLevelCode()+"_"+bus.getLinePlanningNumber()+"_"+bus.getJourneyNumber()+"_"+bus.getFortifyOrderNumber();
		JSONObject fullResponse = new JSONObject(journeyString);
		//System.out.println(fullResponse.toString());
		JSONObject responseWithoutBusName = fullResponse.getJSONObject(requestString);
		//System.out.println(responseWithoutBusName.toString());
		JSONObject responseWithoutStops = responseWithoutBusName.getJSONObject("Stops");
		parsePassesFromJourney(responseWithoutStops);
		//System.out.println(responseWithoutStops.toString());
	}
	
	public void parsePassesFromJourney(JSONObject passesJSON)throws JSONException {
		JSONArray passNames = passesJSON.names();
		for (int i = 0; i < passNames.length(); i++) {
			Pass pass = new Pass();
			JSONObject passJSON = passesJSON.getJSONObject(passNames.get(i).toString());
			pass.setExpectedArrivalTime(passJSON.getString("ExpectedArrivalTime"));
			pass.setExpectedDepartureTime(passJSON.getString("ExpectedDepartureTime"));
			pass.setFortifyOrderNumber(passJSON.getInt("FortifyOrderNumber"));
			pass.setLatitude(passJSON.getDouble("Latitude"));
			pass.setLongitude(passJSON.getDouble("Longitude"));
			pass.setTargetArrivalTime(passJSON.getString("TargetArrivalTime"));
			pass.setTargetDepartureTime(passJSON.getString("TargetDepartureTime"));
			pass.setTimingPointCode(passJSON.getString("TimingPointCode"));
			pass.setLocalServiceLevelCode(passJSON.getInt("LocalServiceLevelCode"));
			pass.setLineDirection(passJSON.getDouble("LineDirection"));
			pass.setLinePlanningNumber(passJSON.getString("LinePlanningNumber"));
			pass.setJourneyNumber(passJSON.getInt("JourneyNumber"));
			pass.setTimingStop(passJSON.getBoolean("IsTimingStop"));
			pass.setLineName(passJSON.getString("LineName"));
			pass.setLinePublicNumber(passJSON.getString("LinePublicNumber"));
			
			pass.setTimingPointName(passJSON.getString("TimingPointName"));
			pass.setTimingPointTown(passJSON.getString("TimingPointTown"));
			pass.setTimingPointDataOwner(passJSON.getString("TimingPointDataOwnerCode"));
			pass.setJourneyStopType(passJSON.getString("JourneyStopType"));
			pass.setTripStopStatus(passJSON.getString("TripStopStatus"));
			pass.setUserStopCode(passJSON.getString("UserStopCode"));
			pass.setTransportType(passJSON.getString("TransportType"));
			pass.setOrderInLine(passNames.getInt(i));
			
			pass.setPassMetadata(passJSON.toString());
			passes.add(pass);
			//Collections.sort(buses);
			//Collections.sort(buses.get(i).getExpectedArrivalTimeAsTime());
		}
		Collections.sort(passes);
		for (int i = 0; i < passes.size(); i++) {
			Pass pass = passes.get(i);
			System.out.println(pass.getOrderInLine()+" "+pass.getTimingPointName()+" "+pass.getTripStopStatus()+" "+pass.getExpectedDepartureTimeAsTime().toString());
		}
	}
	
	public void parseTimingPointCodeString(String tpcString, String tpc) throws JSONException {
		JSONObject fullResponse = new JSONObject(tpcString);
		//System.out.println(fullResponse.toString());
		JSONObject responseWithoutTcp = fullResponse.getJSONObject(tpc);
		//System.out.println(responseWithoutTcp.toString());
		JSONObject stationJSON = responseWithoutTcp.getJSONObject("Stop");
		parseStationFromTpc(stationJSON);
		JSONObject busesJSON = responseWithoutTcp.getJSONObject("Passes");
		parseBusesFromTpc(busesJSON);
		JSONObject generalMessages = responseWithoutTcp.getJSONObject("GeneralMessages");
		//System.out.println(generalMessages);
	}
	
	public void parseBusesFromTpc(JSONObject busesJSON) throws JSONException {
		JSONArray busNames = busesJSON.names();
		//System.out.println(busNames.get(0));
		for (int i = 0; i < busNames.length(); i++) {
			Bus bus = new Bus();
			JSONObject busJSON = busesJSON.getJSONObject(busNames.get(i).toString());
			bus.setDataOwnerCode(busJSON.getString("DataOwnerCode"));
			bus.setExpectedArrivalTime(busJSON.getString("ExpectedArrivalTime"));
			bus.setExpectedDepartureTime(busJSON.getString("ExpectedDepartureTime"));
			bus.setFortifyOrderNumber(busJSON.getInt("FortifyOrderNumber"));
			bus.setLatitude(busJSON.getDouble("Latitude"));
			bus.setLongitude(busJSON.getDouble("Longitude"));
			bus.setTargetArrivalTime(busJSON.getString("TargetArrivalTime"));
			bus.setTargetDepartureTime(busJSON.getString("TargetDepartureTime"));
			bus.setTimingPointCode(busJSON.getString("TimingPointCode"));
			bus.setLocalServiceLevelCode(busJSON.getInt("LocalServiceLevelCode"));
			bus.setLineDirection(busJSON.getDouble("LineDirection"));
			bus.setLinePlanningNumber(busJSON.getString("LinePlanningNumber"));
			bus.setJourneyNumber(busJSON.getInt("JourneyNumber"));
			bus.setTimingStop(busJSON.getBoolean("IsTimingStop"));
			bus.setLineName(busJSON.getString("LineName"));
			bus.setLinePublicNumber(busJSON.getString("LinePublicNumber"));
			bus.setBus_metadata(busJSON.toString());
			buses.add(bus);
			//Collections.sort(buses);
			//Collections.sort(buses.get(i).getExpectedArrivalTimeAsTime());
		}
		Collections.sort(buses);
		//System.out.println(buses.size());
		//System.out.println("here"+buses.get(0).getLongitude());
	}
	
	public void parseStationFromTpc (JSONObject stationJSON) throws JSONException {
		station.setLatitude(stationJSON.getDouble("Latitude"));
		station.setLongitude(stationJSON.getDouble("Longitude"));
		station.setTimingPointCode(stationJSON.getString("TimingPointCode"));
		station.setTimingPointName(stationJSON.getString("TimingPointName"));
		station.setStationMetadata(stationJSON.toString());
	}

}
