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
 import android.view.ContextMenu;
 import android.view.ContextMenu.*;
 import android.view.LayoutInflater;
 import android.view.Menu;
 import android.view.MenuInflater;
 import android.view.MenuItem;
 import android.view.View;
 import android.view.ViewGroup;
 import android.view.ViewGroup.*;
 import android.widget.*;
 import java.util.HashMap;
 import java.util.Map;

public class ActivityForm
 extends Activity {
    // members
  public Boolean modified = false; //typeref/documentation
  public String source = ""; //typeref/documentation
  public SharedPreferences settings = null; //typeref/documentation
  public ProgressDialog progress; //typeref/documentation
  public Toast toast; //typeref/documentation
  public String key = ""; //typeref/documentation
  public Boolean should_finish = false; //typeref/documentation
    // functions
  
  /**************************************************************************//**
  \brief  checkModified - 
  *******************************************************************************
\brief  default - 



*******************************************************************************
\return  - 
  ******************************************************************************/

  public Boolean checkModified()
  {//START
    
    //PROCESS
    return true;
  }

  
  /**************************************************************************//**
  \brief  fillField - 
  *******************************************************************************
\brief  default - 



\param  field_name - 
\param  field_value - 
*******************************************************************************
******************************************************************************/

  public void  fillField(   String field_name, String field_value)
  {//START
    String xml_field_id;
  int resid;
  EditText edit;
  
    //PROCESS
    //Log.i("fillField",fieldName);
  //Log.i("fillField",fieldValue);
  
  xml_field_id = "edit_" + field_name;
  resid = getResources().getIdentifier(xml_field_id
                                     ,"id"
                                     , this.getPackageName());
  edit = (EditText) findViewById(resid);
  if ( edit == null ) {
    Log.e("fillField","field " + xml_field_id + " not found");
    return;
  }
  //Log.i("fillField",xml_field_id + " " + field_value);
  edit.setText(field_value.trim());
  
  
  }

  
  /**************************************************************************//**
  \brief  initializeControls - 
  *******************************************************************************
\brief  default - 



*******************************************************************************
******************************************************************************/

  public void  initializeControls()
  {//START
    //EditText identifier;
  
    //PROCESS
    //identifier = (EditText) findViewById(R.id.identifier);
    //identifier.setText("");
  
  }

  
  /**************************************************************************//**
  \brief  loadData - 
  *******************************************************************************
\brief  default - 



*******************************************************************************
******************************************************************************/

  public void  loadData()
  {//START
    
    //PROCESS
    //values.load();
  
  
  }

  
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
  \brief  onContextItemSelected - 
  *******************************************************************************
\brief  default - 



\param  menu_item - 
*******************************************************************************
\return cond - 
  ******************************************************************************/

  @Override
  public boolean onContextItemSelected(   MenuItem menu_item)
  {//START
    
    //PROCESS
    AdapterView.AdapterContextMenuInfo info = 
    (AdapterView.AdapterContextMenuInfo)menu_item.getMenuInfo();
  return super.onOptionsItemSelected(menu_item);
  
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
  
    //PROCESS
    super.onCreate(savedInstanceState);
  try {
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    initializeControls();
    loadSettings();
    loadData();
    //actions.routeIntend();
  } catch ( Exception e ){
    toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
    toast.show();
    e.printStackTrace();
  }
  
  }

  
  /**************************************************************************//**
  \brief  onCreateContextMenu - 
  *******************************************************************************
\brief  default - 



\param  id - 
\param  context_menu - 
\param  view - 
\param  context_menu_info - 
*******************************************************************************
******************************************************************************/

  public void  onCreateContextMenu(   int id, ContextMenu context_menu, View view, ContextMenuInfo context_menu_info)
  {//START
    
    //PROCESS
    super.onCreateContextMenu(context_menu, view, context_menu_info);
  MenuInflater inflater = getMenuInflater();
  inflater.inflate(id, context_menu);
  
  
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

  public boolean onCreateOptionsMenu(   int id, Menu menu)
  {//START
    
    //PROCESS
    MenuInflater inflater = getMenuInflater();
  inflater.inflate(id, menu);
  return true;
  
  
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
    try {
//    action.route(menu_item.getItemId());
    return true;
  } catch ( Exception e ){
    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
    e.printStackTrace();
  }
  return super.onOptionsItemSelected(menu_item);
  
  
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
    try {
    //values.store();
  } catch ( Exception e ){
    toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
    toast.show();
    e.printStackTrace();
  }
  
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
    try {
    loadSettings();
    loadData();
  } catch ( Exception e ){
    toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
    toast.show();
    e.printStackTrace();
  }
  
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
    try {
    //values.store();
  } catch ( Exception e ){
    toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
    toast.show();
    e.printStackTrace();
  }
  
  
  }

  
}