package com.apptech.lava_retailer.modal;

import com.apptech.lava_retailer.modal.sellOutPendingVerification.List;

import java.util.Date;

public class StartDateList {
    private Date date ;
    private List list;

    public StartDateList(Date date, List list) {
        this.date = date;
        this.list = list;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
}
