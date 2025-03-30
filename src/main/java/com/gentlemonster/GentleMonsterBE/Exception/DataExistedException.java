package com.gentlemonster.GentleMonsterBE.Exception;

public class DataExistedException extends RuntimeException {
    public DataExistedException(String message) {
        super(message);
    }
}
