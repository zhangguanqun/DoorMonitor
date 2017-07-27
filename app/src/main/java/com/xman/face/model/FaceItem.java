package com.xman.face.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangguanqun on 17-5-15.
 */

public class FaceItem {

    public boolean isDate = false;

    public String time;

    public List<String> faces;

    public String photo;

    public String person;

    public FaceItem() {
        this.person = "Unknown Person";
        faces = new ArrayList<String>();
    }

    public FaceItem(String time, String photo) {
        this.time = time;
        this.photo = photo;
        this.person = "Unknown Person";
        faces = new ArrayList<String>();
    }
}
