package test


trait  DataProcessing {
  type OldScore = Int
  type NewScore = Int
  
  def savePalindrome(userName: String, phrase: String): Either[OldScore, NewScore]

  def getScore(userName: String): Int

  def getUsersAndScores: Map[String, Int]
  
}

