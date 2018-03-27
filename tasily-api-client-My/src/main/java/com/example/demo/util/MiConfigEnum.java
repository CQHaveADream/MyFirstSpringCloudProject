package com.example.demo.util;

/**
 * @author chenqian
 * @date 2018-03-21 9:26
 */

public enum MiConfigEnum {

    passThroughMessage("t",1), // 1表示透传消息
    notificationMessage("n",0), // 0表示通知栏消息
    defaultAll("d",-1),
    defaultSound("s",1), // 使用默认提示音提
    defaultVibrate("v",2), //使用默认震动提示
    defaultLights("l",3); // 使用默认led灯光提示

    private String name;
    private int index;

    private MiConfigEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

}
