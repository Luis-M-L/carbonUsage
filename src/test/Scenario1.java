import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class Scenario1 {

    private static final String BASE_PATH = "http://api.carbonintensity.org.uk";

    @Test
    public void getCarbonIntensityEachRegion(){
        Map<Integer, Integer> forecasts = new HashMap<Integer, Integer>();
        Double sum = 0.0;
        for (int regId = 1; regId <= 17; regId++) {
            Response response = given().when().get(BASE_PATH + "/regional/regionid/" + regId).then()
                                .statusCode(200).extract().response();

            JsonPath json = response.jsonPath();

            int key = regId;
            int value = extractForecast(json);
            forecasts.put(key, value);
            // Collections.sort(forecasts, sorter)
            // Double carbon;
            // for(String generator: data[0].regions[0].generationmix){
            //      if("coal".equals(generator.fuel)
            //          sum += generator.perc
            // ]
        }
        System.out.println(forecasts.toString());
        // assertEquals(100 == sum)
    }

    public int extractForecast(JsonPath json){
        ArrayList<String> data = json.get("data");
        Object data0 = data.get(0);
        Object regionData = ((HashMap<Object, Object>)data0).get("data");
        Object regionData0 = ((List)regionData).get(0);
        Object intensity = ((HashMap<Object, Object>)regionData0).get("intensity");
        Object forecast = ((HashMap<Object, Object>)intensity).get("forecast");
        return (Integer) forecast;
    }

    // class sorter

}
