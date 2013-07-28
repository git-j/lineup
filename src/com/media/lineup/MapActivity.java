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
 import android.view.Menu;
 import android.view.MenuInflater;
 import android.view.MenuItem;
 import android.view.View;
 import android.view.ViewGroup.*;
 import android.widget.*;
 import android.webkit.WebView;
 import java.io.*;
 import java.net.*;
 import java.util.Map;
public class MapActivity
 extends Activity {
    // members
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
    WebView webview = (WebView) findViewById(R.id.webview);
    webview.getSettings().setJavaScriptEnabled(true);
    String map = "file:///android_res/drawable/map.png";
    webview.getSettings().setBuiltInZoomControls(true);
    webview.loadUrl(map);
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
    
    //PROCESS
    super.onCreate(savedInstanceState);
  setContentView(R.layout.map_activity);
  initializeControls();
  
  
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
    MenuInflater inflater = getMenuInflater();
  inflater.inflate(R.menu.map, menu);
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
    switch (menu_item.getItemId()) {
    case R.id.closemap:
        finish();
        return true;
    default:
        return super.onOptionsItemSelected(menu_item);
  }
  
  }

  
  /**************************************************************************//**
  \brief  onPause - 
  *******************************************************************************
\brief  default - 



*******************************************************************************
******************************************************************************/

  @Override
  public void  onPause()
  {//START
    
    //PROCESS
    super.onPause();
  // CameraManager.get().closeDriver();
  
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
    
    //PROCESS
    super.onResume();
  
  }

  
  /**************************************************************************//**
  \brief  preferences - 
  *******************************************************************************
\brief  default - 



*******************************************************************************
******************************************************************************/

  public void  preferences()
  {//START
    
    //PROCESS
    Intent preferencesActivityIntent = new Intent(getApplicationContext(), PreferencesActivity.class);
  startActivityForResult(preferencesActivityIntent, 0);
  
  
  }

  
}