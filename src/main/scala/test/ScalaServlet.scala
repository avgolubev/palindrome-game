package test

import java.io.IOException
import java.io.UnsupportedEncodingException
import java.util.stream.Collectors
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(Array("/servlet"))
class ScalaServlet extends HttpServlet with Utility {
  
  private val serialVersionUID = 2932017957626829149L
  
  object HttpMethod extends Enumeration {
    type HttpMethod = Value
    val GET, POST, PUT, DELETE = Value
  }

  val dataProcessing = new DataProcessingInMemory
  
  import HttpMethod._
  
  @throws(classOf[UnsupportedEncodingException])
  @throws(classOf[IOException])
  private def processRequest(request: HttpServletRequest, 
          response: HttpServletResponse, httpMethod: HttpMethod)  = {
    
    commonInit(request, response)
    
    val result = httpMethod match {      
      case POST =>
        val httpBody = request.getReader().lines().collect(Collectors.joining())
        checkPalindromeAndGetResult(httpBody, dataProcessing)
    
      //GET
      case _ => getTopUsers(dataProcessing)
    }
    
    val out = response.getWriter()
    out.write(result)
    out.close()        
  }

  @throws(classOf[ServletException])
  @throws(classOf[IOException])
  override protected def doGet(request: HttpServletRequest, response: HttpServletResponse) =
    processRequest(request, response, HttpMethod.GET);
  

  @throws(classOf[ServletException])
  @throws(classOf[IOException])
  override protected def doPost(request: HttpServletRequest, response: HttpServletResponse) =
    processRequest(request, response, HttpMethod.POST)
  
  @throws(classOf[UnsupportedEncodingException])
  private def commonInit(request: HttpServletRequest, response: HttpServletResponse) = {
    request.setCharacterEncoding("UTF-8")
    response.setContentType("application/json; charset=utf8")
    response.setHeader("Cache-Control", "no-cache")

  }

}
