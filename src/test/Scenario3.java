import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static Utils.Constants.BASE_PATH;
import static io.restassured.RestAssured.given;

public class Scenario3 {

    HashMap<String, ArrayList<Double>> fiveHighestValues;
    HashMap<String, ArrayList<String>> fiveHighestRegions;

    @Test
    public void checkGenerationMixSum(){
        fiveHighestValues = initializeFiveHighestValues();
        fiveHighestRegions = initializeFiveHighestRegions();

        for(int regId = 1; regId <= 17; regId++){
            Response response = given().when().get(BASE_PATH + "/regional/regionid/" + regId).then()
                    .statusCode(200).extract().response();

            JsonPath json = response.jsonPath();
            ArrayList<Object> generationmix = getGenerationmix(json);
            String regionName = getRegionName(json);

            for(Object generationType : generationmix){
                String fuel = ((HashMap<String, Object>) generationType).get("fuel").toString();
                Object percentageObj = ((HashMap<String, Object>) generationType).get("perc");
                Double percent = ((Number) percentageObj).doubleValue();
                System.out.println(fuel +" "+ percent +" "+ regionName);
                updateFiveHighest(fuel, percent, regionName);
            }

        }
        System.out.println(fiveHighestRegions.toString());
    }

    public HashMap<String, ArrayList<Double>> initializeFiveHighestValues(){
        HashMap<String, ArrayList<Double>> map = new HashMap<String, ArrayList<Double>>(9);
        map.put("biomass", new ArrayList<Double>());
        map.put("coal", new ArrayList<Double>());
        map.put("imports", new ArrayList<Double>());
        map.put("gas", new ArrayList<Double>());
        map.put("nuclear", new ArrayList<Double>());
        map.put("other", new ArrayList<Double>());
        map.put("hydro", new ArrayList<Double>());
        map.put("solar", new ArrayList<Double>());
        map.put("wind", new ArrayList<Double>());
        return map;
    }

    public HashMap<String, ArrayList<String>> initializeFiveHighestRegions(){
        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>(9);
        map.put("biomass", new ArrayList<String>());
        map.put("coal", new ArrayList<String>());
        map.put("imports", new ArrayList<String>());
        map.put("gas", new ArrayList<String>());
        map.put("nuclear", new ArrayList<String>());
        map.put("other", new ArrayList<String>());
        map.put("hydro", new ArrayList<String>());
        map.put("solar", new ArrayList<String>());
        map.put("wind", new ArrayList<String>());
        return map;
    }

    public ArrayList<Object>  getGenerationmix(JsonPath json){
        ArrayList<String> data = json.get("data");
        Object data0 = data.get(0);
        ArrayList<Object> generationmixGP = (ArrayList<Object>)((HashMap<Object, Object>)data0).get("data");
        HashMap<String, Object> generationmixParent = (HashMap<String, Object>)generationmixGP.get(0);
        return (ArrayList<Object>) generationmixParent.get("generationmix");
    }

    public String getRegionName(JsonPath json){
        ArrayList<String> data = json.get("data");
        Object data0 = data.get(0);
        return ((HashMap<Object, Object>)data0).get("shortname").toString();
    }

    public void updateFiveHighest(String fuel, Double percent, String regionName){
        ArrayList<Double> thisFuelHighestValues = fiveHighestValues.get(fuel);
        Double minBetweenMaxs = thisFuelHighestValues.size() == 0 ? null : Collections.min(thisFuelHighestValues);

        if (thisFuelHighestValues.size() < 5){
            thisFuelHighestValues.add(percent);
            fiveHighestRegions.get(fuel).add(regionName);
        } else if(minBetweenMaxs < percent){
            int minBetweenMaxsIdx = thisFuelHighestValues.indexOf(minBetweenMaxs);
            thisFuelHighestValues.set(minBetweenMaxsIdx, minBetweenMaxs);
            fiveHighestRegions.get(fuel).set(minBetweenMaxsIdx, regionName);
        }

    }

}
