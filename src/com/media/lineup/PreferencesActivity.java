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
 import android.os.Bundle;
 import android.os.Vibrator;
 import android.preference.*;
 import android.util.*;
 import android.view.LayoutInflater;
 import android.view.SurfaceHolder;
 import android.view.SurfaceView;
 import android.view.View;
 import android.view.ViewGroup.*;
 import android.widget.*;
 import java.io.*;
 import java.net.*;
 import java.util.Map;
public class PreferencesActivity
 extends PreferenceActivity {
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
    super.onCreate(savedInstanceState);
  
    //PROCESS
    addPreferencesFromResource(R.xml.preferences);
  
  
  }

  
}