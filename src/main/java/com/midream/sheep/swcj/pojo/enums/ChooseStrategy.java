package com.midream.sheep.swcj.pojo.enums;

public enum ChooseStrategy {
    METHOD_NAME,ANNOTATION;
    public static ChooseStrategy getChooseStrategy(String key){
        switch (key){
            case "METHOD_NAME":
                return METHOD_NAME;
            case "ANNOTATION":
                return ANNOTATION;
        }
    return null;
    }

}
