package com.lili.dao.model.enums;

/**
 * Created by liguofang on 2014/10/27.
 * 投诉处理状态
 */
public enum ProcessStatus {
    Initial(1,"未处理"), //新增订单，待处理订单
//    Locked(1,"正在处理"), //订单正在匹配处理中
//    Cancel(2,"订单取消"),  //订单取消，用户发起的取消
//    Closed(3,"订单过期关闭"),  //订单关闭
    Finished(2,"处理完成"); //订单完成，如果该订单一次匹配完毕 ，则为完成
//    Matched(5,"匹配成功"),  //订单匹配成功，针对部分成功的情况，统计需要
//    MatchLeft(6,"匹配剩余");// 订单匹配剩余的部分

    private int code;
    private String desc;

    private ProcessStatus(int code, String desc){
        this.code = code;
        this.desc = desc;
    }
    public int getCode(){
        return this.code;
    }

    public String getDesc(){
        return this.desc;
    }

    public static ProcessStatus codeOf(int code){
        for(ProcessStatus type : ProcessStatus.values()){
            if(type.code == code){
                return type;
            }
        }
        return null;
    }

}
