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
import com.example.android.homerun.model.GenderCategories;
import com.example.android.homerun.model.Shelter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class ShelterAdapter extends ArrayAdapter<Shelter> implements Filterable {

    private final List<Shelter> arrayList;
    private List<Shelter> originalList; // Original Values
    private FilterCategories filterCategory;

    public ShelterAdapter(Context context, List<Shelter> list) {
        super(context, 0, list);
        this.arrayList = list;
        this.filterCategory = FilterCategories.NAME;
    }

    public List<Shelter> getShelters() {
        return arrayList;
    }


    /**
     * Sets the search category to be reflected on the search screen
     * @parameter a search category

     */
    public void setSearchCategory(FilterCategories searchCategory) {
        this.filterCategory = searchCategory;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.shelter_list_item,
                    parent, false);
        }

        Shelter shelter = getItem(position);

        TextView name = listItemView.findViewById(R.id.shelter_name);
        TextView capacity = listItemView.findViewById(R.id.shelter_capacity);
        TextView gender = listItemView.findViewById(R.id.shelter_gender);

        assert shelter != null;
        name.setText(shelter.getName());

        capacity.setText(getContext().getString(R.string.capacity, shelter.getCapacityString()));
        gender.setText(getContext().getString(R.string.restricted_to, shelter.getRestrictions()));

        return listItemView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayList.clear();
                arrayList.addAll((ArrayList<Shelter>) results.values);
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(final CharSequence constraint) {
                final FilterResults results = new FilterResults();
                final List<Shelter> filteredArrList = new ArrayList<>();
              
                if (originalList == null) {
                    originalList = new ArrayList<>(arrayList);
                }

                if ((constraint == null) || (constraint.length() == 0)) {
                    results.count = originalList.size();
                    results.values = originalList;

                } else {
                    final String constraint_string = constraint.toString().toLowerCase();

                    for (int i = 0; i < originalList.size(); i++) {
                        Shelter shelter = originalList.get(i);
                        if (satisfiesExactConstraint(shelter, constraint_string) ||
                                (fuzzySearchScore(shelter, constraint_string) > 0)) {
                            filteredArrList.add(shelter);
                        }
                    }

                    filteredArrList.sort(new Comparator<Shelter>() {
                        @Override
                        public int compare(Shelter s1, Shelter s2) {
                            boolean s1_bool = satisfiesExactConstraint(s1, constraint_string);
                            boolean s2_bool = satisfiesExactConstraint(s2, constraint_string);

                            if (s1_bool) {
                                if (!s2_bool) {
                                    return -1;
                                }
                                return s1.getName().compareToIgnoreCase(s2.getName());
                            } else if (s2_bool) {
                                return 1;
                            }

                            int diff = fuzzySearchScore(s2, constraint_string) -
                                    fuzzySearchScore(s1, constraint_string);
                            if (diff == 0) {
                                return s1.getName().compareToIgnoreCase(s2.getName());
                            }

                            return diff;
                        }
                    });

                    results.count = filteredArrList.size();
                    results.values = filteredArrList;
                }
                return results;
            }

            private boolean satisfiesExactConstraint(Shelter shelter, String constraint_string) {
                String data;
                boolean toReturn;

                switch (filterCategory) {

                    case GENDER:
                        data = shelter.getGenderCategory().toString().toLowerCase();
                        toReturn = data.equalsIgnoreCase(GenderCategories.ANYONE.toString()) ||
                                data.startsWith(constraint_string);
                        break;

                    case AGE:
                        data = shelter.getAgeCategory().toString().toLowerCase();
                        toReturn = data.contains(constraint_string);
                        break;

                    case NAME:
                        data = shelter.getName().toLowerCase();
                        toReturn = data.contains(constraint_string);
                        break;

                    default:
                        toReturn = false;
                }

                return toReturn;
            }

            private int fuzzySearchScore(Shelter shelter, String constraint_string) {
                final int GENDER_FUZZY_THRESHOLD = 90;
                final int AGE_FUZZY_THRESHOLD = 45;
                final int NAME_FUZZY_THRESHOLD = 45;

                String data;
                int toReturn;

                switch (filterCategory) {

                    case GENDER:
                        data = shelter.getGenderCategory().toString().toLowerCase();
                        toReturn = FuzzySearch.tokenSetRatio(constraint_string, data);
                        toReturn = (toReturn >= GENDER_FUZZY_THRESHOLD) ? toReturn : 0;
                        break;

                    case AGE:
                        data = shelter.getAgeCategory().toString().toLowerCase();
                        toReturn = FuzzySearch.tokenSetRatio(constraint_string, data);
                        toReturn = (toReturn >= AGE_FUZZY_THRESHOLD) ? toReturn : 0;
                        break;

                    case NAME:
                        data = shelter.getName().toLowerCase();
                        toReturn = FuzzySearch.tokenSetRatio(constraint_string, data);
                        toReturn = (toReturn >= NAME_FUZZY_THRESHOLD) ? toReturn : 0;
                        break;

                    default:
                        toReturn = 0;
                }

                return toReturn;
            }
        };
    }
}
