import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static Utils.Constants.BASE_PATH;

public class Scenario2 {

    double d = 0.0001;

    @Test
    public void checkGenerationMixSum(){
        for(int regId = 1; regId <= 17; regId++){
            Response response = given().when().get(BASE_PATH + "/regional/regionid/" + regId).then()
                    .statusCode(200).extract().response();

            JsonPath json = response.jsonPath();
            ArrayList<Object> generationmix = getGenerationMix(json);

            Double sum = 0.0;
            for(Object generationType : generationmix){
                Object percentageObj = ((HashMap<String, Object>) generationType).get("perc");
                sum += ((Number) percentageObj).doubleValue();
            }

            assert(sum < 100.0 + d && sum > 100.0 - d);
        }
    }

    public ArrayList<Object>  getGenerationMix(JsonPath json){
        ArrayList<String> data = json.get("data");
        Object data0 = data.get(0);
        ArrayList<Object> generationmixGP = (ArrayList<Object>)((HashMap<Object, Object>)data0).get("data");
        HashMap<String, Object> generationmixParent = (HashMap<String, Object>)generationmixGP.get(0);
        return (ArrayList<Object>) generationmixParent.get("generationmix");
    }

}
