package Util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.*;


/**
 * Created by randyjp on 11/14/15.
 */
public class JsonUtils {

    private JSONArray ja;
    private static String OUTPUT = "src/data/output.json";
    private String[] imgs = {"http://www.as-architecture.com/data/projet/projet_fiche/504/large_barfly_045af.jpg",
            "http://www.restaurant-sanfrancisco.com/sites/default/files/styles/home_article/public/field/image/Restaurant_SanFrancisco_Paris_16e_Gastronomique_Italien_1.png",
            "http://www.vathorst.nl/wp-content/uploads/20131025-_DSF4855-320x150.jpg",
            "http://www.charlesanthonysatthepub.com/ordereze/images/items/IMAGE26.JPG",
            "http://www.lacartes.com/images/business/235164/574031/sm/771875.jpg",
            "http://4.bp.blogspot.com/-oRiYeT8Ehmw/T88Wzdo4HQI/AAAAAAAAAic/Njhvnu3RmN4/s320/360Istanbul+1.0Home.png",
            "http://www.qldtravel.com.au/images/providers/accommodation/hamilton-island-resort/dining/pool-terrace1.jpg",
            "http://www.ihg.com/hotelmedia/repository/hotelimages/LCHTT/RSTLN_REST_4_D.jpg",
            "http://men.myedmondsnews.netdna-cdn.com/wp-content/uploads/2014/04/Copper-Pot-Indian-Bar-Grill-1024x6821-320x150.jpg"};

    public JsonUtils() {
        this.ja = new JSONArray();
    }

    public void addJsonObject(String name, double rating, int commennts, String category) {
        int rnd = new Random().nextInt(imgs.length);
        JSONObject rest = new JSONObject();
        rest.put("name", name);
        rest.put("rating", new Double(rating));
        rest.put("comments", new Integer(commennts));
        rest.put("category", category);
        rest.put("img", imgs[rnd]);


        this.ja.add(rest);
    }

    public void createJsonFile() {
        try {
            FileUtils.writeFile(OUTPUT, this.ja.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
