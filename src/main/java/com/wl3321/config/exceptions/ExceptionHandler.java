package com.wl3321.config.exceptions;

import com.wl3321.pojo.ApiResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/2 11:32
 * desc   : 统一异常处理
 */
@ControllerAdvice
public class ExceptionHandler {


    /**
     * 校验单个参数
     * @param exception
     * @return
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ApiResponse handle(ConstraintViolationException exception) {
        ConstraintViolationException exs = (ConstraintViolationException) exception;

        Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
        if (!violations.isEmpty()){
            StringBuilder msgBuilder = new StringBuilder();
            for (ConstraintViolation<?> item : violations) {
                //打印验证不通过的信息
//                    System.out.println(item.getMessage());
                msgBuilder.append(item.getMessage()).append(",");
            }
            String errorMessage = msgBuilder.toString();

            errorMessage = checkMessage(errorMessage);
            return ApiResponse.of(999, errorMessage, null);
        }
        return ApiResponse.of(999, exception.getMessage(), null);
    }

    private String  checkMessage(String errorMessage) {
        if (errorMessage.length() > 1) {
            errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
        }
        return errorMessage;
    }

    /**
     * 校验jsonBean 组合参数
     * @param ex
     * @return
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiResponse handle(MethodArgumentNotValidException ex) {
        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
        if (!CollectionUtils.isEmpty(objectErrors)) {
            StringBuilder msgBuilder = new StringBuilder();
            for (ObjectError objectError : objectErrors) {
                msgBuilder.append(objectError.getDefaultMessage()).append(",");
            }
            String errorMessage = msgBuilder.toString();
            errorMessage = checkMessage(errorMessage);
            return ApiResponse.of(999, errorMessage, null);
        }
        return ApiResponse.of(999, ex.getMessage(), null);
    }
}
