package com.midream.sheep.SWCJ.Exception;
/**
 * @author midreamSheep
 * 空方法指向异常，在找不到方法时
 * */
public class EmptyMatchMethodException extends Exception{
    public EmptyMatchMethodException(String message) {
        super(message);
    }
}
