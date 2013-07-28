package com.media.lineup;
 import android.app.Activity;
 import android.app.AlertDialog;
 import android.app.ProgressDialog;
 import android.content.*;
 import android.content.res.Resources;
 import android.graphics.*;
 import android.graphics.Bitmap;
 import android.graphics.Canvas;
 import android.graphics.Color;
 import android.graphics.Paint;
 import android.graphics.Rect;
 import android.graphics.drawable.Drawable;
 import android.media.*;
 import android.os.Bundle;
 import android.os.Handler;
 import android.os.Message;
 import android.preference.*;
 import android.util.*;
 import android.view.ContextMenu;
 import android.view.ContextMenu.*;
 import android.view.LayoutInflater;
 import android.view.Menu;
 import android.view.MenuInflater;
 import android.view.MenuItem;
 import android.view.View;
 import android.view.View.*;
 import android.view.ViewGroup.*;
 import android.widget.*;
 import android.widget.AdapterView.*;
 import java.io.*;
 import java.net.*;
 import java.util.ArrayList;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.Map;

public class MainActivity
 extends ActivityForm {
    // members
  public boolean initialized = false; //typeref/documentation
  public SharedPreferences settings = null; //typeref/documentation
  public String curListItem; //typeref/documentation
  public String[] publication_files; //typeref/documentation
  public String[] publication_labels; //typeref/documentation
  public requestRemote remoteReceiver; //typeref/documentatio
  public Handler remoteHandler; //typeref/documentation

  private String remote_app_provider = "http://d0nt.de/lineup/2012/";
  private String remote_app_key = "linuxtag_schedule.xml";

    // functions

  /**************************************************************************//**
  \brief  initializeControls -
  *******************************************************************************
\brief  default -



*******************************************************************************
******************************************************************************/

  public void  initializeControls()
  {//START

    //PROCESS
    setContentView(R.layout.main_activity);
    if ( settings == null )
      settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    if ( settings.getBoolean("first_launch", true) ){
      settings.edit().putBoolean("first_launch", false).commit();
      initializeDefaults();
    }

  }


  /**************************************************************************//**
  \brief  onCreateOptionsMenu -
  *******************************************************************************
\brief  default -



\param  id -
\param  menu -
*******************************************************************************
\return cond -
  ******************************************************************************/

  @Override
  public boolean onCreateOptionsMenu( Menu menu)
  {//START
      boolean    cond;
    //PROCESS
    return super.onCreateOptionsMenu(R.menu.main, menu);
//    return(cond);
  }

  /**************************************************************************//**
  \brief  onOptionsItemSelected -
  *******************************************************************************
\brief  default -



\param  menu_item -
*******************************************************************************
\return cond -
  ******************************************************************************/
  private void initializeDefaults(){
    // todo ask for
    ModelAdapter m = new ModelAdapter(this);
    m.openToWrite();
    m.deleteImported();

    m.insertLocation("Arbeitsamt",1);
    m.insertLocation("Audio Couch",1);
    m.insertLocation("Base",1);
    m.insertLocation("Bassline Circus",1);
    m.insertLocation("Beat Manege",1);
    m.insertLocation("Cabarret",1);
    m.insertLocation("Casino",1);
    m.insertLocation("Datscha",1);
    m.insertLocation("Dubstation",1);
    m.insertLocation("Dub Station",1);
    m.insertLocation("Eclipse",1);
    m.insertLocation("Firespace",1);
    m.insertLocation("Hangarbuehne",1);
    m.insertLocation("Kalkutta",1);
    m.insertLocation("Kino",1);
    m.insertLocation("Kreuz des Ostens",1);
    m.insertLocation("Luftschloss",1);
    m.insertLocation("Neuland",1);
    m.insertLocation("Oase",1);
    m.insertLocation("Palast der Republik",1);
    m.insertLocation("Platz der Kossmonauten",1);
    m.insertLocation("Querfeld",1);
    m.insertLocation("Räuberhöhle",1);
    m.insertLocation("Robo Field",1);
    m.insertLocation("Rootsbase",1);
    m.insertLocation("Roter Platz",1);
    m.insertLocation("Safe Area",1);
    m.insertLocation("Salon de Baile",1);
    m.insertLocation("Seebühne",1);
    m.insertLocation("Shower Tower",1);
    m.insertLocation("Tanzwiese",1);
    m.insertLocation("Theater",1);
    m.insertLocation("Trancefloor",1);
    m.insertLocation("Tube",1);
    m.insertLocation("Turmbühne",1);
    m.insertLocation("Weidendom",1);
    m.insertLocation("Zentralkommitee",1);


    Long begin = 1340899200L;
    m.insertScheduleEvent(begin,begin+3600,"Start","descr",1,new ArrayList<String>(),new ArrayList<String>());
    m.insertScheduleEvent(begin+3600*96,begin+3600*97,"Doors Close","descr",1,new ArrayList<String>(),new ArrayList<String>());
    m.close();

    Toast.makeText( getApplicationContext()
                  , getString(R.string.success_save)
                  , Toast.LENGTH_LONG).show();

    /* may cause user never to use start again
    final Intent selectActivityIntent = new Intent(getApplicationContext(), SelectActivity.class);
    selectActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
    startActivity(selectActivityIntent);
    */
  }
  
  
  private void initialize3000(){
    ModelAdapter m = new ModelAdapter(this);
    m.openToWrite();
    m.deleteImported();
    //....
    Long now = System.currentTimeMillis();
    Date now_date = new Date(now);
    now_date.setHours(17);
    now_date.setMinutes(0);
    now = now_date.getTime() / 1000L;

    m.insertEvent(now,now,"Doors Open","descr",1,new ArrayList<String>(),new ArrayList<String>());
    ArrayList<String> locations = new ArrayList<String>();
    ArrayList<String> persons = new ArrayList<String>();
    // ZirkusZelt
    locations.clear();locations.add("Zirkuszelt");
    persons.clear();persons.add("Open Synth");
    m.insertScheduleEvent(now+3600*3,now+3600*4,"","descr",1,locations,persons);
    persons.clear();persons.add("Nick Hansen");
    m.insertScheduleEvent(now+3600*5,now+3600*7,"","descr",1,locations,persons);
    persons.clear();persons.add("German Lachs");
    m.insertScheduleEvent(now+3600*7,now+3600*9,"","descr",1,locations,persons);
    persons.clear();persons.add("Schäufler & Zovsky");
    m.insertScheduleEvent(now+3600*9,now+3600*10,"","descr",1,locations,persons);
    persons.clear();persons.add("Douglas Greed");
    m.insertScheduleEvent(now+3600*10,now+3600*12,"","descr",1,locations,persons);
    persons.clear();persons.add("D.Hörste vs Paul G on Sax");
    m.insertScheduleEvent(now+3600*12,now+3600*15,"","descr",1,locations,persons);
    persons.clear();persons.add("Neurotron");
    m.insertScheduleEvent(now+3600*15,now+3600*16,"","descr",1,locations,persons);
    persons.clear();persons.add("Slimcase");
    m.insertScheduleEvent(now+3600*16,now+3600*18,"","descr",1,locations,persons);

    persons.clear();persons.add("Herr Süss & Herr Sauer");
    m.insertScheduleEvent(now+3600*30,now+3600*33,"","descr",1,locations,persons);
    persons.clear();persons.add("Dreher & Smart");
    m.insertScheduleEvent(now+3600*33,now+3600*34,"","descr",1,locations,persons);
    persons.clear();persons.add("The Glitz");
    m.insertScheduleEvent(now+3600*34,now+3600*36,"","descr",1,locations,persons);
    persons.clear();persons.add("Microstern");
    m.insertScheduleEvent(now+3600*36,now+3600*37,"","descr",1,locations,persons);
    persons.clear();persons.add("Sportbrigarde Sparwasser");
    m.insertScheduleEvent(now+3600*37,now+3600*39,"","descr",1,locations,persons);
    persons.clear();persons.add("Kollektiv Ost");
    m.insertScheduleEvent(now+3600*39,now+3600*42,"","descr",1,locations,persons);

    locations.clear();locations.add("Am See");
    persons.clear();persons.add("DJ Funkmode");
    m.insertScheduleEvent(now+3600*0,now+3600*4,"","descr",1,locations,persons);

    persons.clear();persons.add("Kundan Lal");
    m.insertScheduleEvent(now+3600*20,now+3600*23,"","descr",1,locations,persons);
    persons.clear();persons.add("Rune Goldbutt");
    m.insertScheduleEvent(now+3600*23,now+3600*26,"","descr",1,locations,persons);
    persons.clear();persons.add("Olsen & The Hurley Sea");
    m.insertScheduleEvent(now+3600*26,now+3600*27,"","descr",1,locations,persons);
    persons.clear();persons.add("Digger Barnes");
    m.insertScheduleEvent(now+3600*29,now+3600*30,"","descr",1,locations,persons);

    locations.clear();locations.add("Waldbühne");
    persons.clear();persons.add("Balkantronika Soundsystem");
    m.insertScheduleEvent(now+3600*2,now+3600*4,"","descr",1,locations,persons);
    persons.clear();persons.add("DJ Delay");
    m.insertScheduleEvent(now+3600*4,now+3600*7,"","descr",1,locations,persons);
    persons.clear();persons.add("Apparatschik");
    m.insertScheduleEvent(now+3600*7,now+3600*8,"","descr",1,locations,persons);
    persons.clear();persons.add("Eule aka Done");
    m.insertScheduleEvent(now+3600*8,now+3600*12,"","descr",1,locations,persons);

    persons.clear();persons.add("Slare");
    m.insertScheduleEvent(now+3600*25,now+3600*26,"","descr",1,locations,persons);
    persons.clear();persons.add("Skaban & Käptn Peng");
    m.insertScheduleEvent(now+3600*26,now+3600*27,"","descr",1,locations,persons);
    persons.clear();persons.add("Slare");
    m.insertScheduleEvent(now+3600*27,now+3600*28,"","descr",1,locations,persons);
    persons.clear();persons.add("Hey O Hansen");
    m.insertScheduleEvent(now+3600*28,now+3600*29,"","descr",1,locations,persons);
    persons.clear();persons.add("Kombinat 100");
    m.insertScheduleEvent(now+3600*29,now+3600*30,"","descr",1,locations,persons);
    persons.clear();persons.add("Molle & Hansen");
    m.insertScheduleEvent(now+3600*30,now+3600*31,"","descr",1,locations,persons);
    persons.clear();persons.add("Marbert Rocel");
    m.insertScheduleEvent(now+3600*31,now+3600*33,"","descr",1,locations,persons);
    persons.clear();persons.add("Pophop");
    m.insertScheduleEvent(now+3600*33,now+3600*35,"","descr",1,locations,persons);

    persons.clear();persons.add("Madrose vs. die Maart");
    m.insertScheduleEvent(now+3600*43,now+3600*45,"","descr",1,locations,persons);
    persons.clear();persons.add("Air Cushion Finish");
    m.insertScheduleEvent(now+3600*45,now+3600*46,"","descr",1,locations,persons);
    persons.clear();persons.add("Mad Pitt vs Mad Wood");
    m.insertScheduleEvent(now+3600*46,now+3600*49,"","descr",1,locations,persons);
    persons.clear();persons.add("Chez Mieke");
    m.insertScheduleEvent(now+3600*49,now+3600*52,"","descr",1,locations,persons);
    persons.clear();persons.add("Ellenschneider");
    m.insertScheduleEvent(now+3600*52,now+3600*53,"","descr",1,locations,persons);


    locations.clear();locations.add("Utopia");
    persons.clear();persons.add("Der Gammler");
    m.insertScheduleEvent(now+3600*0,now+3600*3,"","descr",1,locations,persons);
    persons.clear();persons.add("Woman in the Reek");
    m.insertScheduleEvent(now+3600*3,now+3600*4,"","descr",1,locations,persons);
    persons.clear();persons.add("Mirrmirr & Flo/yd");
    m.insertScheduleEvent(now+3600*5,now+3600*6,"","descr",1,locations,persons);
    persons.clear();persons.add("Lonski & Classen");
    m.insertScheduleEvent(now+3600*7,now+3600*8,"","descr",1,locations,persons);
    persons.clear();persons.add("Bob Drop vs der Gammler");
    m.insertScheduleEvent(now+3600*8,now+3600*13,"","descr",1,locations,persons);
    persons.clear();persons.add("Commander Love");
    m.insertScheduleEvent(now+3600*13,now+3600*16,"","descr",1,locations,persons);
    persons.clear();persons.add("The Sorry Entertainer");
    m.insertScheduleEvent(now+3600*16,now+3600*20,"","descr",1,locations,persons);
    persons.clear();persons.add("Till Von Sein");
    m.insertScheduleEvent(now+3600*29,now+3600*22,"","descr",1,locations,persons);
    persons.clear();persons.add("Wareika");
    m.insertScheduleEvent(now+3600*22,now+3600*23,"","descr",1,locations,persons);
    persons.clear();persons.add("Gorge");
    m.insertScheduleEvent(now+3600*23,now+3600*25,"","descr",1,locations,persons);

    persons.clear();persons.add("Carsten Rausch & Ferdinand Laurin");
    m.insertScheduleEvent(now+3600*25,now+3600*26,"","descr",1,locations,persons);
    persons.clear();persons.add("Sven Dohse");
    m.insertScheduleEvent(now+3600*26,now+3600*28,"","descr",1,locations,persons);
    persons.clear();persons.add("Lula Circus");
    m.insertScheduleEvent(now+3600*28,now+3600*29,"","descr",1,locations,persons);
    persons.clear();persons.add("Nils Nilson");
    m.insertScheduleEvent(now+3600*29,now+3600*31,"","descr",1,locations,persons);
    persons.clear();persons.add("Soukie Windish");
    m.insertScheduleEvent(now+3600*31,now+3600*34,"","descr",1,locations,persons);
    persons.clear();persons.add("Feinfilter");
    m.insertScheduleEvent(now+3600*34,now+3600*35,"","descr",1,locations,persons);
    persons.clear();persons.add("Fango Packung");
    m.insertScheduleEvent(now+3600*35,now+3600*38,"","descr",1,locations,persons);
    persons.clear();persons.add("Signore Sereno");
    m.insertScheduleEvent(now+3600*38,now+3600*40,"","descr",1,locations,persons);
    persons.clear();persons.add("something wonderfully");
    m.insertScheduleEvent(now+3600*40,now+3600*41,"","descr",1,locations,persons);
    persons.clear();persons.add("Lutsche");
    m.insertScheduleEvent(now+3600*41,now+3600*43,"","descr",1,locations,persons);
    persons.clear();persons.add("Sebo & Madmotormiquel");
    m.insertScheduleEvent(now+3600*43,now+3600*45,"","descr",1,locations,persons);
    persons.clear();persons.add("Marcus Carp");
    m.insertScheduleEvent(now+3600*45,now+3600*47,"","descr",1,locations,persons);
    persons.clear();persons.add("Mollono.Bass");
    m.insertScheduleEvent(now+3600*47,now+3600*48,"","descr",1,locations,persons);
    persons.clear();persons.add("Monkey Mafia");
    m.insertScheduleEvent(now+3600*48,now+3600*50,"","descr",1,locations,persons);
    persons.clear();persons.add("Eulen:Haupt & Molle:NHauer");
    m.insertScheduleEvent(now+3600*50,now+3600*52,"","descr",1,locations,persons);


    m.insertScheduleEvent(now+3600*52,now+3600*52,"Doors Close","descr",1,new ArrayList<String>(),new ArrayList<String>());
    m.close();

    Toast.makeText( getApplicationContext()
                  , getString(R.string.success_save)
                  , Toast.LENGTH_LONG).show();

    final Intent selectActivityIntent = new Intent(getApplicationContext(), SelectActivity.class);
    selectActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
    startActivity(selectActivityIntent);
  }

  @Override
  public boolean onOptionsItemSelected(   MenuItem menu_item)
  {//START

    //PROCESS
    switch (menu_item.getItemId()) {
    case R.id.initializeExamples:
        initializeExample();
        return true;
    case R.id.help:
        {
        final Intent helpActivityIntent = new Intent(getApplicationContext(), HelpActivity.class);
        startActivityForResult(helpActivityIntent, 0);
        }
        return true;
    case R.id.storedItems:
        {
        final Intent editActivityIntent = new Intent(getApplicationContext(), SelectActivity.class);
        startActivityForResult(editActivityIntent, 0);
        }
        return true;
    case R.id.newItem:
        {
        final Intent newActivityIntent = new Intent(getApplicationContext(), NewActivity.class);
        startActivityForResult(newActivityIntent, 0);
        }
        return true;
    case R.id.showMap:
        {
        final Intent mapIntent = new Intent(getApplicationContext(), MapActivity.class);
        startActivityForResult(mapIntent, 0);
        }
        return true;
    case R.id.clearDB:
        {
          clearDatabase();
        }
        return true;
    case R.id.clearImported:
        {
          clearImported();
        }
        return true;
    case R.id.importRemote:
        {
          importRemote();
        }
        return true;
    case R.id.exportRemote:
        {
          exportRemote();
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

  void clearDatabase(){
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Are you sure you want to Delete ALL Data?")
           .setCancelable(false)
           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                 ModelAdapter m = new ModelAdapter(getApplicationContext());
                 m.openToWrite();
                 m.deleteAll();
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
  void clearImported(){
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Are you sure you want to Delete every Imported Item?")
           .setCancelable(false)
           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                 ModelAdapter m = new ModelAdapter(getApplicationContext());
                 m.openToWrite();
                 m.deleteImported();
                 m.close();
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
  void initializeExample(){
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Are you sure you want to Replace all data with a fictional festival that starts today?")
           .setCancelable(false)
           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                 initialize3000();
               }
           })
           .setNegativeButton("No", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                 initializeDefaults();
                 dialog.cancel();
               }
           });
    AlertDialog alert = builder.create();
    alert.show();
  }

  public void  remoteReturned()
  {//START
    String publication_type;
    String org_identifier, filename;
    Map<String,String> string_map;
    int unique_id = 1;
    File f;
      //PROCESS
      if (!remoteReceiver.notification.equals("") ){
      toast = Toast.makeText(getApplicationContext(), remoteReceiver.notification, Toast.LENGTH_LONG);
      toast.show();
      return;
    }
    final Intent selectActivityIntent = new Intent(getApplicationContext(), SelectActivity.class);
    selectActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
    startActivity(selectActivityIntent);
    //Log.i("remoteReturned",remoteReceiver.string_map.toString());
  }

 public void  requestRemote(   String uri)
  {//START
    final String furi = uri;
    String what;
      //PROCESS
    what = getString(R.string.request_progress_info);
    what = what.replace("_SERVICE_",remote_app_provider);
    what = what.replace("_VALUE_",remote_app_key);
    remoteReceiver = new requestRemote();
    remoteReceiver.uri = uri;
    remoteReceiver.activity = this;
    remoteReceiver.progress = ProgressDialog.show(this,getString(R.string.request_progress),what,true);
    remoteHandler = new Handler() {
      @Override
      public void handleMessage(Message msg) {
        remoteReturned();
      }
    };
    remoteReceiver.start();
  }

  void importRemote(){
    settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    remote_app_provider = settings.getString("remote_app_provider", "");
    remote_app_key = settings.getString("remote_app_key", "example.xml");
    if ( remote_app_provider.equals("") ) remote_app_provider = "";
    if ( remote_app_key.equals("") ) remote_app_key = "example.xml";
    requestRemote(remote_app_provider + remote_app_key);
  }
  void exportRemote(){
  }
}
