package com.erp.sheelafoam.BE;

/**
 * Created by dell on 18-Jul-17.
 */

public class MenuItems {
    private String txt_menu;
    private String txt_img_url;
    private String web_url;

    public MenuItems() {

    }

    public MenuItems(String txt_menu, String txt_img_url, String web_url) {
        this.txt_img_url = txt_img_url;
        this.txt_menu = txt_menu;
        this.web_url = web_url;

    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getTxt_menu() {
        return txt_menu;
    }

    public void setTxt_menu(String txt_menu) {
        this.txt_menu = txt_menu;
    }

    public String getTxt_img_url() {
        return txt_img_url;
    }

    public void setTxt_img_url(String txt_img_url) {
        this.txt_img_url = txt_img_url;
    }
}
