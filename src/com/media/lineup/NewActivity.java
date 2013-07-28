package com.media.lineup;
 import android.app.Activity;
 import android.content.*;
 import android.content.res.AssetFileDescriptor;
 import android.content.res.Resources;
 import android.graphics.Bitmap;
 import android.graphics.Canvas;
 import android.graphics.Paint;
 import android.graphics.Rect;
 import android.graphics.drawable.Drawable;
 import android.media.*;
 import android.net.Uri;
 import android.os.Bundle;
 import android.os.Vibrator;
 import android.util.*;
 import android.widget.AdapterView.OnItemClickListener;
 import android.view.Menu;
 import android.view.MenuInflater;
 import android.view.MenuItem;
 import android.view.View;
 import android.view.MotionEvent;
 import android.view.ViewGroup.*;
 import android.widget.*;
 import java.io.*;
 import java.net.*;
 import java.util.Map;
 import java.util.Date;
 import java.util.Calendar;
 import java.text.DateFormat;
 import java.util.ArrayList;
 import android.database.Cursor;
 import android.app.DatePickerDialog;
 import android.app.Dialog;
 import android.app.AlertDialog;
 import android.widget.EditText;

public class NewActivity
 extends Activity {
  private static final String tag = "com.media.lineup.NewActivity";
  // members
  Long selected_begin_time;
  static final int DATE_DIALOG_ID = 0;
  static final int UI_RELATIVE = 0;
  static final int UI_ABSOLUTE = 0;
  private static final int TIME_PICKER_INTERVAL=15;
  private int ui_switch = UI_RELATIVE;
  private Cursor locations_cursor, persons_cursor;
  private int picked_year,picked_month,picked_day,picked_hour,picked_minute;
  private String selected_person, selected_location;
  private int selected_person_position, selected_location_position;
  private String description, name;
  private int today_year, today_day, today_month;
  private Boolean restore;
  private ViewFlipper flipper;

  // functions
  private TimePicker.OnTimeChangedListener null_changed_listener =
    new TimePicker.OnTimeChangedListener() {
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
      // for updates to avoid stackoverflows etc
    }
  };
  private DatePicker.OnDateChangedListener null_date_changed_listener =
    new DatePicker.OnDateChangedListener() {
    public void onDateChanged(DatePicker view, int year, int month, int day) {
      // for updates to avoid stackoverflows etc
    }
  };
  private TimePicker.OnTimeChangedListener begin_time_changed_listener =
    new TimePicker.OnTimeChangedListener() {
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
      ui_switch = UI_ABSOLUTE;
      picked_hour = hourOfDay;
      picked_minute = minute;
      //Log.i("BeginTimeChanged",Integer.toString(picked_hour));
      updateRelativeTimeDisplay(view, hourOfDay, minute);
    }
  };
  private TimePicker.OnTimeChangedListener begin_time_relative_changed_listener =
    new TimePicker.OnTimeChangedListener() {
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
      updateAbsoluteTimeDisplay(view,  hourOfDay, minute);
      ui_switch = UI_RELATIVE;
    }
  };
  private DatePicker.OnDateChangedListener begin_date_changed_listener =
    new DatePicker.OnDateChangedListener() {
    public void onDateChanged(DatePicker view, int year, int month, int day) {
      //updateAbsoluteTimeDisplay(view,  hourOfDay, minute);
      //ui_switch = UI_RELATIVE;
      picked_year = year;
      picked_month = month;
      picked_day = day;
      updateRelativeTimeDisplay((TimePicker) findViewById(R.id.begin_time), picked_hour, picked_minute);
    }
  };
  public void updateAbsoluteTimeDisplay(TimePicker timePicker, int hourOfDay, int minute) {
    DatePicker begin_date = (DatePicker) findViewById(R.id.begin_date);
    TimePicker begin_time = (TimePicker) findViewById(R.id.begin_time);
    TimePicker begin_time_relative = (TimePicker) findViewById(R.id.begin_time_relative);
    final Calendar now_date = Calendar.getInstance();


    begin_time_relative.setOnTimeChangedListener(null_changed_listener);
    // do calculation of next time
    if (minute%TIME_PICKER_INTERVAL!=0){
        int minuteFloor=minute-(minute%TIME_PICKER_INTERVAL);
        minute=minuteFloor + (minute==minuteFloor+1 ? TIME_PICKER_INTERVAL : 0);
        if (minute==60){
            begin_time_relative.setCurrentHour((hourOfDay + 1)%23);
            hourOfDay+=1;
            minute=0;
        }
    }
    begin_time_relative.setCurrentMinute(minute%60);
    begin_time_relative.setOnTimeChangedListener(begin_time_relative_changed_listener);

    now_date.add(Calendar.HOUR_OF_DAY,hourOfDay);
    now_date.add(Calendar.MINUTE,minute);
    picked_hour = now_date.get(Calendar.HOUR_OF_DAY);
    picked_minute = now_date.get(Calendar.MINUTE);
    //case the offset switches days
    picked_year = now_date.get(Calendar.YEAR);
    picked_month = now_date.get(Calendar.MONTH);
    picked_day = now_date.get(Calendar.DATE);
    //Log.i("updateAbsoluteTimeDisplay",Integer.toString(picked_hour));
    //new Exception().printStackTrace();

    begin_time.setOnTimeChangedListener(null_changed_listener);
    begin_time.setCurrentHour(picked_hour);
    begin_time.setCurrentMinute(picked_minute);
    begin_time.setOnTimeChangedListener(begin_time_changed_listener);
    begin_date.init(2012,7,1,null_date_changed_listener);
    begin_date.init(picked_year,picked_month,picked_day,begin_date_changed_listener);
    updateBegin();
  }
  public void updateRelativeTimeDisplay(TimePicker timePicker, int hourOfDay, int minute) {
    TimePicker begin_time = (TimePicker) findViewById(R.id.begin_time);
    TimePicker begin_time_relative = (TimePicker) findViewById(R.id.begin_time_relative);
    final Calendar now = Calendar.getInstance();
    final Calendar given = Calendar.getInstance();
    given.set(Calendar.YEAR,picked_year);
    given.set(Calendar.MONTH,picked_month);
    given.set(Calendar.DAY_OF_MONTH,picked_day);
    given.set(Calendar.HOUR_OF_DAY,hourOfDay);
    given.set(Calendar.MINUTE,minute);
    Long dist = (given.getTimeInMillis() - now.getTimeInMillis()) / 1000L;
    Long dhours = dist /  3600;
    Long dminutes = dist /  60 - dhours * 60;
    Log.i(tag,Long.toString(dist));
    Log.i(tag,Long.toString(dhours));
    Log.i(tag,Long.toString(dminutes));
    Boolean enable =  dhours >=0 && dhours < 24 && dminutes >= 0;
    begin_time_relative.setFocusable(enable);
    begin_time_relative.setClickable(enable);
    begin_time_relative.setEnabled(enable);
    if ( enable ){

      //Log.i("updateRelativeTimeDisplay",Integer.toString(picked_hour));
      begin_time_relative.setOnTimeChangedListener(null_changed_listener);
      begin_time_relative.setCurrentHour(dhours.intValue()%23);
      begin_time_relative.setCurrentMinute(dminutes.intValue()%60);

      begin_time_relative.setOnTimeChangedListener(begin_time_relative_changed_listener);
    }
    updateBegin();
  }
  public void updateBegin(){
    TextView begin_time_display = (TextView) findViewById(R.id.begin_time_display);
    TextView begin_time_display_info = (TextView) findViewById(R.id.begin_time_display_info);
    TextView begin_date_display = (TextView) findViewById(R.id.begin_date_display);
    TimePicker begin_time = (TimePicker) findViewById(R.id.begin_time);
    TimePicker begin_time_relative = (TimePicker) findViewById(R.id.begin_time_relative);
    ModelAdapter.setNow();
    Calendar selectedDate = Calendar.getInstance();
    selectedDate.set(Calendar.YEAR,picked_year);
    selectedDate.set(Calendar.MONTH,picked_month);
    selectedDate.set(Calendar.DAY_OF_MONTH,picked_day);
    selectedDate.set(Calendar.HOUR_OF_DAY,picked_hour);
    selectedDate.set(Calendar.MINUTE,picked_minute);

    selected_begin_time = selectedDate.getTime().getTime() / 1000L;
    //Log.i("ux",selectedDate.toString());
    begin_time_display.setText(ModelAdapter.fDelta(selected_begin_time));
    begin_date_display.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(selectedDate.getTime()));
    begin_time_display_info.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(selectedDate.getTime()) + "\n" + ModelAdapter.fDelta(selected_begin_time));

    /*
    Boolean enable =  today_year == picked_year && today_month==picked_month && today_day == picked_day;
    begin_time_relative.setFocusable(enable);
    begin_time_relative.setClickable(enable);
    begin_time_relative.setEnabled(enable);
    */
  }
  public void updateName(){
    EditText  edit_name= (EditText) findViewById(R.id.name);
    Spinner   locations= (Spinner) findViewById(R.id.locations);
    Spinner   persons = (Spinner)  findViewById(R.id.persons);
    if ( locations.getSelectedItem() == null ) return;
    if ( persons.getSelectedItem() == null ) {
      selected_location = ((Cursor)locations.getSelectedItem()).getString(1);
      edit_name.setText( ((Cursor)locations.getSelectedItem()).getString(1));
      return;
    }
    selected_location = ((Cursor)locations.getSelectedItem()).getString(1);
    selected_person = ((Cursor)persons.getSelectedItem()).getString(1);
    edit_name.setText( selected_location + " / " + selected_person );
  }
  public void updateSpinner(){
    ModelAdapter m = new ModelAdapter(this);
    m.setupBaseData();
    m.openToRead();
    if ( persons_cursor != null )
      persons_cursor.close();
    if ( locations_cursor != null )
      locations_cursor.close();
    persons_cursor = m.queryPersons();
    locations_cursor = m.queryLocations();
    Spinner persons = (Spinner) findViewById(R.id.persons);
    Spinner locations = (Spinner) findViewById(R.id.locations);
    SimpleCursorAdapter persons_adapter = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item,persons_cursor, new String[] {"name"}, new int[] {android.R.id.text1});
    persons_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    persons.setAdapter(persons_adapter);
    persons.setOnItemSelectedListener(person_changed_listener);
    SimpleCursorAdapter locations_adapter = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item,locations_cursor, new String[] {"name"}, new int[] {android.R.id.text1});
    locations_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    locations.setAdapter(locations_adapter);
    locations.setOnItemSelectedListener(location_changed_listener);
    m.close();
    locations.setSelection(selected_location_position);
    persons.setSelection(selected_person_position);
  }
  private AdapterView.OnItemSelectedListener person_changed_listener = new AdapterView.OnItemSelectedListener() {
    public void onItemSelected(AdapterView parent, View v, int position, long id)
    {
      selected_person_position = position;
      updateName();
    }
    public void onNothingSelected(AdapterView<?> adapterView) {}
  };
  private AdapterView.OnItemSelectedListener location_changed_listener = new AdapterView.OnItemSelectedListener() {
    public void onItemSelected(AdapterView parent, View v, int position, long id)
    {
      selected_location_position = position;
      updateName();
    }
    public void onNothingSelected(AdapterView<?> adapterView) {}
  };

  public void  initializeControls()
  {//START
    //PROCESS
    flipper = (ViewFlipper) findViewById(R.id.new_flipper);
    TimePicker begin_time = (TimePicker) findViewById(R.id.begin_time);
    TimePicker begin_time_relative = (TimePicker) findViewById(R.id.begin_time_relative);
    DatePicker begin_date = (DatePicker) findViewById(R.id.begin_date);

    begin_time.setIs24HourView(true);
    begin_time_relative.setIs24HourView(true);

    if ( !restore ){
      final Calendar bt = Calendar.getInstance();
      bt.add(Calendar.MINUTE,30);
      picked_hour = bt.get(Calendar.HOUR_OF_DAY);
      picked_minute = bt.get(Calendar.MINUTE);
    }
    begin_time.setCurrentHour(picked_hour);
    begin_time.setCurrentMinute(picked_minute);
    //Log.i("init",Integer.toString(picked_hour));
    begin_time.setOnTimeChangedListener(begin_time_changed_listener);
    begin_time_relative.setOnTimeChangedListener(begin_time_relative_changed_listener);
    final Calendar c = Calendar.getInstance();
    today_year = c.get(Calendar.YEAR);
    today_month = c.get(Calendar.MONTH);
    today_day = c.get(Calendar.DAY_OF_MONTH);
    if ( picked_year == 0 ){
      c.add(Calendar.MINUTE,30);
      picked_year = c.get(Calendar.YEAR);
      picked_month = c.get(Calendar.MONTH);
      picked_day = c.get(Calendar.DAY_OF_MONTH);
    }
    begin_date.init(picked_year,picked_month,picked_day,begin_date_changed_listener);
    updateRelativeTimeDisplay(begin_time_relative,picked_hour,picked_minute);

    updateSpinner();


    Button add_person = (Button) findViewById(R.id.add_person);
    add_person.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(NewActivity.this);
        alert.setTitle("Add New Person");
        alert.setMessage("Type the Name of a Person");
        // Set an EditText view to get user input
        final EditText input = new EditText(NewActivity.this);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            String value = input.getText().toString();
            // Do something with value!
            ModelAdapter m = new ModelAdapter(getApplicationContext());
            m.openToWrite();
            m.insertPerson(value,0);
            m.close();
            updateSpinner();
          }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int whichButton) {
          // Canceled.
          }
        });
        alert.show();
      }
    });
    Button add_location = (Button) findViewById(R.id.add_location);
    add_location.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(NewActivity.this);
        alert.setTitle("Add New Location");
        alert.setMessage("Type the Name of a Location");
        // Set an EditText view to get user input
        final EditText input = new EditText(NewActivity.this);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            String value = input.getText().toString();
            // Do something with value!
            ModelAdapter m = new ModelAdapter(getApplicationContext());
            m.openToWrite();
            m.insertLocation(value,0);
            m.close();
            updateSpinner();
          }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int whichButton) {
          // Canceled.
          }
        });
        alert.show();
        }
    });
    ((Button) findViewById(R.id.finish_activity)).setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            finishActivity();
        }
    });
    ((Button) findViewById(R.id.finish_quick)).setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            finishActivity();
        }
    });
    ((Button) findViewById(R.id.next_select_time)).setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            nextPage();
        }
    });
    ((Button) findViewById(R.id.next_add_info)).setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            nextPage();
        }
    });
    ((Button) findViewById(R.id.prev_select_location)).setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            prevPage();
        }
    });
    ((Button) findViewById(R.id.prev_select_time)).setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            prevPage();
        }
    });
    updateBegin();
  }

  @Override
  public void  onCreate(   Bundle savedInstanceState)
  {//START
    super.onCreate(savedInstanceState);
    setContentView(R.layout.new_activity);
    picked_day = 0; picked_year = 0; picked_month = 0; picked_hour = 0; picked_minute = 0;
    restore = false;
    if (savedInstanceState!=null){
      picked_day = savedInstanceState.getInt("picked_day");
      picked_month = savedInstanceState.getInt("picked_month");
      picked_year = savedInstanceState.getInt("picked_year");
      picked_hour = savedInstanceState.getInt("picked_hour");
      picked_minute = savedInstanceState.getInt("picked_minute");
      selected_location = savedInstanceState.getString("selected_location");
      selected_person = savedInstanceState.getString("selected_person");
      selected_person_position = savedInstanceState.getInt("selected_person_position");
      selected_location_position = savedInstanceState.getInt("selected_location_position");
      description = savedInstanceState.getString("description");
      name = savedInstanceState.getString("name");
      restore = true;
    }
    //Log.i("restored",Integer.toString(picked_year) + "/" + Integer.toString(picked_month) + "/" + Integer.toString(picked_day) + " " +  Integer.toString(picked_hour) + ":" + Integer.toString(picked_minute));
    initializeControls();
  }
  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    return;
  }
  @Override
  protected void onSaveInstanceState(Bundle savedInstanceState) {
    EditText  edit_name= (EditText) findViewById(R.id.name);
    EditText  edit_description= (EditText) findViewById(R.id.description);
    savedInstanceState.putInt("picked_day", picked_day);
    savedInstanceState.putInt("picked_month", picked_month);
    savedInstanceState.putInt("picked_year", picked_year);
    savedInstanceState.putInt("picked_hour", picked_hour);
    savedInstanceState.putInt("picked_minute", picked_minute);
    savedInstanceState.putString("selected_location",selected_location);
    savedInstanceState.putString("selected_person",selected_person);
    savedInstanceState.putInt("selected_location_position", selected_location_position);
    savedInstanceState.putInt("selected_person_position", selected_person_position);
    savedInstanceState.putString("description",edit_description.getText().toString());
    savedInstanceState.putString("name",edit_name.getText().toString());
    if ( persons_cursor != null )
      persons_cursor.close();
    if ( locations_cursor != null )
      locations_cursor.close();
    super.onSaveInstanceState(savedInstanceState);
  }


  @Override
  public boolean onCreateOptionsMenu(   Menu menu)
  {//START

    //PROCESS
    MenuInflater inflater = getMenuInflater();
  inflater.inflate(R.menu.new_activity, menu);
  return true;


  }

  @Override
  public boolean onOptionsItemSelected(   MenuItem menu_item)
  {//START

    //PROCESS
    switch (menu_item.getItemId()) {
      case R.id.finishActivity:
          finishActivity();
          return true;
      case R.id.help:
          {
          final Intent helpActivityIntent = new Intent(getApplicationContext(), HelpActivity.class);
          startActivityForResult(helpActivityIntent, 0);
          }
          return true;
      case R.id.preferences:
          {
          Intent preferencesActivityIntent = new Intent(getApplicationContext(), PreferencesActivity.class);
          startActivityForResult(preferencesActivityIntent, 0);
          }
          return true;
      default:
          return super.onOptionsItemSelected(menu_item);
    }
  }

  @Override
  public void  onPause()
  {//START

    //PROCESS
    super.onPause();

  }

  @Override
  public void  onResume()
  {//START

    //PROCESS
    super.onResume();

  }
  private void nextPage(){
    flipper.setInAnimation(AnimationHelper.inFromRightAnimation());
    flipper.setOutAnimation(AnimationHelper.outToLeftAnimation());
    flipper.showNext();
  }
  private void prevPage(){
    flipper.setInAnimation(AnimationHelper.inFromLeftAnimation());
    flipper.setOutAnimation(AnimationHelper.outToRightAnimation());
    flipper.showPrevious();
  }
  private DatePickerDialog.OnDateSetListener date_picker_listener =
    new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            picked_year  = year;
            picked_month = monthOfYear;
            picked_day   = dayOfMonth;
            updateBegin();
        }
    };

  @Override
  protected Dialog onCreateDialog(int id) {
    switch (id) {
    case DATE_DIALOG_ID:
        return new DatePickerDialog(this,date_picker_listener,picked_year, picked_month, picked_day);
    }
    return null;
  }


  public void  preferences()
  {//START

    //PROCESS
    Intent preferencesActivityIntent = new Intent(getApplicationContext(), PreferencesActivity.class);
    startActivityForResult(preferencesActivityIntent, 0);


  }
  public void finishActivity()
  {

    ModelAdapter m = new ModelAdapter(this);
    m.openToWrite();
    EditText  edit_name= (EditText) findViewById(R.id.name);
    EditText  edit_description= (EditText) findViewById(R.id.description);
    ArrayList<String> locations= new ArrayList<String>();
    if ( selected_location == null )
      locations.add("Location");
    else
      locations.add(selected_location);
    ArrayList<String> persons= new ArrayList<String>();
    if ( selected_person == null )
      persons.add("Person");
    else
      persons.add(selected_person);

    m.insertEvent(selected_begin_time
                 ,selected_begin_time+3600
                 ,edit_name.getText().toString()
                 ,edit_description.getText().toString()
                 ,0
                 , locations
                 , persons
                 );
    m.close();
    if ( persons_cursor != null )
      persons_cursor.close();
    if ( locations_cursor != null )
      locations_cursor.close();
    Toast.makeText( getApplicationContext()
                  , getString(R.string.success_save)
            + "\n"
            + ModelAdapter.fDelta(selected_begin_time)
            + "\n"
            + edit_name.getText().toString()
          , Toast.LENGTH_LONG).show();
    finish();
    final Intent selectActivityIntent = new Intent(getApplicationContext(), SelectActivity.class);
    selectActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
    startActivity(selectActivityIntent);
  }

  public void showPickDate()
  {
    showDialog(DATE_DIALOG_ID);
  }


}
