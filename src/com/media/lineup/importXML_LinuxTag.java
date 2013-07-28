package com.media.lineup;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
 import java.text.DateFormat;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.ArrayList;
 import android.util.*;

public class importXML_LinuxTag extends importXML{
  private Document doc;
  private Map<String,String> rooms;
  private static final String tag = "com.media.lineup.importXML_LinuxTag";
  private String title, description;

  public importXML_LinuxTag(ModelAdapter model){
    super(model);
    rooms = new HashMap<String,String>();
  }

  public Boolean importDocument(Document document)
    throws pException,ParseException{
    if ( !document.getDocumentElement().getTagName().equals("program") )
      return false;
    doc  = document;
    NodeList dem = document.getDocumentElement().getChildNodes();
    for ( int i = 0
        ; i < dem.getLength()
        ; i++) {
      if (dem.item(i).getNodeType() != Node.ELEMENT_NODE)
        continue;
      if ( ((Element)dem.item(i)).getTagName().equals("event") )
        readEvent(dem.item(i));
      else if ( ((Element)dem.item(i)).getTagName().equals("locationset") )
        readLocations(((Element)dem.item(i)).getChildNodes());

    }

    return true;
  }
  private void readLocations(NodeList events){
    for (int i = 0
        ; i < events.getLength()
        ; i++) {
      if (events.item(i).getNodeType() != Node.ELEMENT_NODE)
        continue;
      Element em = ((Element)events.item(i));
      String id = em.getAttribute("id");
      String name = "";
      NodeList ec = em.getChildNodes();
      for ( int j = 0
          ; j < ec.getLength()
          ; j++ ){
        if (ec.item(j).getNodeType() != Node.ELEMENT_NODE)
          continue;
        Element ecem = (Element)ec.item(j);
        if ( ecem.getTagName().equals("name"))
          name = text(ecem);
      }
      rooms.put(id,name);
    }
  }
  private String roomName(String room_id){
    return rooms.get(room_id);
  }
  private void  readPersons(   Node node, ArrayList<String> persons)
    throws pException,ParseException{
    NodeList values;
    String personname = "";
    //PROCESS
    values = ((Element)node).getChildNodes();
    for (int j = 0
        ; j < values.getLength()
        ; j++) {
      if (values.item(j).getNodeType() != Node.ELEMENT_NODE) continue;
      Element em = (Element)values.item(j);
      if ( em.getTagName().equals("firstname") )
        personname = text(em);
      else if ( em.getTagName().equals("lastname") ){
        personname+=" " + text(em);
        persons.add(personname);
        personname = "";
      }
    }

    //Log.i(tag,persons.toString());
  }
  private void  readTalk(   Node node )
    throws pException,ParseException{
    NodeList values;
    values = ((Element)node).getChildNodes();
    title = "";
    description = "";
    for (int j = 0
        ; j < values.getLength()
        ; j++) {
      if (values.item(j).getNodeType() != Node.ELEMENT_NODE) continue;
      Element em = (Element)values.item(j);
      if ( em.getTagName().equals("title") )
        title = text(em);
      else if ( em.getTagName().equals("abstract") )
        description = text(em);
    }

    //Log.i(tag,title + ":" + description);
  }
  private void readEvent(Node event)
    throws pException,ParseException{
    /*  <event id="e101"
         type="t14"
         starttime="10:00"
         endtime="10:30"
         duration="00:30:00"
         date="23.05.2012"
         room="S1"
         category="p11">
         <person/>
         <talk/>
    */
    ArrayList<String> locations = new ArrayList<String>();
    ArrayList<String> persons = new ArrayList<String>();
    Date start_date = null;
    Date end_date = null;

    String starttime = ((Element)event).getAttribute("starttime");
    String endtime = ((Element)event).getAttribute("endtime");
    String date = ((Element)event).getAttribute("date");
    String room_id = ((Element)event).getAttribute("room");
    String room = roomName(room_id);
    NodeList values;
    values = ((Element)event).getChildNodes();
    for (int j = 0
        ; j < values.getLength()
        ; j++) {
      if (values.item(j).getNodeType() != Node.ELEMENT_NODE) continue;
      Element em = (Element)values.item(j);
      if ( em.getTagName().equals("person") )
        readPersons(values.item(j),persons);
      else if ( em.getTagName().equals("talk") )
        readTalk(values.item(j));
    }
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
      start_date = formatter.parse(date + " " + starttime);
      end_date = formatter.parse(date + " " + endtime);
      //Log.i(tag,"start:" + start_date.toString());
      //Log.i(tag,"end:" + end_date.toString());
    }catch(ParseException ie){}
    Log.i(tag,room + " @ " + date + " " + starttime);
    if (  start_date == null
        ||end_date == null
        ||title == null
        ||title.equals("N.N.")
        )
        return;
    long begin_time = start_date.getTime()/1000L;
    long end_time = end_date.getTime()/1000L;
    locations.add(room);
    model.insertScheduleEvent(begin_time,end_time
                             ,title
                             ,description,1,locations,persons);

  }
}
