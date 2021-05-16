package com.apptech.myapplication.list.product_gallery;

public class ProductGalleryLists {

    String id;
    String pro_id;
    String pro_name;
    String img_url;
    String _img_url_ar;
    String time;
    String video;
    String video_ar;
    String type;

    public ProductGalleryLists(String id, String pro_id, String pro_name, String img_url, String _img_url_ar, String time, String video, String video_ar, String type) {
        this.id = id;
        this.pro_id = pro_id;
        this.pro_name = pro_name;
        this.img_url = img_url;
        this._img_url_ar = _img_url_ar;
        this.time = time;
        this.video = video;
        this.video_ar = video_ar;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getPro_id() {
        return pro_id;
    }

    public String getPro_name() {
        return pro_name;
    }

    public String getImg_url() {
        return img_url;
    }

    public String get_img_url_ar() {
        return _img_url_ar;
    }

    public String getTime() {
        return time;
    }

    public String getVideo() {
        return video;
    }

    public String getVideo_ar() {
        return video_ar;
    }

    public String getType() {
        return type;
    }
}
