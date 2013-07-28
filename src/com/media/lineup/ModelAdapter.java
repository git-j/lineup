package com.media.lineup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import java.util.List;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.HashMap;

public class ModelAdapter {
  public static final String DATABASE_NAME = "lineup";
  public static final int DATABASE_VERSION = 1;
  public static final String KEY_ID = "_id";

  //create table MY_DATABASE (ID integer primary key, Content text not null);
  private static final String SCRIPT_CREATE_EVENT =
    "create table Event ("
  + "  _id integer primary key autoincrement"
  + ", active integer not null"
  + ", begin integer not null"
  + ", end integer not null"
  + ", name text not null"
  + ", display_text text not null"
  + ", description text not null"
  + ", imported integer not null"
  + ", fk_location_map integer  null"
  + ", fk_person_map integer null"
  + ");";
  private static final String SCRIPT_CREATE_LOCATION =
     "create table Location ("
  + "  _id integer primary key autoincrement"
  + ", name text not null"
  + ", last_used integer not null"
  + ", imported integer not null"
  + ", fk_location_map integer null"
  + ");";
  private static final String SCRIPT_CREATE_PERSON =
     "create table Person ("
  + "  _id integer primary key autoincrement"
  + ", name text not null"
  + ", last_used integer not null"
  + ", imported integer not null"
  + ", fk_person_map integer null"
  + ");";
  private SQLiteHelper sqLiteHelper;
  private SQLiteDatabase sqLiteDatabase;

  private Context context;

  public ModelAdapter(Context c){
    context = c;
    setNow();
  }

  public ModelAdapter openToRead() throws android.database.SQLException {
    sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    sqLiteDatabase = sqLiteHelper.getReadableDatabase();
    return this;
  }

  public ModelAdapter openToWrite() throws android.database.SQLException {
    sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    sqLiteDatabase = sqLiteHelper.getWritableDatabase();
    return this;
  }

  public void close(){
    if ( sqLiteHelper != null )
      sqLiteHelper.close();
  }
  public void closeWrite(){
    if ( sqLiteDatabase != null )
      sqLiteDatabase.close();
    close();
  }

  public long insertPerson(String name,int imported){
    ContentValues contentValues = new ContentValues();
    contentValues.put("name", name);
    contentValues.put("imported",imported);
    contentValues.put("last_used",now);
    return sqLiteDatabase.insert("Person", null, contentValues);
  }
  public long insertLocation(String name,int imported){
    ContentValues contentValues = new ContentValues();
    contentValues.put("name", name);
    contentValues.put("imported",imported);
    contentValues.put("last_used",now);
    return sqLiteDatabase.insert("Location", null, contentValues);
  }
  public long insertEvent(Long begin, Long end, String name, String description, int imported, List<String> locations, List<String> persons){
    ContentValues contentValues = new ContentValues();
    contentValues.put("begin", begin);
    contentValues.put("end", end);
    contentValues.put("description",description);
    contentValues.put("imported",imported);
    String display_text = "";
    for(String location : locations ){
      if ( !display_text.equals("") ) display_text+=", ";
      display_text+= location;
      //TODO: update Location-Event-Mapping table?
      updateLastUsed("Location",location);
    }
    if ( !display_text.equals("") ) display_text+=" / ";
    int pindx = 0;
    for(String person : persons ){
      if ( pindx != 0 ) display_text+=", ";
      pindx++;
      display_text+= person;
      //TODO: update Persons-Event-Mapping table?
      updateLastUsed("Person",person);
    }
    if ( !name.equals("") ) display_text+=": " + name;
    if ( name.equals("") ) name = display_text;
    else display_text = name;
    contentValues.put("name", name);
    contentValues.put("display_text", display_text);
    contentValues.put("active", 1);

    return sqLiteDatabase.insert("Event", null, contentValues);
  }
  public long insertScheduleEvent(Long begin, Long end, String name, String description, int imported, List<String> locations, List<String> persons){
    ContentValues contentValues = new ContentValues();
    contentValues.put("begin", begin);
    contentValues.put("end", end);
    contentValues.put("description",description);
    contentValues.put("imported",imported);
    String display_text = "";
    for(String location : locations ){
      if ( !display_text.equals("") ) display_text+=", ";
      display_text+= location;
      //TODO: update Location-Event-Mapping table?
      updateLastUsed("Location",location);
    }
    if ( !name.equals("") ){
      if ( !display_text.equals("") ) display_text+=" / ";
      display_text += name;
    }
    if ( !display_text.equals("") ) display_text+=" / ";
    int pindx = 0;
    for(String person : persons ){
      if ( pindx != 0 ) display_text+=", ";
      pindx++;
      display_text+= person;
      //TODO: update Persons-Event-Mapping table?
      updateLastUsed("Person",person);
    }
    contentValues.put("name", name);
    contentValues.put("display_text", display_text);
    contentValues.put("active", 0);

    return sqLiteDatabase.insert("Event", null, contentValues);
  }
  public long updateLastUsed(String table, String name){
    ContentValues contentValues = new ContentValues();
    contentValues.put("last_used",now);
    return sqLiteDatabase.update(table, contentValues, "name = ?", new String[]{name});
  }
  public long deactivateEvent(Long event_id){
    ContentValues contentValues = new ContentValues();
    contentValues.put("active",0);
    return sqLiteDatabase.update("Event", contentValues, "_id = ?", new String[]{event_id.toString()});
  }
  public long activateEvent(Long event_id){
    ContentValues contentValues = new ContentValues();
    contentValues.put("active",1);
    return sqLiteDatabase.update("Event", contentValues, "_id = ?", new String[]{event_id.toString()});
  }

