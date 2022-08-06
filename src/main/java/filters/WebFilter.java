package filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@javax.servlet.annotation.WebFilter(filterName = "WebFilter", urlPatterns = {"/*"})
public class WebFilter implements Filter {

    /*private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR" };*/

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        //String servletPath = request.getServletPath();

        if (request.getServletPath().equals("/v1/api")){

            String ip = getClientIpAddress(request);
            if (!ip.startsWith("10.108")){
                request.setAttribute("badIP", Boolean.TRUE);
            }else{
                request.setAttribute("badIP", Boolean.FALSE);
            }
        }

        chain.doFilter(request, response);

    }

    public void init(FilterConfig config) {}

    private String getClientIpAddress(HttpServletRequest request) {
        /*for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }*/
        return request.getRemoteAddr();
    }
}
