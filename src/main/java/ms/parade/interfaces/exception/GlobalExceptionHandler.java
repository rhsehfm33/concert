package ms.parade.interfaces.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 예외 문구를 ';' 토큰으로 분리해 errorType, errorMessage 추출. <br>
     * 이 때 문자열 한 개면 errorMessage 로 설정,
     * 두 개면 errorType, errorMessage 로 설정.
     * 이 외의 경우는 ErrorResponse 설정 오류 반환.
     *
     * @param exceptionMessage 예외 메시지
     * @param status 반환할 HTTP 상태 코드
     * @return ResponseEntity with ErrorResponse body
     */
    private ResponseEntity<ErrorResponse> toErrorResponseEntity(String exceptionMessage, HttpStatus status) {
        // 메시지가 null 또는 빈 문자열인 경우 기본 메시지 설정
        if (exceptionMessage == null || exceptionMessage.trim().isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("WRONG_ERROR_RESPONSE", "Invalid error response format");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String[] errorInfo = exceptionMessage.split(";");
        if (errorInfo.length == 1) {
            ErrorResponse errorResponse = new ErrorResponse(null, errorInfo[0].trim());
            return new ResponseEntity<>(errorResponse, status);
        } else if (errorInfo.length == 2) {
            ErrorResponse errorResponse = new ErrorResponse(errorInfo[0].trim(), errorInfo[1].trim());
            return new ResponseEntity<>(errorResponse, status);
        } else {
            ErrorResponse errorResponse = new ErrorResponse("WRONG_ERROR_RESPONSE", "Invalid error response format");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return toErrorResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        return toErrorResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
        return toErrorResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return toErrorResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
