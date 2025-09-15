package prateek_gupta.SampleProject.prateek_gupta;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;

import java.util.ResourceBundle;

public class ServiceException extends Exception {
    public enum ExceptionType {
        UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
        DB_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
        MISSING_REQUIRED_PARAMETERS(HttpStatus.BAD_REQUEST),
        MODULE_LOCK(HttpStatus.FORBIDDEN);

        final HttpStatus status;

        ExceptionType(HttpStatus status) {
            this.status = status;
        }
    }


    private static final Log logUtil = LogFactory.getLog(ServiceException.class);

    public HttpStatus status;
    public String exceptionMessage;

    public ServiceException() {
        this(ExceptionType.UNKNOWN_ERROR, null, null);
    }

    public ServiceException(
            ExceptionType exceptionType) {
        this(exceptionType, null, null);
    }

    public ServiceException(
            String exceptionMessage) {
        this(null, null, exceptionMessage);
    }

    public ServiceException(HttpStatus status,
                            String exceptionMessage) {
        this(null, status, exceptionMessage);
    }

    public ServiceException(
            ExceptionType exceptionType, HttpStatus status, String exceptionMessage) {
        if (status != null && StringUtils.isNotBlank(exceptionMessage)) {
            this.status = status;
            this.exceptionMessage = exceptionMessage;

        } else if (exceptionType != null) {
            this.status = exceptionType.status;
            this.exceptionMessage =
                    ResourceBundle.getBundle("ServiceExceptionMessages").getString(
                            exceptionType.toString());
        } else if (StringUtils.isNotBlank(exceptionMessage)) {
            this.status = HttpStatus.INTERNAL_SERVER_ERROR;
            this.exceptionMessage = exceptionMessage;
        } else {
            this.status = HttpStatus.INTERNAL_SERVER_ERROR;
            this.exceptionMessage =
                    ResourceBundle.getBundle("ServiceExceptionMessages").getString(
                            ExceptionType.UNKNOWN_ERROR.toString());
        }
    }

    public static void logException(Exception e) {
        String message = e instanceof ServiceException ?
                ((ServiceException) e).exceptionMessage : e.getMessage();
        logUtil.error(message, e);
    }

    public static void moduleLockCheck(
            String fieldKey,boolean isNotBlank) throws ServiceException {
        moduleLockCheck(fieldKey,null,isNotBlank);
    }

    public static void moduleLockCheck(
            String fieldKey,String fieldValue,boolean isNotBlank) throws ServiceException {
        Object configValue=Init.getConfiguration(fieldKey,"");
        if (isNotBlank) {
            if (StringUtils.isBlank(String.valueOf(configValue)))
                throw new ServiceException(ExceptionType.MODULE_LOCK);
        }
        else if (StringUtils.isBlank(String.valueOf(configValue)) || !configValue.equals(fieldValue))
                throw new ServiceException(ExceptionType.MODULE_LOCK);
    }
}
