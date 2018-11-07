import java.util.Map;

import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class Scenario1 {

    private static final String BASE_PATH = "http://api.carbonintensity.org.uk";

    @Test
    public void getCarbonIntensityEachRegion(){
        Map<String, String> forecasts;
        Double sum = 0.0;
        for (int regId = 1; regId <= 17; regId++) {
            Response response = given().when().get(BASE_PATH + "/regional/regionid/" + regId).then()
                                .statusCode(200).extract().response();
            // String key = data[0].regions[0].regionid
            // String value = data[0].regions[0].intensity.forecast
            // forecasts.put(key, value);
            // Collections.sort(forecasts, sorter)
            // Double carbon;
            // for(String generator: data[0].regions[0].generationmix){
            //      if("coal".equals(generator.fuel)
            //          sum += generator.perc
            // ]
        }
        // assertEquals(100 == sum)
    }

    // class sorter

}
