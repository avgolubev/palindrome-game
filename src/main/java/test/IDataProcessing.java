package test;

import java.util.Map;

public interface IDataProcessing {

  boolean savePalindrome(String userName, String phrase);

  int addScore(String userName, int score);

  int getScore(String userName);

  Map getTopUsers();
}