  public int deleteAll(){
    sqLiteDatabase.delete("Event", null, null);
    sqLiteDatabase.delete("Location", null, null);
    sqLiteDatabase.delete("Person", null, null);
    return 0;
  }
  public int deleteImported(){
    sqLiteDatabase.delete("Event","imported > ?",new String[]{"0"});
    return 0;
  }
  public int deleteEvent(Long event_id){
    sqLiteDatabase.delete("Event","_id = ?",new String[]{event_id.toString()});
    return 0;
  }
  public int deletePerson(String name){
    sqLiteDatabase.delete("Person","name = ?",new String[]{name});
    return 0;
  }
  public int deleteLocation(String name){
    sqLiteDatabase.delete("Location","name = ?",new String[]{name});
    return 0;
  }

  public Cursor queryAll(){
    String[] columns = new String[]{KEY_ID, "begin", "end", "display_text","active","imported"};
    Cursor cursor = sqLiteDatabase.query("Event", columns, null, null, null,null, "begin", null);
    return cursor;
  }
  public Cursor queryAllFutures(){
    String[] columns = new String[]{KEY_ID, "begin", "end", "display_text","active","imported"};
    Cursor cursor = sqLiteDatabase.query("Event", columns,  "begin > ?", new String[]{now.toString()}, null,null, "begin", null);
    return cursor;
  }
  public Cursor queryAllNow(){
    String[] columns = new String[]{KEY_ID, "begin", "end", "display_text","active","imported"};
    Long abegin = now + 900;
    Long aend   = now - 900;
    Cursor cursor = sqLiteDatabase.query("Event", columns,  "? > begin  AND  ? < end", new String[]{abegin.toString(),aend.toString()}, null,null, "begin", null);
    return cursor;
  }
  public Cursor queryAllMyFutures(){
    String[] columns = new String[]{KEY_ID, "begin", "end", "display_text","active","imported"};
    Cursor cursor = sqLiteDatabase.query("Event", columns,  "begin > ? AND imported = 0", new String[]{now.toString()}, null,null, "begin", null);
    return cursor;
  }
  public Cursor queryAllPast(){
    String[] columns = new String[]{KEY_ID, "begin", "end", "display_text","active","imported"};
    Cursor cursor = sqLiteDatabase.query("Event", columns,  "begin < ?", new String[]{now.toString()}, null,null, "begin desc", null);
    return cursor;
  }

  public Cursor queryFutures(int count){
    Long now = System.currentTimeMillis() / 1000L;
    String[] columns = new String[]{KEY_ID, "begin", "end", "display_text","imported"};
    Cursor cursor = sqLiteDatabase.query("Event", columns,  "begin > ? AND active > ?", new String[]{now.toString(),"0"}, null, null, "begin", Integer.toString(count));
    return cursor;
  }
  public Cursor queryPast(int count){
    Long now = System.currentTimeMillis() / 1000L;
    String[] columns = new String[]{KEY_ID, "begin", "end", "display_text","imported"};
    Cursor cursor = sqLiteDatabase.query("Event", columns, "begin < ? AND active > ?", new String[]{now.toString(),"0"} , null, null, "begin desc", Integer.toString(count));
    return cursor;
  }

