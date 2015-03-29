package gamerzdisease.com.flashcards.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import gamerzdisease.com.flashcards.R;

/**
 * Created by Travis on 3/1/2015.
 */
public class GradeAdapter extends BaseAdapter {

    ArrayList<Double> mGradeList;

    public GradeAdapter(ArrayList<Double> gradeList){ mGradeList = new ArrayList<>(gradeList); }

    @Override
    public int getCount(){
        return mGradeList.size();
    }

    @Override
    public Object getItem(int position){
        return mGradeList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Context context = parent.getContext();
        int textSize = (int)context.getResources().getDimension(R.dimen.text_size_special);
        Collections.reverse(mGradeList);
        double percentage = mGradeList.get(position);

        if(convertView == null){
            LinearLayout view = new LinearLayout(context);
            TextView gradeTextView = new TextView(context);
            gradeTextView.setPadding(15, 10, 0, 10);
            gradeTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            gradeTextView.setText(String.format("%.1f", percentage) + "%");
            view.addView(gradeTextView);
            return view;
        }
        return convertView;
    }

}
