package hcmut.cse.travelsocialnetwork.utils;

/**
 * @author : hung.nguyen23
 * @since : 8/17/22 Wednesday
 **/
public class CustomException extends Exception{
    public CustomException() {
    }

    public CustomException(String s) {
        super(s, null, false, false);
    }

    public CustomException(String s, Throwable throwable) {
        super(s, throwable, false, false);
    }

    public CustomException(Throwable throwable) {
        super(throwable.getMessage(), throwable, false, false);
    }

    public CustomException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
