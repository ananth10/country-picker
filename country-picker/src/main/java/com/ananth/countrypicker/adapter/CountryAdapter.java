package com.ananth.countrypicker.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ananth.countrypicker.R;
import com.ananth.countrypicker.models.CountryItem;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

/**
 * Created by Babu on 7/2/2016.
 */
public class CountryAdapter  extends BaseAdapter {

    private Context context;
    List<CountryItem> countries;
    LayoutInflater inflater;

    private int getResId(String drawableName) {

        try {
            Class<R.drawable> res = R.drawable.class;
            Field field = res.getField(drawableName);
            int drawableId = field.getInt(null);
            return drawableId;
        } catch (Exception e) {
            Log.e("CountryCodePicker", "Failure to get drawable id.", e);
        }
        return -1;
    }

    public CountryAdapter(Context context, List<CountryItem> countries) {
        super();
        this.context = context;
        this.countries = countries;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override public int getCount() {
        return countries.size();
    }

    @Override public Object getItem(int arg0) {
        return null;
    }

    @Override public long getItemId(int arg0) {
        return 0;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View cellView = convertView;
        Cell cell;
        CountryItem country = countries.get(position);

        if (convertView == null) {
            cell = new Cell();
            cellView = inflater.inflate(R.layout.item_row, null);
            cell.textView = (TextView) cellView.findViewById(R.id.row_title);
            cell.imageView = (ImageView) cellView.findViewById(R.id.row_icon);
            cellView.setTag(cell);
        } else {
            cell = (Cell) cellView.getTag();
        }

        cell.textView.setText(country.getmName());

        String drawableName = "flag_" + country.getmCode().toLowerCase(Locale.ENGLISH);
        int drawableId = getResId(drawableName);
        country.setmFlag(drawableId);
        cell.imageView.setImageResource(drawableId);
        return cellView;
    }

    static class Cell {
        public TextView textView;
        public ImageView imageView;
    }
}
