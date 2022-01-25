package com.midream.sheep.swsj.Exception;
/**
 * @author midreamSheep
 * 策略冲突异常
 * */
public class ConflictException extends Exception{
    public ConflictException(String message) {
        super(message);
    }
}
