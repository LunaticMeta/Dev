package com.pelkan.tab;

import java.io.Serializable;

/**
 * Created by jhj0104 on 2016-10-31.
 */

public class Data implements Serializable {
    public String Date;
    public String time;
    public Data(){}
    public Data(String Date, String time){
        this.Date = Date;
        this.time=time;
    }
}