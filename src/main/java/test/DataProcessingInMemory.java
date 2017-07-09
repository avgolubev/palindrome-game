
package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DataProcessingInMemory implements IDataProcessing {

    public final ConcurrentMap<String, Integer> SCORE_MAP = new ConcurrentHashMap<>();
    public final ConcurrentMap<String, List> PHRASE_MAP = new ConcurrentHashMap<>();

    @Override
    public boolean savePalindrome(String userName, String phrase) {
        synchronized (PHRASE_MAP) {
            List<String> phraseList = PHRASE_MAP.getOrDefault(userName, new ArrayList());
            PHRASE_MAP.putIfAbsent(userName, phraseList);

            if (phraseList.contains(phrase)) {
                return false;
            } else {
                phraseList.add(phrase);
                return true;
            }
        }
    }

    @Override
    public int addScore(String userName, int score) {
        return SCORE_MAP.compute(userName, (k, v) -> v == null ? score : v + score);
    }
    
    @Override
    public int getScore(String userName) {
        return SCORE_MAP.getOrDefault(userName, 0);
    }
    
    @Override
    public Map getTopUsers() {
        Map<String, Integer> map = new HashMap<>();
        synchronized (SCORE_MAP) {
            SCORE_MAP.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(5)
                    .forEach(e -> map.put(e.getKey(), e.getValue()));
        }
        return map;
    }
    
}
