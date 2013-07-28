package com.media.lineup;
 import java.lang.Exception;
 import android.util.Log;
public class pException
 extends Exception {
    // members
    // functions

  public pException(   String message)
  {//INIT
    super(message);
    Log.e("pException",message);
  }
}
