package ms.parade.interfaces.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(
        HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler
    ) {
        // 요청 정보를 로깅
        logger.info("Incoming Request: Method: {}, URI: {}, Remote Address: {}",
            request.getMethod(),
            request.getRequestURI(),
            request.getRemoteAddr());

        return true; // 컨트롤러로 요청을 전달
    }

    @Override
    public void afterCompletion(
        @NonNull HttpServletRequest request, HttpServletResponse response, @NonNull Object handler, Exception ex
    ) {
        // 응답 정보를 로깅
        logger.info("Response Status: {}, Content Type: {}",
            response.getStatus(),
            response.getContentType());

        // 예외가 발생했을 경우 추가 로깅
        if (ex != null) {
            logger.error("Exception occurred: ", ex);
        }
    }
}
