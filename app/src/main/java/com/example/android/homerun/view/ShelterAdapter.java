package com.example.android.homerun.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.android.homerun.R;
import com.example.android.homerun.model.FilterCategories;
import com.example.android.homerun.model.Shelter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Comparator;

import me.xdrop.fuzzywuzzy.FuzzySearch;

/**
 * Created by PC on 2/27/18.
 */

public class ShelterAdapter extends ArrayAdapter<Shelter> implements Filterable {

    private ArrayList<Shelter> arrayList;
    private ArrayList<Shelter> originalList; // Original Values
    private FilterCategories filterCategory;

    public ShelterAdapter(Context context, ArrayList<Shelter> list) {
        super(context, 0, list);
        this.arrayList = list;
        this.filterCategory = FilterCategories.NAME;
    }

    public void setSearchCategory(FilterCategories searchCategory) { this.filterCategory = searchCategory;}

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.shelter_list_item, parent, false);
        }

        Shelter shelter = getItem(position);

        TextView name = (TextView) listItemView.findViewById(R.id.shelter_name);
        TextView capacity = (TextView) listItemView.findViewById(R.id.shelter_capacity);
        TextView gender = (TextView) listItemView.findViewById(R.id.shelter_gender);

        assert shelter != null;
        name.setText(shelter.getName());
        capacity.setText("Capacity: " + shelter.getCapacity());
        gender.setText("Restricted to: " + shelter.getRestrictions());

        return listItemView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayList.clear();
                arrayList.addAll((ArrayList<Shelter>) results.values);
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(final CharSequence constraint) {
                final FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                final ArrayList<Shelter> filteredArrList = new ArrayList();

                if (originalList == null) {
                    originalList = new ArrayList(arrayList); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = originalList.size();
                    results.values = originalList;
                } else {
                    final String constraint_string = constraint.toString().toLowerCase();

                    for (int i = 0; i < originalList.size(); i++) {
                        String data;
                        switch (filterCategory) {
                            case AGE:
                                data = originalList.get(i).getAgeCategory().toString().toLowerCase();
                                break;
                            case GENDER:
                                data = originalList.get(i).getGenderCategory().toString().toLowerCase();
                                break;
                            default:
                                data = originalList.get(i).getName().toLowerCase();
                        }
                        if (data.contains(constraint_string) ||
                                FuzzySearch.tokenSetRatio(constraint_string, data) >=
                                        (filterCategory == FilterCategories.GENDER ? 90 : 45)) {
                            filteredArrList.add(originalList.get(i));
                        }
                    }

                    filteredArrList.sort(new Comparator<Shelter>() {
                        @Override
                        public int compare(Shelter s1, Shelter s2) {
                            String s1_data;
                            String s2_data;

                            switch (filterCategory) {
                                case AGE:
                                    s1_data = s1.getAgeCategory().toString().toLowerCase();
                                    s2_data = s2.getAgeCategory().toString().toLowerCase();
                                    break;
                                case GENDER:
                                    s1_data = s1.getGenderCategory().toString().toLowerCase();
                                    s2_data = s2.getGenderCategory().toString().toLowerCase();
                                    break;
                                default:
                                    s1_data = s1.getName().toLowerCase();
                                    s2_data = s2.getName().toLowerCase();
                            }

                            if (s1_data.contains(constraint_string)) {
                                if (!s2_data.contains(constraint_string)) {
                                    return -1;
                                }
                                return s1.getName().toLowerCase().compareTo(s2.getName().toLowerCase());
                            } else if (s2_data.contains(constraint_string)) {
                                return 1;
                            }

                            int diff = FuzzySearch.tokenSetRatio(constraint_string, s2_data) -
                                    FuzzySearch.tokenSetRatio(constraint_string, s1_data);
                            if (diff == 0) {
                                return s1.getName().toLowerCase().compareTo(s2.getName().toLowerCase());
                            }

                            return diff;
                        }
                    });

                    // set the Filtered result to return
                    results.count = filteredArrList.size();
                    results.values = filteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
