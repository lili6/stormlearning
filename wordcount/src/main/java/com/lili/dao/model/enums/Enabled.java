package com.lili.dao.model.enums;

/**
 * Created by liguofang on 2014/10/27.
 * record enabled
 * 0:used
 * 1:deleted
 */
public enum Enabled {
    USED(0,"使用中"),
    DELETED(1,"已删除");

    private int code;
    private String desc;

    private Enabled(int code, String desc){
        this.code = code;
        this.desc = desc;
    }
    public int getCode(){
        return this.code;
    }

    public String getDesc(){
        return this.desc;
    }

    public static Enabled codeOf(int code){
        for(Enabled type : Enabled.values()){
            if(type.code == code){
                return type;
            }
        }
        return null;
    }

}
