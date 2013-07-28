package com.media.lineup;
 import android.app.Activity;
 import android.app.AlertDialog;
 import android.app.ProgressDialog;
 import android.content.*;
 import android.content.res.Resources;
 import android.graphics.Bitmap;
 import android.graphics.Canvas;
 import android.graphics.Paint;
 import android.graphics.Rect;
 import android.graphics.drawable.Drawable;
 import android.media.*;
 import android.net.Uri;
 import android.os.Bundle;
 import android.os.Environment;
 import android.os.Handler;
 import android.os.Message;
 import android.preference.*;
 import android.util.*;
 import android.view.LayoutInflater;
 import android.view.Menu;
 import android.view.ContextMenu;
 import android.view.ContextMenu.*;
 import android.view.MenuInflater;
 import android.view.MenuItem;
 import android.view.View;
 import android.view.ViewGroup;
 import android.view.ViewGroup.*;
 import android.view.WindowManager;
 import android.widget.*;
 import java.io.*;
 import java.io.IOException;
 import java.lang.Exception;
 import java.lang.InterruptedException;
 import java.lang.Thread;
 import java.net.*;
 import java.text.DateFormat;
 import java.text.ParseException;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.Map;
 import javax.xml.parsers.*;
 import org.apache.http.client.*;
 import org.apache.http.client.HttpClient;
 import org.apache.http.client.ResponseHandler;
 import org.apache.http.client.methods.*;
 import org.apache.http.impl.client.*;
 import org.w3c.dom.*;
 import org.xml.sax.*;
 import android.database.Cursor;

