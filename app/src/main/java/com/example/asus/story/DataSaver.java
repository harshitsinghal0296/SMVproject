package com.example.asus.story;

/**
 * Created by asus on 6/29/2017.
 */

public class DataSaver {

    String title,desc;
    byte[] img;
    long sid,cid;

    public DataSaver(){
    }

    public void setStoryData(String tit,String desc,byte[] pic)
    {
        this.title = tit;
        this.desc = desc;
        this.img = pic;
    }
    public void setids(long c,long s)
    {
        this.sid = s;
        this.cid = c;
    }
    public void set_cat_id(long c)
    {
        this.cid = c;
    }
    public String getStoryTitle()
    {
        return this.title;
    }
    public String getStoryDesc()
    {
        return this.desc;
    }
    public byte[] getStoryImg()
    {
        return this.img;
    }
    public long get_c_id()
    {
        return this.cid;
    }
    public long get_s_id()
    {
        return this.sid;
    }

}