  public Map<String,String> queryEvent(Long event_id){
    Map<String,String> result = new HashMap<String,String>();
    String[] columns = new String[]{KEY_ID, "begin", "end", "display_text","imported"};
    Cursor cursor = sqLiteDatabase.query("Event", columns,  "event_id = ?", new String[]{event_id.toString()}, null, null, null, null);
    for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){
      for(int i=0;i<columns.length;i++){
        result.put(columns[i],cursor.getString(i));
      }
    }
    cursor.close();
    return result;
  }
  public Cursor queryLocations(){
    String[] columns = new String[]{KEY_ID, "name"};
    Cursor cursor = sqLiteDatabase.query("Location", columns, null, null, null,null, "last_used desc", null);
    return cursor;
  }
  public Cursor queryPersons(){
    String[] columns = new String[]{KEY_ID, "name"};
    Cursor cursor = sqLiteDatabase.query("Person", columns, null, null, null,null, "last_used desc", null);
    return cursor;
  }
  public void setupBaseData(){
    closeWrite();
    openToRead();
    int lcount, pcount;
    Cursor ccount;
    ccount = sqLiteDatabase.rawQuery("select count(*) from Location", null);
    ccount.moveToFirst();
    lcount= ccount.getInt(0);
    ccount.close();
    ccount= sqLiteDatabase.rawQuery("select count(*) from Person", null);
    ccount.moveToFirst();
    pcount= ccount.getInt(0);
    ccount.close();
    close();
    if ( lcount == 0 || pcount == 0 ){
      openToWrite();
      if ( lcount == 0 )
        insertLocation("Home",0);
      if ( pcount == 0 )
        insertPerson("You",0);
      closeWrite();
    }
  }




  public class SQLiteHelper extends SQLiteOpenHelper {
    public SQLiteHelper(Context context, String name, CursorFactory factory, int version) {
      super(context, name, factory, version);
   }
    @Override
    public void onCreate(SQLiteDatabase db) {
      // TODO Auto-generated method stub
       db.execSQL(SCRIPT_CREATE_EVENT);
       db.execSQL(SCRIPT_CREATE_LOCATION);
       db.execSQL(SCRIPT_CREATE_PERSON);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // TODO Auto-generated method stub
    }
  }

  private static DateFormat dateFormat = new SimpleDateFormat("HH:mm");
  public static Long now;
  public static Boolean future_time;
  public static void setfDeltaFutureTime(Boolean future_time_){
    future_time = future_time_;
  }
  public static void setNow(){
    now = System.currentTimeMillis() / 1000L;
  }
  public static String fDelta(Long begin){
        if ( now == null ) setNow();
        Long delta = Math.abs(now - begin);
        //Log.i(LOG,Long.toString(begin));
        //Log.i(LOG,Long.toString(now));
        //Log.i(LOG,"delta:" + delta.toString());
        Long days = delta / (24L * 3600L);
        //Log.i(LOG,"days:" + days.toString());
        Long hours = delta / ( 3600L ) - (days * 24L);
        //Log.i(LOG,"hours:" + hours.toString());
        Long minutes = delta / ( 60L );
        //Log.i(LOG,"minutes:" + minutes.toString());
        minutes-= ( delta / 3600L ) * 60L;
        String sdelta = "";
        if ( days > 0 )
          sdelta+=days.toString() + "d ";
        sdelta+= hours.toString() + "'";
        if ( minutes < 10 ) sdelta+="0";
        sdelta+= minutes.toString();
        Date begin_date = new Date(begin*1000);
        if ( future_time!= null && future_time )
          return  dateFormat.format(begin_date) + " / " + sdelta + " ";
        return sdelta;
  }

  public static String fDeltaSimple(Long begin){
        if ( now == null ) setNow();
        Long delta = Math.abs(now - begin);
        //Log.i(LOG,Long.toString(begin));
        //Log.i(LOG,Long.toString(now));
        //Log.i(LOG,"delta:" + delta.toString());
        Long days = delta / (24L * 3600L);
        //Log.i(LOG,"days:" + days.toString());
        Long hours = delta / ( 3600L ) - (days * 24L);
        //Log.i(LOG,"hours:" + hours.toString());
        Long minutes = delta / ( 60L );
        //Log.i(LOG,"minutes:" + minutes.toString());
        minutes-= ( delta / 3600L ) * 60L;
        String sdelta = "";
        if ( days > 0 )
          sdelta+=days.toString() + "d ";
        sdelta+= hours.toString() + "'";
        if ( minutes < 10 ) sdelta+="0";
        sdelta+= minutes.toString();
        return  sdelta;
  }

}