public class SelectActivity
 extends ActivityForm {
    // members
  public ProgressDialog progress; //typeref/documentation
  public Long selected_event_id;
  private static final String tag = "com.media.lineup.SelectActivity";
  private static final int L_FUTURE = 1;
  private static final int L_FUTURE_MY = 2;
  private static final int L_PAST = 3;
  private static final int L_NOW = 4;
  private static final int L_ALL = 5;

  private int list_source = L_NOW;
  private Cursor list_cursor;
    // functions
  
  /**************************************************************************//**
  \brief  loadSettings - 
  *******************************************************************************
\brief  default - 



*******************************************************************************
******************************************************************************/

  public void  loadSettings()
  {//START
    
    //PROCESS
    settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
  }

  
  /**************************************************************************//**
  \brief  onCreate - 
  *******************************************************************************
\brief  default - 



\param  savedInstanceState - 
*******************************************************************************
******************************************************************************/

  @Override
  public void  onCreate(   Bundle savedInstanceState)
  {//START
    Bundle extras;
    Toast toast;
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    //PROCESS
    super.onCreate(savedInstanceState);
    setContentView(R.layout.select_activity);
    updateList();
    ListView lv = (ListView)findViewById(R.id.list);
    lv.setTextFilterEnabled(true);
    registerForContextMenu(lv);

    lv.setOnItemClickListener(new ListView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView event_id_column = (TextView)view.findViewById(R.id.event_id); 
        TextView active_column = (TextView)view.findViewById(R.id.active_state); 
        int active = 0;
        Log.i(".",active_column.getText().toString());
        if ( active_column.getText().toString().equals("1") ) active = 1;
        String event_id_text = event_id_column.getText().toString();
        Long event_id = Long.valueOf(event_id_text);

        ModelAdapter m = new ModelAdapter(getApplicationContext());
        m.openToWrite();
        ImageView inactive_image = (ImageView)view.findViewById(R.id.inactive);
        ImageView active_image = (ImageView)view.findViewById(R.id.active);
        if ( active == 0 ){
          inactive_image.setVisibility(View.GONE);
          active_image.setVisibility(View.VISIBLE);
          m.activateEvent(event_id);
          active = 1;
        }else{
          active_image.setVisibility(View.GONE);
          inactive_image.setVisibility(View.VISIBLE);
          Log.i("",Long.toString(m.deactivateEvent(event_id)));
          active = 0;
        }
        m.closeWrite();
        m.close();
        active_column.setText(Integer.toString(active));
        UpdateEventDisplayService.setEventsUpdated();
      }
    });

    Button new_event = (Button) findViewById(R.id.new_event);
    new_event.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            newActivity();
        }
    });

    Button filter_now = (Button) findViewById(R.id.filter_now);
    filter_now.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            list_source = L_NOW;
            updateList();
        }
    });
    Button filter_future = (Button) findViewById(R.id.filter_future);
    filter_future.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            list_source = L_FUTURE;
            updateList();
        }
    });
    Button filter_future_my = (Button) findViewById(R.id.filter_future_my);
    filter_future_my.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            list_source = L_FUTURE_MY;
            updateList();
        }
    });
    Button filter_past = (Button) findViewById(R.id.filter_past);
    filter_past.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
            list_source = L_PAST;
            updateList();
        }
    });

  }

  
  /**************************************************************************//**
  \brief  onCreateOptionsMenu - 
  *******************************************************************************
\brief  default - 



\param  menu - 
*******************************************************************************
\return cond - 
  ******************************************************************************/

  @Override
  public boolean onCreateOptionsMenu(   Menu menu)
  {//START
    
    //PROCESS
    return super.onCreateOptionsMenu(R.menu.select, menu);
  
  
  }

  @Override
  public void  onCreateContextMenu(   ContextMenu context_menu, View view, ContextMenuInfo context_menu_info)
  {//START
    
    //PROCESS
    super.onCreateContextMenu(context_menu, view, context_menu_info);
  MenuInflater inflater = getMenuInflater();
  inflater.inflate(R.menu.select_list_context, context_menu);
  
  
  }

  
  /**************************************************************************//**
  \brief  onDraw - 
  *******************************************************************************
\brief  default - 



\param  canvas - 
*******************************************************************************
******************************************************************************/

  public void  onDraw(   Canvas canvas)
  {//START
    //super.onDraw(canvas);
    //PROCESS
    //showLevel();
  
  }

  
  /**************************************************************************//**
  \brief  onOptionsItemSelected - 
  *******************************************************************************
\brief  default - 



\param  menu_item - 
*******************************************************************************
\return cond - 
  ******************************************************************************/

  @Override
  public boolean onOptionsItemSelected(   MenuItem menu_item)
  {//START
    
    //PROCESS
    switch (menu_item.getItemId()) {
    case R.id.newItem:
        newActivity();
        return true;
    case R.id.preferences:
        preferences();
        return true;
    case R.id.main:
        mainActivity();
        return true;
    case R.id.showMap:
        showMap();
        return true;
    case R.id.help:
        showHelp();
        return true;
    default:
        return super.onOptionsItemSelected(menu_item);
  }
  
  }

  @Override
  public boolean onContextItemSelected(   MenuItem menu_item)
  {//START
    AdapterView.AdapterContextMenuInfo info =
    (AdapterView.AdapterContextMenuInfo)menu_item.getMenuInfo();
    ListView lv = (ListView)findViewById(R.id.list);
    Cursor c = ((SelectListCursorAdapter)lv.getAdapter()).getCursor();
    c.moveToPosition(info.position);
    selected_event_id = c.getLong(0);
    switch (menu_item.getItemId()) {
    case R.id.newItem:
        newActivity();
        return true;
    case R.id.sendItem:
        //sendActivity();
        return true;
    case R.id.viewItem:
        //sendActivity();
        viewItem();
        return true;
    case R.id.deleteItem:
        deleteItem();
        return true;
    }
  return super.onContextItemSelected(menu_item);
  
  }

  
  /**************************************************************************//**
  \brief  onPause - 
  *******************************************************************************
\brief  default - 



*******************************************************************************
******************************************************************************/

  public void  onPause()
  {//START
    super.onPause();
    //PROCESS
    //if( checkModified() ) save();
  
  
  }

  
  /**************************************************************************//**
  \brief  onResume - 
  *******************************************************************************
\brief  default - 



*******************************************************************************
******************************************************************************/

  @Override
  public void  onResume()
  {//START
    super.onResume();
    //PROCESS
    loadSettings();
    updateList();
  }


  public void updateList(){
    ModelAdapter m = new ModelAdapter(this);
    m.openToRead();
    if ( list_cursor!=null) list_cursor.close();
    ((ToggleButton) findViewById(R.id.filter_now)).setChecked(false);
    ((ToggleButton) findViewById(R.id.filter_future)).setChecked(false);
    ((ToggleButton) findViewById(R.id.filter_future_my)).setChecked(false);
    ((ToggleButton) findViewById(R.id.filter_past)).setChecked(false);
    switch( list_source ){
      case L_FUTURE:
        list_cursor = m.queryAllFutures();
        ((ToggleButton) findViewById(R.id.filter_future)).setChecked(true);
        break;
      case L_FUTURE_MY:
        list_cursor = m.queryAllMyFutures();
        ((ToggleButton) findViewById(R.id.filter_future_my)).setChecked(true);
        break;
      case L_PAST:
        list_cursor = m.queryAllPast();
        ((ToggleButton) findViewById(R.id.filter_past)).setChecked(true);
        break;
      case L_NOW:
        list_cursor = m.queryAllNow();
        ((ToggleButton) findViewById(R.id.filter_now)).setChecked(true);
        break;
      case L_ALL:
      default:
        list_cursor = m.queryAll();
    }

    SelectListCursorAdapter adapter = new SelectListCursorAdapter(
      getApplicationContext(),
      list_cursor
    );
    ListView lv = (ListView)findViewById(R.id.list);
    lv.setAdapter(adapter);
    m.close();
  }

  /**************************************************************************//**
  \brief  onStop - 
  *******************************************************************************
\brief  default - 



*******************************************************************************
******************************************************************************/

  public void  onStop()
  {//START
    super.onStop();
    //PROCESS
    //if( checkModified() ) save();
  
  
  }

  
  /**************************************************************************//**
  \brief  preferences - 
  *******************************************************************************
\brief  default - 



*******************************************************************************
******************************************************************************/
  public void deleteItem(){
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Are you sure you want to Delete?")
           .setCancelable(false)
           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                 ModelAdapter m = new ModelAdapter(getApplicationContext());
                 m.openToWrite();
                 m.deleteEvent(selected_event_id);
                 m.close();
                 updateList();
               }
           })
           .setNegativeButton("No", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
               }
           });
    AlertDialog alert = builder.create();
    alert.show();
  }
  public void viewItem(){
  }

  public void  preferences()
  {//START
    
    //PROCESS
    Intent preferencesActivityIntent = new Intent(getApplicationContext(), PreferencesActivity.class);
  startActivityForResult(preferencesActivityIntent, 0);
  
  
  }

  public void  mainActivity()
  {//START
    
    //PROCESS
    Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
    startActivityForResult(mainActivityIntent, 0);
  }
  public void  newActivity()
  {//START
    
    //PROCESS
    Intent newActivityIntent = new Intent(getApplicationContext(), NewActivity.class);
    startActivityForResult(newActivityIntent, 0);
  }

  /**************************************************************************//**
  \brief  send - 
  *******************************************************************************
\brief  default - 



*******************************************************************************
******************************************************************************/

  public void  send()
  {//START
    Map<String, String> string_map;
  String bibtex_string, bibtex_note, filename, subject;
    //PROCESS
    /*
    try {
    string_map = formToValueMap();
    bibtex_note = string_map.get("user_note");
    if(bibtex_note == null) bibtex_note="";
    filename = bibtexFilename(string_map);
    subject = "[" + send_email_subject + "]";
    subject = subject.replace("_IDENTIFIER_",string_map.get("identifier"));
    subject = subject.replace("_TITLE_",string_map.get("title"));
    
    //Log.i("sendBibtex:s",subject);
    //Log.i("sendBibtex:n",bibtex_note);
    //Log.i("sendBibtex:f",filename);
    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
    //config
    //from stettings? 
    String[] receiver = { send_email_receiver };
    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, receiver );
    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, bibtex_note);
    emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse("file:///data/data/com.media.bibliopilia/files/"+filename));
    emailIntent.setType("text/plain");
    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
  } catch (pException e){
    Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
    toast.show();
  }
  */
  }

  
  /**************************************************************************//**
  \brief  showHelp - 
  *******************************************************************************
\brief  default - 



*******************************************************************************
******************************************************************************/

  public void  showHelp()
  {//START
    
    //PROCESS
    final Intent helpActivityIntent = new Intent(getApplicationContext(), HelpActivity.class);
  startActivityForResult(helpActivityIntent, 0);
  
  
  }
  public void  showMap()
  {//START
    
    //PROCESS
    final Intent mapActivityIntent = new Intent(getApplicationContext(), MapActivity.class);
  startActivityForResult(mapActivityIntent, 0);
  
  
  }

  
  /**************************************************************************//**
  \brief  showList - 
  *******************************************************************************
\brief  default - 



*******************************************************************************
******************************************************************************/

  public void  showList()
  {//START
    
    //PROCESS
//    final Intent listActivityIntent = new Intent(getApplicationContext(), listActivity.class);
//  listActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
//  startActivity(listActivityIntent);
  
  
  }

  
}