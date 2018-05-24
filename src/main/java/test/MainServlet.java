package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/servlet")
public class MainServlet extends HttpServlet {
  
  enum HttpMethod {GET, POST, PUT, DELETE};

  static final IDataProcessing dataProcessing = new DataProcessingInMemory();
  
  private void processRequest(HttpServletRequest request, 
          HttpServletResponse response, HttpMethod httpMethod) throws UnsupportedEncodingException, IOException {
    String result;
    
    commonInit(request, response);
    
    switch(httpMethod) {      
      case POST:
        String httpBody = request.getReader().lines().collect(Collectors.joining());
        result = Utils.checkPalindromeAndGetResult(httpBody, dataProcessing);
      break;
      //GET
      default: result = Utils.getTopUsers(dataProcessing);
    }
    
    try (PrintWriter out = response.getWriter()) {
      out.write(result);
    }        
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    processRequest(request, response, HttpMethod.GET);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    processRequest(request, response, HttpMethod.POST);
  }
  
  private void commonInit(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
    request.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=utf8");
    response.setHeader("Cache-Control", "no-cache");

  }

}
