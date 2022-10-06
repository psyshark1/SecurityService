package servlets;

import Data.RequestData;
import Data.ResponseData;
import DataBase.Dbconn;
import utils.SecurityUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.sql.SQLException;

@WebServlet(name = "Api", urlPatterns = "/v1/api")

public class Api extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        RequestData requestData = new RequestData();
        ResponseData responseData = new ResponseData();
        response.setContentType("text/html;charset=UTF-8");
        Boolean badIP = (Boolean) request.getAttribute("badIP");
        assert badIP != null;

        if (badIP){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN,"Forbidden by IP");
            WriteMsg(response,"Forbidden by IP");
            return;

        }else{

            if (request.getHeader("X-YandexUID") != null){

                String[] decode228 = SecurityUtils.decPass64(SecurityUtils.decPass228(request.getHeader("X-YandexUID"))).split("_");

                if (decode228.length == 6) {
                    requestData.setUnixTime(decode228[0]);
                    requestData.setAccID(decode228[3]);
                    requestData.setRobotID(decode228[2]);

                    ReferenceQueue<String[]> queue = new ReferenceQueue<>();
                    WeakReference<String[]> weakRef = new WeakReference<>(decode228, queue);
                    decode228 = null;
                    weakRef.clear();

                    if (requestData.getAccID() != 0 && requestData.getRobotID() != 0) {

                        if (requestData.UnixTimeCheck(5)) {
                            try {
                                Dbconn db = Dbconn.getInstance();
                                String[] AutorityData = db.getAutorityData(requestData.getRobotID(), requestData.getAccID());
                                if (AutorityData[0] == null || AutorityData[1] == null) {
                                    WriteMsg(response, "No Data");
                                    return;
                                }
                                responseData.setLogin(AutorityData[0]);
                                responseData.setPass(AutorityData[1]);
                            } catch (SQLException s) {
                                response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "SQL Exception");
                                WriteMsg(response, PrintException(s) + " SQL Exception");
                                return;
                            } catch (ClassNotFoundException throwables) {
                                response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "ClassNotFound Exception");
                                WriteMsg(response, PrintException(throwables) + " ClassNotFound Exception");
                                return;
                            }

                            if (responseData.length() != 0) {
                                String output = SecurityUtils.encPass228(SecurityUtils.encPass64(responseData.getLogin() + "_" + responseData.getPass()));
                                response.addHeader("X-YandexUID", output);
                                WriteMsg(response, "OK");
                                return;
                            }
                        }
                    }
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST,"Bad Request");
        WriteMsg(response,"Bad Request");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED,"No POST");
        WriteMsg(response,"No POST");
    }

    private String PrintException(Exception e) {
        StringWriter err = new StringWriter();
        e.printStackTrace(new PrintWriter(err));
        return err.toString();
    }

    private void WriteMsg(HttpServletResponse response, String text) throws IOException {
        PrintWriter writer = response.getWriter();
        try {
            writer.print(text);
        } finally {
            writer.flush();
            writer.close();
        }
    }
}
