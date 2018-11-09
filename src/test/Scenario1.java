import java.util.*;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.logging.impl.SimpleLog;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class Scenario1 {

    private static final String BASE_PATH = "http://api.carbonintensity.org.uk";
    public final SimpleLog log = new SimpleLog("Region name: forecast value");

    @Test
    public void getCarbonIntensityEachRegion(){
        ArrayList<ArrayList<Integer>> forecasts = new ArrayList<ArrayList<Integer>>();
        ArrayList<String> names = new ArrayList<String>();
        Double sum = 0.0;
        for (int regId = 1; regId <= 17; regId++) {
            Response response = given().when().get(BASE_PATH + "/regional/regionid/" + regId).then()
                                .statusCode(200).extract().response();

            JsonPath json = response.jsonPath();

            addForecastAndShortname(json, regId, forecasts, names);
            // Double carbon;
            // for(String generator: data[0].regions[0].generationmix){
            //      if("coal".equals(generator.fuel)
            //          sum += generator.perc
            // ]
        }
        Collections.sort(forecasts, new Sorter());
        printOrderedForecasts(names, forecasts);
        // assertEquals(100 == sum)
    }

    public void addForecastAndShortname(JsonPath json, int regId, ArrayList<ArrayList<Integer>> forecasts, ArrayList<String> names){
        ArrayList<String> data = json.get("data");
        Object data0 = data.get(0);

        Object regionData = ((HashMap<Object, Object>)data0).get("data");
        Object regionData0 = ((List)regionData).get(0);
        Object intensity = ((HashMap<Object, Object>)regionData0).get("intensity");
        Object forecast = ((HashMap<Object, Object>)intensity).get("forecast");
        ArrayList<Integer> elem = new ArrayList<Integer>();
        elem.add(regId);
        elem.add((Integer) forecast);
        forecasts.add(elem);

        String shortname = ((HashMap<Object, Object>)data0).get("shortname").toString();
        names.add(shortname);
    }

    public void printOrderedForecasts(ArrayList<String> names, ArrayList<ArrayList<Integer>> forecasts){
        for(ArrayList<Integer> f : forecasts){
            log.info(names.get(f.get(0)-1) + ": " + f.get(1));
        }
    }

    private class Sorter implements Comparator<ArrayList<Integer>> {
        public int compare(ArrayList<Integer> a, ArrayList<Integer> b) {
            return a.get(1) <= b.get(1) ? 1 : -1;
        }
    }

}
