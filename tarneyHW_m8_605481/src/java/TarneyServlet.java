
/**
 * TarneyServlet Class
 * - Serves up HTML pages for Beartooth hiking company
 *
 * @author Brandon Tarney
 * @since 6/31/2017
 */
import com.brandontarney.bookingrate.Rates.HIKE;
import com.brandontarney.controller.BadQueryStringException;
import com.brandontarney.controller.BadRateException;
import com.brandontarney.controller.Controller;
import com.brandontarney.controller.HikeQueryParser;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "TarneyServlet", urlPatterns = {"/TarneyServlet"})
public class TarneyServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String queryString = request.getQueryString();
        //System.out.println("Query String is: " + queryString);
        try {
            HIKE hike = HikeQueryParser.getHike(queryString);
            int duration = HikeQueryParser.getDuration(queryString);
            
            int[] monthDayYear= HikeQueryParser.getDate(queryString);
            int month = monthDayYear[0];
            int day = monthDayYear[1];
            int year = monthDayYear[2];
            /*
            int year = HikeQueryParser.getYear(queryString);
            int month = HikeQueryParser.getMonth(queryString);
            int day = HikeQueryParser.getDay(queryString);
            */

            int partySize = HikeQueryParser.getPartySize(queryString);

            String[] rates = Controller.computeRate(hike, duration, year, month, day);
            String totalCost = String.valueOf(partySize * Double.parseDouble(rates[0]));
            createHikeRatePage(response, rates[0], totalCost);
        } catch (BadQueryStringException | BadRateException exception) {
            createUserErrorPage(response, exception.getMessage());
        }
    }

    /**
     * createUserErrorPage
     *
     * @param response response sent to client
     * @param errorMsg message to display at the top of HTML page
     * @throws IOException
     */
    private void createUserErrorPage(HttpServletResponse response, String errorMsg)
            throws IOException {
        try (PrintWriter out = response.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");

            out.println("<head>");
            out.println("<title>Beartooth Hiking Company (BHC)</title>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
            out.println("<link rel=\"stylesheet\" href=\"css/BeartoothHikingCompany.css\" type=\"text/css\" />");
            out.println("<script type=\"text/javascript\" src=\"javascript/BeartoothHikingCompany.js\"></script>");
            out.println("<script src=\"javascript/jquery-3.2.1.js\"></script>");
            out.println("<script src=\"javascript/jquery-ui-1.12.1/jquery-ui.js\"></script>");
            out.println("<script>");
            out.println("$( function() {");
            out.println("$( \"#datepicker\" ).datepicker();");
            out.println("} );");
            out.println("</script>");
            out.println("<style>");
            out.println(".ui-datepicker {");
            out.println("background: #c6dbff;");
            out.println("}");
            out.println("</style>");

            out.println("</head>");

            out.println("<body>");
            out.println("<div id=\"page_wrapper\">");

            out.println("<header>");
            out.println("<h1 id=\"top_header\">Beartooth Hiking Company</h1>");
            out.println("<p id=\"slogan\">Alaska's Lamest Hiking Company</p>");
            out.println("<nav>");
            out.println("</header>");

            out.println("<section id=\"form\" class=\"table\">");
            out.println("<h2>ERROR</h2>");
            out.println("<p class=\"excited_msg\">" + errorMsg + "</p>");
            out.println("<h2 id=\"form_header\">Choose Your Hike</h2>");
            out.println("<form action=\"http://localhost:8084/tarneyHW_m7_605481/TarneyServlet\" method=GET onSubmit=\" return validatePartySize(1, 10)\">");
            //out.println("<form action=\"http://web6.jhuep.com:80/tarneyHW_m7_605481/TarneyServlet\" method=GET onSubmit=\" return validatePartySize(1, 10)\">");
            out.println("<p class=\"excited_msg\">Hike</p>");
            out.println("<select class=\"bigger_text\" name=\"hike\" size=\"1\">");
            out.println("<option value=\"hellroaring\">Hellroaring Plateau</option>");
            out.println("<option value=\"gardiner\">Gardiner Lake</option>");
            out.println("<option value=\"beaten\">Beaten Path</option>");
            out.println("</select>");
            out.println("<p class=\"excited_msg\">Date (6/1 - 9/31)</p>");
            out.println("<input type=\"text\" class=\"big_text\" id=\"datepicker\" name=\"datepicker\" size=\"10\" value=\"06/01/2017\">");
            out.println("<p class=\"excited_msg\">Duration (days, see below)</p>");
            out.println("<select class=\"bigger_text\" name=\"duration\">");
            out.println("<option value=\"2\">2</option>");
            out.println("<option value=\"3\">3</option>");
            out.println("<option value=\"4\">4</option>");
            out.println("<option value=\"5\">5</option>");
            out.println("<option value=\"7\">7</option>");
            out.println("</select>");
            out.println("<p class=\"excited_msg\">Party Size (Humans, 1 - 10)</p>");
            out.println("<input class=\"big_text\" type=\"text\" size=\"5\" maxlength=\"2\" name=\"people\" id=\"people\" onBlur=\"validatePartySize(1,10)\" value=\"1\">");
            out.println("<br/><br/>");
            out.println("<input class=\"biggest_text\" type=\"SUBMIT\" name=\"submit\" value=\"submit\" />");
            out.println("<br/>");
            out.println("<p class=\"excited_msg\">Cost Per Person:");
            out.println("<input class=\"bigger_text\" type=\"text\" name=\"zip\" size=\"15\" maxlength=\"15\" value=\"$0.00 ($USD)\"/>");
            out.println("</p>");
            out.println("<p class=\"excited_msg\">Total Cost:");
            out.println("<input class=\"bigger_text\" type=\"text\" name=\"zip\" size=\"15\" maxlength=\"15\" value=\"$0.00 ($USD)\"/>");
            out.println("</p>");
            out.println("</form>");
            out.println("</section>");

            out.println("<!-- form types: text fields, text areas, buttons, checkboxes, password, file, radio, submit, text -->");
            out.println("<section id=\"main_info\">");
            out.println("<table id=\"tours_table\">");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th id=\"table_title\" colspan=\"5\">Tour Options</th>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<th>Tour</th>");
            out.println("<th>Duration (days)</th>");
            out.println("<th>Difficulty</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            out.println("<tr>");
            out.println("<td>Hellroaring Plateau</td>");
            out.println("<td>2, 3, 4</td>");
            out.println("<td>easy</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td>Gardiner Lake</td>");
            out.println("<td>3, 5</td>");
            out.println("<td>intermediate</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td>The Beaten Path</td>");
            out.println("<td>5, 7</td>");
            out.println("<td>difficult</td>");
            out.println("</tr>");
            out.println("</tbody>");
            out.println("</table>");
            out.println("</section>");
            out.println("<section id=\"more_info\">");
            out.println("<h2 id=\"more_info_header\" class=\"more_info\">More Information</h2>");
            out.println("<p>For more information, checkout the <a href=\"http://www.wilderness.net/index.cfm?fuse=NWPS&sec=wildView&WID=1\"><span id=\"wilderness_link\">wilderness website</span></a></p>");
            out.println("</section>");
            out.println("<footer>");
            out.println("<p>Contact: <a href=\"mailto:brandon.tarney@gmail.com\">Brandon Tarney</a></p>");
            out.println("</footer>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * createHikeRatePage
     *
     * @param response response sent to client
     * @param rate rate to display on HTML page
     * @throws IOException
     */
    private void createHikeRatePage(HttpServletResponse response, String rate, String totalCost)
            throws IOException {

        try (PrintWriter out = response.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");

            out.println("<head>");
            out.println("<title>Beartooth Hiking Company (BHC)</title>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
            out.println("<link rel=\"stylesheet\" href=\"css/BeartoothHikingCompany.css\" type=\"text/css\" />");
            out.println("<script type=\"text/javascript\" src=\"javascript/BeartoothHikingCompany.js\"></script>");
            out.println("<script src=\"javascript/jquery-3.2.1.js\"></script>");
            out.println("<script src=\"javascript/jquery-ui-1.12.1/jquery-ui.js\"></script>");
            out.println("<script>");
            out.println("$( function() {");
            out.println("$( \"#datepicker\" ).datepicker();");
            out.println("} );");
            out.println("</script>");
            out.println("<style>");
            out.println(".ui-datepicker {");
            out.println("background: #c6dbff;");
            out.println("}");
            out.println("</style>");

            out.println("</head>");

            out.println("<body>");
            out.println("<div id=\"page_wrapper\">");

            out.println("<header>");
            out.println("<h1 id=\"top_header\">Beartooth Hiking Company</h1>");
            out.println("<p id=\"slogan\">Alaska's Lamest Hiking Company</p>");
            out.println("<nav>");
            out.println("</header>");

            out.println("<section id=\"form\" class=\"table\">");
            out.println("<h2 id=\"form_header\">Choose Your Hike</h2>");
            out.println("<form action=\"http://localhost:8084/tarneyHW_m7_605481/TarneyServlet\" method=GET onSubmit=\" return validatePartySize(1, 10)\">");
            //out.println("<form action=\"http://web6.jhuep.com:80/tarneyHW_m7_605481/TarneyServlet\" method=GET onSubmit=\" return validatePartySize(1, 10)\">");
            out.println("<p class=\"excited_msg\">Hike</p>");
            out.println("<select class=\"bigger_text\" name=\"hike\" size=\"1\">");
            out.println("<option value=\"hellroaring\">Hellroaring Plateau</option>");
            out.println("<option value=\"gardiner\">Gardiner Lake</option>");
            out.println("<option value=\"beaten\">Beaten Path</option>");
            out.println("</select>");
            out.println("<p class=\"excited_msg\">Date (6/1 - 9/31)</p>");
            out.println("<input type=\"text\" class=\"big_text\" id=\"datepicker\" name=\"datepicker\" size=\"10\" value=\"06/01/2017\">");
            out.println("<p class=\"excited_msg\">Duration (days, see below)</p>");
            out.println("<select class=\"bigger_text\" name=\"duration\">");
            out.println("<option value=\"2\">2</option>");
            out.println("<option value=\"3\">3</option>");
            out.println("<option value=\"4\">4</option>");
            out.println("<option value=\"5\">5</option>");
            out.println("<option value=\"7\">7</option>");
            out.println("</select>");
            out.println("<p class=\"excited_msg\">Party Size (Humans, 1 - 10)</p>");
            out.println("<input class=\"big_text\" type=\"text\" size=\"5\" maxlength=\"2\" name=\"people\" id=\"people\" onBlur=\"validatePartySize(1,10)\" value=\"1\">");
            out.println("<br/><br/>");
            out.println("<input class=\"biggest_text\" type=\"SUBMIT\" name=\"submit\" value=\"submit\" />");
            out.println("<br/>");
            out.println("<p class=\"excited_msg\">Cost Per Person:");
            out.println("<input class=\"bigger_text\" type=\"text\" name=\"zip\" size=\"15\" maxlength=\"15\" value=\"$" + rate + " ($USD)\"/>");
            out.println("</p>");
            out.println("<p class=\"excited_msg\">Total Cost:");
            out.println("<input class=\"bigger_text\" type=\"text\" name=\"zip\" size=\"15\" maxlength=\"15\" value=\"$" + totalCost + " ($USD)\"/>");
            out.println("</p>");
            out.println("</form>");
            out.println("</section>");
            out.println("<!-- form types: text fields, text areas, buttons, checkboxes, password, file, radio, submit, text -->");
            out.println("<section id=\"main_info\">");
            out.println("<table id=\"tours_table\">");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th id=\"table_title\" colspan=\"5\">Tour Options</th>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<th>Tour</th>");
            out.println("<th>Duration (days)</th>");
            out.println("<th>Difficulty</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            out.println("<tr>");
            out.println("<td>Gardiner Lake</td>");
            out.println("<td>3, 5</td>");
            out.println("<td>intermediate</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td>Hellroaring Plateau</td>");
            out.println("<td>2, 3, 4</td>");
            out.println("<td>easy</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td>The Beaten Path</td>");
            out.println("<td>5, 7</td>");
            out.println("<td>difficult</td>");
            out.println("</tr>");
            out.println("</tbody>");
            out.println("</table>");
            out.println("</section>");
            out.println("<section id=\"more_info\">");
            out.println("<h2 id=\"more_info_header\" class=\"more_info\">More Information</h2>");
            out.println("<p>For more information, checkout the <a href=\"http://www.wilderness.net/index.cfm?fuse=NWPS&sec=wildView&WID=1\"><span id=\"wilderness_link\">wilderness website</span></a></p>");
            out.println("</section>");
            out.println("<footer>");
            out.println("<p>Contact: <a href=\"mailto:brandon.tarney@gmail.com\">Brandon Tarney</a></p>");
            out.println("</footer>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
