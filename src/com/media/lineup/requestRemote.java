package com.media.lineup;
 import android.app.ProgressDialog;
 import android.util.*;
 import android.widget.Toast;
 import com.media.lineup.MainActivity;
 import java.io.*;
 import java.io.IOException;
 import java.lang.Exception;
 import java.lang.Thread;
 import java.net.*;
 import org.apache.http.client.*;
 import org.apache.http.client.HttpClient;
 import org.apache.http.client.ResponseHandler;
 import org.apache.http.client.methods.*;
 import org.apache.http.impl.client.*;
 import org.xml.sax.*;
 import org.xml.sax.Parser;
 import javax.xml.parsers.ParserConfigurationException;
 import java.text.ParseException;
 import org.w3c.dom.*;
 import javax.xml.parsers.DocumentBuilderFactory;
 import javax.xml.parsers.DocumentBuilder;


public class requestRemote
 extends Thread {
    // members
  public String uri = ""; //typeref/documentation
  public String request_result = ""; //typeref/documentation
  public MainActivity activity = null; //typeref/documentation
  public String notification = ""; //typeref/documentation
  public ProgressDialog progress = null; //typeref/documentation
  public String msg_service_not_available = "Service not Available"; //typeref/documentation
  public String msg_invalid_xml_in_publication = "Invalid XML in Publication"; //typeref/documentation
  public String msg_empty_result_received = "No Result Received\nThe Information you requested was not available. You have to enter the information manually."; //typeref/documentation
  private ModelAdapter model;
  private static final String tag = "com.media.lineup.requestRemote";
    // functions

  /**************************************************************************//**
  \brief  request -
  *******************************************************************************
\brief  default -



\param  uri -
*******************************************************************************
******************************************************************************/

  public void  request(   String uri)
  {//START
    String request_url;
  HttpClient http_client;
  HttpGet request;
  Toast toast;
  ResponseHandler<String> handler;
  final String empty_response = "<?xml version=\"1.0\"?><schedule/>";
    //PROCESS
    request_result = "";
  Log.i(tag,uri);
  http_client = new DefaultHttpClient();
  try{
    request = new HttpGet(uri);
    handler = new BasicResponseHandler();
    request_result = http_client.execute(request, handler);
  } catch (ClientProtocolException e) {
    e.printStackTrace();
    notification = msg_service_not_available;
    //getString(R.string.exception_service_not_available);
    request_result = empty_response;
  } catch (UnsupportedEncodingException e){
    e.printStackTrace();
    notification = e.toString();
    request_result = empty_response;
  } catch (IOException e) {
    e.printStackTrace();
    notification = e.toString();
    request_result = empty_response;
  } catch (IllegalStateException e){
    e.printStackTrace();
    notification = e.toString();
    request_result = empty_response;
  }
  http_client.getConnectionManager().shutdown();
  //full dump:
  //Log.i(tag, request_result);


  }


  /**************************************************************************//**
  \brief  run -
  *******************************************************************************
\brief  default -



*******************************************************************************
******************************************************************************/

  public void  run()
  {//START

    //PROCESS
    request(uri);
    xmlToValueMap(request_result);
    progress.dismiss();
    activity.remoteHandler.sendEmptyMessage(0);
  }


  /**************************************************************************//**
  \brief  xmlReadPublication -
  *******************************************************************************
\brief  default -



\param  node -
\param  string_map -
*******************************************************************************
******************************************************************************/

  public void xmlToValueMap(   String xml_string)
  {//START
    String tag = "xmlparse";
    DocumentBuilderFactory factory;
    DocumentBuilder builder;
    InputStream xis;
    Document document;
    Element root;
    NodeList days;
    if ( activity == null ) return;
    model = new ModelAdapter(activity.getApplicationContext());
    model.openToWrite();
    model.deleteImported();
      //PROCESS
    try {
      factory = DocumentBuilderFactory.newInstance();
      builder = factory.newDocumentBuilder();
      xis = new StringBufferInputStream (xml_string);
      document = builder.parse(xis);

      importXML_Pentabarf ip = new importXML_Pentabarf(model);
      importXML_LinuxTag il = new importXML_LinuxTag(model);
      if ( ip.importDocument(document) ){
        Log.i(tag,"imported pentabarf");
      }else if ( il.importDocument(document) ){
        Log.i(tag,"imported linuxtag");
      }
    }catch(DOMException e){
      notification = e.toString();
      Log.w(tag,e.toString());
    }catch(SAXException e){
      notification = e.toString();
      Log.w(tag,e.toString());
    }catch(IOException e){
      notification = e.toString();
      Log.w(tag,e.toString());
    }catch(ParserConfigurationException e){
      notification = e.toString();
      Log.w(tag,e.toString());
    }catch(ParseException e){
      notification = e.toString();
      Log.w(tag,e.toString());
    }catch(pException e){
      notification = e.toString();
      Log.w(tag,e.toString());
    }
    model.closeWrite();
  }


}
