package org.zerock.backend.controller.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.servlet.HandlerInterceptor;
import org.zerock.backend.util.JWTUtil;
import org.zerock.backend.util.exceptions.CustomJWTException;

import java.io.PrintWriter;
import java.util.Map;


@Log4j2
@RequiredArgsConstructor
public class JWTInterceptor implements HandlerInterceptor {


    private final JWTUtil jwtUtil;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("============JWTInterceptor=============");

        if(request.getMethod().equals("OPTIONS")){
            return true;
        }

        String accessToken = request.getHeader("Authorization");

        log.info("accessToken........" + accessToken);

        log.info(handler);

        log.info(jwtUtil);

        try {
            validateJWT(accessToken, response);
        }catch(CustomJWTException customJWTException){
            response.setContentType("application/json");

            try(PrintWriter printWriter = response.getWriter()) {

                printWriter.println("{\"error\": \"" + customJWTException.getMessage() + "\"} ");
                printWriter.flush();
            }catch(Exception e){

            }
            return false;
        }

        return true;
    }

    private Map<String, Object> validateJWT(String jwtStr , HttpServletResponse response) throws CustomJWTException {


        if(jwtStr == null) {
            throw new CustomJWTException("Null");
        }

        //Bearer 7
        String token = jwtStr.substring(7);

        return jwtUtil.validateToken(token);
    }

}
