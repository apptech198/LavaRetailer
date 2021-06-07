package com.apptech.lava_retailer.list.modelList;

public class ModelList {
    String model;
    boolean checkable = false;

    public ModelList(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isCheckable() {
        return checkable;
    }

    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }
}
