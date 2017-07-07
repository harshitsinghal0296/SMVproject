package com.example.asus.story;

import java.util.HashMap;

import android.content.Context;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatAutoCompleteTextView;



/** Customizing AutoCompleteTextView to return Place Description
 * corresponding to the selected item
 */
public class CustomAutoCompleteTextView extends AppCompatAutoCompleteTextView {

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** Returns the place description corresponding to the selected item */
    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        /** Each item in the autocompetetextview suggestion list is a hashmap object */
        HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
        return hm.get("description");
    }
}