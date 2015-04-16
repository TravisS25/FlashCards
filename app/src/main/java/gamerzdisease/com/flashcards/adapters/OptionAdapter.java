package gamerzdisease.com.flashcards.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import gamerzdisease.com.flashcards.R;

/**
 * Created by Travis on 3/27/2015.
 */
public class OptionAdapter extends BaseAdapter {

    private ArrayList<String> mText;
    private ArrayList<Integer> mImages;
    private static LayoutInflater mInflate;

    public OptionAdapter(Activity optionActivity, ArrayList<String> text, ArrayList<Integer> images ){
        mText = new ArrayList<>(text);
        mImages = new ArrayList<>(images);
        mInflate = (LayoutInflater)optionActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return mText.size();
    }

    @Override
    public Object getItem(int position){
        return mText.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View rowView;

        if(convertView == null)
            rowView = mInflate.inflate(R.layout.option_listview_layout, null);
        else
            rowView = convertView;

        TextView optionText = (TextView)rowView.findViewById(R.id.option_text);
        ImageView optionImage = (ImageView)rowView.findViewById(R.id.option_image);
        optionText.setText(mText.get(position));
        optionImage.setImageResource(mImages.get(position));
        return rowView;
    }

}