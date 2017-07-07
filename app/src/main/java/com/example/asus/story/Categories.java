/*package com.example.asus.story;

import com.orm.SugarRecord;


public class Categories extends SugarRecord {
    public String name;
    Categories(){
    }
    Categories(String name){
    }

    public String getName(){ return name; }
    public void setName(String name){
        this.name = name;
    }

} */

package com.example.asus.story;

/**
 * Created by asus on 6/19/2017.
 */

public class Categories {

    //private variables
    int _id;
    String _caption;

    // Empty constructor
    public Categories(){

    }
    // constructor
    public Categories(int id, String caption){
        this._id = id;
        this._caption = caption;
    }

    // constructor
    public Categories(String caption){

        this._caption = caption;
    }

    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting first name
    public String getCaption(){
        return this._caption;
    }

    // setting first name
    public void setcaption(String caption){
        this._caption = caption;
    }

    public String toString() {
        return _caption;
    }
}


