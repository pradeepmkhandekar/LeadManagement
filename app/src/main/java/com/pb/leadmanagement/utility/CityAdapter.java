package com.pb.leadmanagement.utility;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.pb.leadmanagement.R;
import com.pb.leadmanagement.core.model.CityMasterEntity;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends ArrayAdapter<CityMasterEntity> {

    Context context;
    int resource, textViewResourceId;
    List<CityMasterEntity> items, tempItems, suggestions;

    public CityAdapter(Context context, int resource, int textViewResourceId, List<CityMasterEntity> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<CityMasterEntity>(items); // this makes the difference.
        suggestions = new ArrayList<CityMasterEntity>();
    }

    @Nullable
    @Override
    public CityMasterEntity getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_people, parent, false);
        }
        CityMasterEntity make = items.get(position);
        if (make != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(make.getCityName());
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
            String str = ((CityMasterEntity) resultValue).getCityName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (CityMasterEntity people : tempItems) {
                    if (people.getCityName().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            List<CityMasterEntity> filterList = (ArrayList<CityMasterEntity>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (CityMasterEntity people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}