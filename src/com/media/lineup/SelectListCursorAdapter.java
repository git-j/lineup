package com.media.lineup;

import android.widget.CursorAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import android.widget.*;
import android.view.*;
import android.content.Context;
import android.database.Cursor;
import java.util.Date;

public class SelectListCursorAdapter extends CursorAdapter {
  private DateFormat dateFormat = new SimpleDateFormat("MM-dd\nHH:mm");
  public SelectListCursorAdapter(Context context, Cursor c) {
    super(context, c);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    TextView display_text_column = (TextView)view.findViewById(R.id.display_text);
    TextView begin_column = (TextView)view.findViewById(R.id.begin);
    TextView event_id_column = (TextView)view.findViewById(R.id.event_id);
    TextView active_column = (TextView)view.findViewById(R.id.active_state);
    event_id_column.setVisibility(View.GONE);
    active_column.setVisibility(View.GONE);

    event_id_column.setText(cursor.getString(cursor.getColumnIndex("_id")));
    Long begin = cursor.getLong(cursor.getColumnIndex("begin"));

    Date begin_date = new Date(begin*1000L);
    int active = cursor.getInt(cursor.getColumnIndex("active"));
    int imported =  cursor.getInt(cursor.getColumnIndex("imported"));

    active_column.setText(Integer.toString(active));
    display_text_column.setText(cursor.getString(cursor.getColumnIndex("display_text")));
    begin_column.setText(dateFormat.format(begin_date) + "\n" + ModelAdapter.fDeltaSimple(begin));
    if ( imported == 0 ){
      display_text_column.setTypeface(android.graphics.Typeface.DEFAULT_BOLD,android.graphics.Typeface.BOLD);
    }

    ImageView inactive_image = (ImageView)view.findViewById(R.id.inactive);
    ImageView active_image = (ImageView)view.findViewById(R.id.active);
    if ( active > 0 ){
      inactive_image.setVisibility(View.GONE);
      active_image.setVisibility(View.VISIBLE);
    }else{
      active_image.setVisibility(View.GONE);
      inactive_image.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View v = inflater.inflate(R.layout.select_list_item, parent, false);
    bindView(v, context, cursor);
    return v;
  }
}

