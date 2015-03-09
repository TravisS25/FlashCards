package gamerzdisease.com.flashcards.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import gamerzdisease.com.flashcards.R;

/**
 * Created by Travis on 3/1/2015.
 */
public class GradeAdapter extends BaseAdapter {


    @Override
    public int getCount(){
        return 0;
    }

    @Override
    public Object getItem(int position){
        return 0;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Context context = parent.getContext();
        int textSize = (int)context.getResources().getDimension(R.dimen.text_size);
        if(convertView == null){
            LinearLayout view = new LinearLayout(context);
            TextView nameTextView = new TextView(context);
            nameTextView.setPadding(0, 0, 10, 0);
            nameTextView.setTextSize(textSize);
            view.addView(nameTextView);
            return view;
        }
        return convertView;
    }

}
