package com.pb.leadmanagement.motor;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.pb.leadmanagement.R;
import com.pb.leadmanagement.core.response.MakeX;
import com.pb.leadmanagement.test.People;

import java.util.ArrayList;
import java.util.List;

public class MakeAdapter extends ArrayAdapter<MakeX> {

    Context context;
    int resource, textViewResourceId;
    List<MakeX> items, tempItems, suggestions;

    public MakeAdapter(Context context, int resource, int textViewResourceId, List<MakeX> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<MakeX>(items); // this makes the difference.
        suggestions = new ArrayList<MakeX>();
    }

    @Nullable
    @Override
    public MakeX getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_people, parent, false);
        }
        MakeX make = items.get(position);
        if (make != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(make.getMake());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((MakeX) resultValue).getMake();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (MakeX people : tempItems) {
                    if (people.getMake().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<MakeX> filterList = (ArrayList<MakeX>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (MakeX people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}