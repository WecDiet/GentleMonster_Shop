package com.gentlemonster.GentleMonsterBE.Exception;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super(message);
    }
}
