package test;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public abstract class Utils {

  public static final String NO_VALUE = "Отсутствует имя пользователя или фраза.";
  public static final String PARSE_ERROR = "Неверный формат json.";
  public static final String NOT_PALINDROME = "Данная фраза не является палиндром.";
  public static final String REPEAT = "Данный палиндром уже использовался.";

  public static String checkPalindromeAndGetResult(String jsonString, IDataProcessing dataProcessing) {
    String result;
    Pair<String, String> pair;
    
    try {
      pair = getUserPhrasePair(jsonString);
    }
    catch (ParseException ex) {
      Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
      return PARSE_ERROR;
    }

    if (pair == null) return NO_VALUE;
     
    String palindrome = checkPalindrome(pair.phrase);
    if (palindrome == null) 
      return scoreWithErrorToJsonString(dataProcessing.getScore(pair.userName),
              NOT_PALINDROME);
    
    if (dataProcessing.savePalindrome(pair.userName, palindrome))
      return scoreToJsonString(dataProcessing.addScore(pair.userName, palindrome.length()));
    
    
    return scoreWithErrorToJsonString(dataProcessing.getScore(pair.userName),
            REPEAT);
  }

  public static Pair<String, String> getUserPhrasePair(String jsonString) throws ParseException {
    JSONParser parser = new JSONParser();
    JSONObject json = (JSONObject) parser.parse(jsonString);
    String userName = (String) json.get("userName");
    String phrase = (String) json.get("phrase");

    return (userName == null || phrase == null)
            ? null : new Pair(userName, phrase);
  }

  public static String checkPalindrome(String phrase) {
    String direct = phrase.toLowerCase().replaceAll(" ", "");
    String reverse = new StringBuffer(direct).reverse().toString();

    return direct.equals(reverse) ? direct : null;
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

  public static String getTopUsers(IDataProcessing dataProcessing) {
    Map<String, Integer> top = dataProcessing.getTopUsers();
    JSONObject json = new JSONObject();
    JSONArray array = new JSONArray();
    top.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).forEach((item) -> {
      JSONObject pair = new JSONObject();
      pair.put("userName", item.getKey());
      pair.put("score", item.getValue());
      array.add(pair);
    });
    json.put("top", array);

    return json.toJSONString();
  }

}
