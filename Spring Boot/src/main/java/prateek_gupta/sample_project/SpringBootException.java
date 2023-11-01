package prateek_gupta.sample_project;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ResourceBundle;

public class SpringBootException extends Exception {
    public enum ExceptionType{UNKNOWN_ERROR,DB_ERROR,MISSING_REQUIRED_DATA}

    private static final Log logUtil = LogFactory.getLog(SpringBootException.class);

    public ExceptionType exceptionType;

    public SpringBootException() {
        this.exceptionType = ExceptionType.UNKNOWN_ERROR;
    }

    public SpringBootException(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getMessage() {
        if (exceptionType == null)
            exceptionType = ExceptionType.UNKNOWN_ERROR;
        return ResourceBundle.getBundle("SampleProjectError").getString(
                exceptionType.toString());
    }

    public static void logException(Exception e) {
        logUtil.error(e.getMessage(), e);
    }
}
