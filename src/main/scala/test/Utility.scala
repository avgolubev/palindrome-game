package test

import java.util.logging.Level
import java.util.logging.Logger
import scala.util.{Success, Failure, Try}
import play.api.libs.json._

trait Utility {

  val NO_VALUE       = "Отсутствует имя пользователя или фраза."
  val PARSE_ERROR    = "Неверный формат json."
  val NOT_PALINDROME = "Данная фраза не является палиндром."
  val REPEAT         = "Данный палиндром уже использовался."

  def checkPalindromeAndGetResult(jsonString: String, dataProcessing: DataProcessing) = {
    
    Try(getUserPhrasePair(jsonString)) match {
      
      case Failure(_) => PARSE_ERROR
      
      case Success(None) => NO_VALUE 
      
      case Success(Some(pair)) =>  
        checkPalindrome(pair._2) match {      
          case Some(palindrome) => 
            dataProcessing.savePalindrome(pair._1, palindrome) match {              
              case Right(score) => scoreToJsonString(score)
              case Left(score) =>  scoreWithErrorToJsonString(score, REPEAT)
            }
              
          case None =>  
            scoreWithErrorToJsonString(dataProcessing.getScore(pair._1), NOT_PALINDROME)              
        }
    }
  }

  def getUserPhrasePair(jsonString: String): Option[(String, String)] = {
    val json = Json.parse(jsonString)
    
    ((json \ "userName").asOpt[String], (json \ "phrase").asOpt[String]) match {
      case (Some(userName), Some(phrase)) => Some(userName, phrase)
        
      case _ => None
    }         
  }

  def checkPalindrome(phrase: String) = {
    val direct = phrase.toLowerCase().replaceAll(" ", "")
    val reverse = direct.reverse

    if (direct == reverse)
      Some(direct)
    else
      None
  }

  def scoreToJsonString(score: Int) = {
    val json = Json.obj("score" -> score)
    Json.stringify(json)
  }

  def scoreWithErrorToJsonString(score: Int, error: String) = {
    val json = Json.obj(
      "score" -> score,
      "error" -> error)
    Json.stringify(json)
  }

  def getTopUsers(dataProcessing: DataProcessing) = {
    val usersAndScores = dataProcessing.getUsersAndScores
    println(usersAndScores)
    val json = Json.obj(
        "top" -> usersAndScores.toSeq.sortWith(_._2 > _._2).take(5)
                 .map{p => Json.obj("userName" -> p._1, "score" -> p._2)}
    )
    Json.stringify(json)
  }

}
