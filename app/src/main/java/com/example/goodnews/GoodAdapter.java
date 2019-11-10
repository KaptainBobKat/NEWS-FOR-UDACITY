package com.example.goodnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;

public class GoodAdapter extends ArrayAdapter<Good> {
    public GoodAdapter(Context context, ArrayList<Good> news)
    {
        super(context, 0, news);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View GoodNewlist = convertView;
        if (GoodNewlist == null) {
            GoodNewlist = LayoutInflater.from(getContext()).inflate(
                    R.layout.structure_acitivity, parent, false);
        }
        Good goodNews = getItem(position);
        TextView goodHeadlineTextView = (TextView) GoodNewlist.findViewById(R.id.main_headline);
        String GoodHeadline = goodNews.getmTitle();
        goodHeadlineTextView.setText(GoodHeadline);

        TextView goodCategoryTextView = (TextView) GoodNewlist.findViewById(R.id.Category);
        String GoodCategory = goodNews.getmCategory();
        goodCategoryTextView.setText(GoodCategory);

        TextView GoodDateTextView = (TextView) GoodNewlist.findViewById(R.id.Date);
        String GoodDate = goodNews.getmDate();
        GoodDateTextView.setText(GoodDate);

        TextView GoodWriterTextView = (TextView) GoodNewlist.findViewById(R.id.Writer);
        String GoodWriter = goodNews.getmAuthor();
        GoodWriterTextView.setText(GoodWriter);
        if(GoodWriter.equals("")){
            GoodWriter = getContext().getString(R.string.noauthor);
        }

        GoodWriterTextView.setText(GoodWriter);

        return GoodNewlist;
    }
}
