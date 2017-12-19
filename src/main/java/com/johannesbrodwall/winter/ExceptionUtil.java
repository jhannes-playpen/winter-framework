package com.johannesbrodwall.winter;

public class ExceptionUtil {

    public static RuntimeException soften(Exception e) {
        return helper(e);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Exception> T helper(Exception e) throws T {
        throw (T) e;
    }

}
