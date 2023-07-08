package com.example.crm_loginpage.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public class CustomAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private final List<String> originalList;
    private final List<String> filteredList;

    public CustomAutoCompleteAdapter(Context context, List<String> items) {
        super(context, android.R.layout.simple_list_item_1, items);
        originalList = new ArrayList<>(items);
        filteredList = new ArrayList<>();
    }

    @Override
    public Filter getFilter() {
        return new CustomFilter();
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            filteredList.clear();


            String typedText = constraint.toString().toLowerCase();
            for (String item : originalList) {
                if (item.toLowerCase().contains(typedText)) {
                    filteredList.add(item);
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results.count > 0) {
                addAll((ArrayList<String>) results.values);
            }
            notifyDataSetChanged();
        }
    }
}

