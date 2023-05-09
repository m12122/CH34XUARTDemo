package cn.wch.ch34xuartdemo.entity;

import com.google.gson.annotations.Expose;

public class JsonEntity {
    @Expose
    private String labnumber;
    @Expose
    private String epc;
    public JsonEntity(String labnumber,String epc){
        this.epc = epc;
        this.labnumber = labnumber;
    }

}
