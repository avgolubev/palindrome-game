package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.parser.ParseException;

public class MainServlet extends HttpServlet {
    static final IDataProcessing dataProcessing = new DataProcessingInMemory();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String result;
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf8");
        response.setHeader("Cache-Control", "no-cache");
        
        result = Utils.topToJsonString(dataProcessing.getTopUsers());

        try (PrintWriter out = response.getWriter()) {
            out.write(result);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String result;
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf8");
        response.setHeader("Cache-Control", "no-cache");

        String httpBody = request.getReader().lines().collect(Collectors.joining());
        Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, httpBody);

        try {
            
            Pair<String, String> pair = Utils.getUserPhrasePair(httpBody);
            if(pair == null)
                result = Utils.NO_VALUE;
            else {
                String palindrome = Utils.checkPalindrome(pair.phrase);
                if(palindrome == null)
                    result = Utils.scoreWithErrorToJsonString(dataProcessing.getScore(pair.userName)
                                                                                                      , Utils.NOT_PALINDROME);
                else {
                        if(dataProcessing.savePalindrome(pair.userName, palindrome))
                            result = Utils.scoreToJsonString(dataProcessing.addScore(pair.userName, palindrome.length()));
                        else
                            result = Utils.scoreWithErrorToJsonString(dataProcessing.getScore(pair.userName)
                                                                                                              , Utils.REPEAT);
                }
            }
         
        } catch (ParseException ex) {
            Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
            result = Utils.PARSE_ERROR;
        }

        try (PrintWriter out = response.getWriter()) {
            out.write(result);
        }
    }

}
