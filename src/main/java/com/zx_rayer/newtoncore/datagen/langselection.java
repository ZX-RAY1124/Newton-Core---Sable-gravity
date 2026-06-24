package com.zx_rayer.newtoncore.datagen;

public enum langselection {
    EN_US("en_us"),
    ZH_CN("zh_cn");

    private final String code;

    langselection(String code) {   //enum构造函数
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }

}