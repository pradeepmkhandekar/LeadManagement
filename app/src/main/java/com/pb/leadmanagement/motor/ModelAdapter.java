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
import com.pb.leadmanagement.core.response.ModelX;

import java.util.ArrayList;
import java.util.List;

public class ModelAdapter extends ArrayAdapter<ModelX> {

    Context context;
    int resource, textViewResourceId;
    List<ModelX> items, tempItems, suggestions;

    public ModelAdapter(Context context, int resource, int textViewResourceId, List<ModelX> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<ModelX>(items); // this makes the difference.
        suggestions = new ArrayList<ModelX>();
    }

    @Nullable
    @Override
    public ModelX getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_people, parent, false);
        }
        ModelX model = items.get(position);
        if (model != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(model.getModel());
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
            String str = ((ModelX) resultValue).getModel();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (ModelX modelItem : tempItems) {
                    if (modelItem.getModel().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(modelItem);
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
            List<ModelX> filterList = (ArrayList<ModelX>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (ModelX modelItem : filterList) {
                    add(modelItem);
                    notifyDataSetChanged();
                }
            }
        }
    };
}