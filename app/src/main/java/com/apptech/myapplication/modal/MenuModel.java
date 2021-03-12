package com.apptech.myapplication.modal;

public class MenuModel {

    public String menuName , fragmentname;
    public boolean hasChildren, isGroup;

    public MenuModel(String menuName, boolean isGroup, boolean hasChildren , String fragmentname) {

        this.menuName = menuName;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
        this.fragmentname = fragmentname;
    }

}
