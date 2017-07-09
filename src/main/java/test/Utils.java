
package test;

import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public abstract class Utils {
    
    public static final String NO_VALUE                    = "Отсутствует имя пользователя или фраза.";
    public static final String PARSE_ERROR            = "Неверный формат json.";
    public static final String NOT_PALINDROME = "Данная фраза не является палиндром.";
    public static final String REPEAT                         = "Данный палиндром уже использовался.";
        
    public static Pair<String, String> getUserPhrasePair(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(jsonString);
         String userName = (String) json.get("userName");
         String phrase = (String) json.get("phrase");
         if(userName == null || phrase == null)
             return null;
         else
             return  new Pair(userName, phrase);
    }
    
    public static String checkPalindrome(String phrase) {
        String direct = phrase.toLowerCase().replaceAll(" ", "");
        String reverse = new StringBuffer(direct).reverse().toString();
        if(direct.equals(reverse))
            return direct;
        else
            return null;
    }
    
    public static String scoreToJsonString(int score) {
        JSONObject json = new JSONObject();
        json.put("score", score);
        return json.toJSONString();
    }

    
    public static String scoreWithErrorToJsonString(int score, String error) {
        JSONObject json = new JSONObject();
        json.put("score", score);
        json.put("error", error);
        return json.toJSONString();
    }
    
    public static String topToJsonString(Map<String, Integer> top) {
       JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        top.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).forEach((item) -> {
            JSONObject pair = new JSONObject();
            pair.put("userName", item.getKey());
            pair.put("score"          , item.getValue());
            array.add(pair);
      });
        json.put("top", array);
    
       return json.toJSONString();
    }
    
}
