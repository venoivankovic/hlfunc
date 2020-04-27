package hl9;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/*
 * API used: https://github.com/skywave/KV78Turbo-OVAPI/wiki/
 * 
 * Useful for live bus locations https://ovzoeker.nl/locatie/52.38629:4.86437:12
 * I wonder how they get their data
 */

public class OVapi {
	
	public OVapi() {
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * GET /tpc/$timingpointcode
	 * GET /tpc/$timingpointcode/departures
	 */
	
	public static String doRequestByTimeCodePoint(String tpc) 
			throws IOException, InterruptedException {
		//tpc = "57114550";
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().
				uri(URI.create("http://v0.ovapi.nl/tpc/"+tpc+"/departures")).build();

		HttpResponse<String> response = client.
				send(request, HttpResponse.BodyHandlers.ofString());

		//System.out.println(response.body());
		return response.body();
	}
	
	/*
	 * GET /line/$DataOwnerCode_$LinePlanningNumber_$LineDirection
	 */
	
	public static String doRequestByLine(String dataOwnerCode, String 
			linePlanningNumber, String lineDirection)
			throws IOException, InterruptedException{
		dataOwnerCode = "CXX";
		linePlanningNumber = "N391";
		lineDirection = "1";
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().
				uri(URI.create("http://v0.ovapi.nl/line/"+dataOwnerCode+"_"+linePlanningNumber+"_"+lineDirection)).build();

		HttpResponse<String> response = client.
				send(request, HttpResponse.BodyHandlers.ofString());

		//System.out.println(response.body());
		return response.body();
	}
	
	/*
	 * GET /journey/$DataOwnerCode_$LocalServiceLevelCode
	 * _$LinePlanningNumber_%JourneyNumber_$FortifyOrderNumber
	 */
	
	public static String doRequestByJourney(String dataOwnerCode, int localServiceLevelCode, 
			String linePlanningNumber, int journeyNumber, int fortifyOrderNumber)throws IOException, InterruptedException{
		String localServiceLevelCodeString = Integer.toString(localServiceLevelCode);
		String journeyNumberString = Integer.toString(journeyNumber);
		String fortifyOrderNumberString = Integer.toString(fortifyOrderNumber);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().
				uri(URI.create("http://v0.ovapi.nl/journey/"+dataOwnerCode+"_"+localServiceLevelCodeString+"_"+linePlanningNumber+"_"+journeyNumberString+"_"+fortifyOrderNumberString)).build();
		
		System.out.println(request);
		
		HttpResponse<String> response = client.
				send(request, HttpResponse.BodyHandlers.ofString());

		//System.out.println(response.body());
		System.out.println("here");
		return response.body();
	}
	
	
	//Maybe also add stopAreaCode if necessary
}
