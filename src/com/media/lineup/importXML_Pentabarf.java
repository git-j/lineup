package com.media.lineup;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

 import org.xml.sax.*;

 import java.text.DateFormat;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.ArrayList;
 import com.media.lineup.MainActivity;
  import android.util.*;


public class importXML_Pentabarf extends importXML{
  public String msg_invalid_xml_in_publication = "Invalid XML in Publication"; //typeref/documentation
  public String msg_empty_result_received = "No Result Received\nThe Information you requested was not available. You have to enter the information manually."; //typeref/documentation
  private static final String tag = "com.media.lineup.importXML_Pentabarf";

  public importXML_Pentabarf(ModelAdapter model){
    super(model);
  }

  public Boolean importDocument(Document document)
    throws pException,ParseException{
    if ( !document.getDocumentElement().getTagName().equals("schedule") )
      return false;
    readSchedule(document.getDocumentElement().getChildNodes());
    return true;
  }

  private void  readPersons(   Node node, ArrayList<String> persons)
   throws pException,ParseException{
    NodeList values;
    String type, room_name;
    //PROCESS
    if (node.getNodeType() != Node.ELEMENT_NODE) {
       //throw new pException(msg_invalid_xml_in_publication);
       return;
    }
    values = ((Element)node).getChildNodes();
    room_name = ((Element)node).getAttribute("name");
    for (int j = 0
        ; j < values.getLength()
        ; j++) {
      if (values.item(j).getNodeType() != Node.ELEMENT_NODE) continue;
      persons.add(text(values.item(j)));
    }

    type = ((Element)node).getTagName();
    Log.i("xmlReadPersons",persons.toString());
}
  private void  readEvent(   Node node, String date, String room_name)
  throws pException,ParseException{//START
    NodeList values;
    Node em;
    String type, nodename, description="",title="";
    ArrayList<String> locations = new ArrayList<String>();
    ArrayList<String> persons = new ArrayList<String>();
    Date pdate = null;
    long duration = 0;
    //PROCESS
    if (node.getNodeType() != Node.ELEMENT_NODE) {
       //throw new pException(msg_invalid_xml_in_publication);
       return;
    }
    values = ((Element)node).getChildNodes();
    for (int j = 0
        ; j < values.getLength()
        ; j++) {
        if (values.item(j).getNodeType() != Node.ELEMENT_NODE) continue;
        em = values.item(j);
        nodename = ((Element)em).getTagName();
        if ( nodename.equals("start") ) {
          Log.i("xmlReadEvent","start:" + date + " " +  text(em));
          try {
          SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
          pdate = formatter.parse(date + " " + text(em));
          Log.i("xmlReadEvent","start:" + pdate.toString());
          }catch(ParseException ie){
          }

        }else if ( nodename.equals("duration") ) {
          //Log.i("xmlReadEvent","duration:" + text(em));
          String sduration = text(em);
          String shours = sduration.substring(0,sduration.indexOf(":"));
          String sminutes = sduration.substring(sduration.indexOf(":")+1);
          //Log.i("duration",shours + "|" + sminutes);
          if ( shours.equals("00") ) shours = "24";
          duration = Integer.parseInt(shours) * 3600 + Integer.parseInt(sminutes);
        }else if ( nodename.equals("title") ) {
          //Log.i("xmlReadEvent","title:" + text(em));
          title = text(em);
        }else if ( nodename.equals("subtitle") ) {
          //Log.i("xmlReadEvent","subtitle:" + text(em));
        }else if ( nodename.equals("persons") ) {
          //Log.i("xmlReadEvent","persons:");
          readPersons(em,persons);
        }else if ( nodename.equals("abstract") ) {
          description = text(em);
        }
    }
    if ( pdate == null ) return; // ignore event
    locations.add(room_name);
    long begin_time = pdate.getTime()/1000L;
    model.insertScheduleEvent(begin_time,begin_time+duration
                             ,title
                             ,description,1,locations,persons);

    type = ((Element)node).getTagName();
    //Log.i("xmlReadEvent",type);


  }
  public void  readRoom(   Node node, String date)
  throws pException,ParseException{//START
    NodeList values;
    String type, room_name;
    //PROCESS
    if (node.getNodeType() != Node.ELEMENT_NODE) {
       //throw new pException(msg_invalid_xml_in_publication);
       return;
    }
    values = ((Element)node).getChildNodes();
    room_name = ((Element)node).getAttribute("name");
    for (int j = 0
        ; j < values.getLength()
        ; j++) {
      readEvent(values.item(j),date,room_name);
    }

    type = ((Element)node).getTagName();
    Log.i("xmlReadRoom",type);


  }

  private void  readDays(   Node node )
  throws pException,ParseException{//START
    NodeList values;
    String type, ident_string;
      //PROCESS
    if (node.getNodeType() != Node.ELEMENT_NODE) {
      return;
      //throw new pException(msg_invalid_xml_in_publication);
    }
    type = ((Element)node).getTagName();
    Log.i("xmlReadDays",type);
    values = ((Element)node).getChildNodes();
     String date = ((Element)node).getAttribute("date");
     for (int j = 0
        ; j < values.getLength()
        ; j++) {
       readRoom(values.item(j),date);
    }
  }

  private void  readSchedule(   NodeList days )
  throws pException,ParseException{//START
    if ( days.getLength() == 0 ){
      //notification = msg_empty_result_received;
      //getString(R.string.exception_empty_result_received);
      return;
    }
    //<schedule><day>
    for ( int i = 0
        ; i < days.getLength()
        ; i++) {
      Log.i(tag,"Day:" + Integer.toString(i));
      readDays(days.item(i));
    }
  }
}
