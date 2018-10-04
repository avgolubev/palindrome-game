package test;

import java.util.{ArrayList, List, Comparator}
import java.util.concurrent.ConcurrentHashMap
import collection.JavaConverters._

class DataProcessingInMemory extends DataProcessing {

  private val SCORE_MAP = new ConcurrentHashMap[String, Int].asScala
  private val PHRASE_MAP = new ConcurrentHashMap[String, Seq[String]].asScala

  override def savePalindrome(userName: String, phrase: String): Either[OldScore, NewScore] = 
    PHRASE_MAP.synchronized {
      val phraseList = PHRASE_MAP.getOrElseUpdate(userName, Seq[String]())

      if (phraseList.contains(phrase)) 
        Left(sumSeqElemSize(phraseList))
      else {
        val newPhraseList = phraseList :+ phrase
        PHRASE_MAP.put(userName, newPhraseList)        
        Right(sumSeqElemSize(newPhraseList))
      }
    }    
  
  override def getScore(userName: String): Int = 
    PHRASE_MAP.get(userName) match {
      case Some(seq) => sumSeqElemSize(seq)
      case _ => 0
  }
   
  override def getUsersAndScores = {
    val res = PHRASE_MAP.map{case (u, l) => (u, sumSeqElemSize(l))}
    collection.immutable.Map(res.toSeq: _*)
  }
  
  private def sumSeqElemSize(seq: Seq[String]) = seq.foldLeft(0)((a, s) => a + s.size)  

}
