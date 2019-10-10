package com.return0.Kucsa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DatabaseManager {

    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper helper;

    DatabaseManager(Context context){
        this.context=context;
    }

    DatabaseManager open() throws SQLException{
        this.helper=new DatabaseHelper(this.context);
        this.database=this.helper.getWritableDatabase();

        return this;
    }

    public void close(){
        this.helper.close();
    }

    public void insert(String phoneNumber,String regno,String user,String course,String year,String regDate){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DatabaseHelper.REGNO,regno);
        contentValues.put(DatabaseHelper.USER,user);
        contentValues.put(DatabaseHelper.COURSE,course);
        contentValues.put(DatabaseHelper.STUDYEAR,year);
        contentValues.put(DatabaseHelper.REGDATE,regDate);
        contentValues.put(DatabaseHelper.PHONE,phoneNumber);

        this.database.insert(DatabaseHelper.TB_NAME,null,contentValues);
    }

    public Cursor fetch(){
  Cursor cursor=this.database.query(DatabaseHelper.TB_NAME,new String[]{DatabaseHelper._ID,DatabaseHelper.REGNO,DatabaseHelper.PHONE
  ,DatabaseHelper.USER,DatabaseHelper.COURSE,DatabaseHelper.STUDYEAR,DatabaseHelper.REGDATE},null,null,null
  ,null,DatabaseHelper.PHONE) ;

  if (cursor!=null){
      cursor.moveToFirst();
  }
  return cursor;

    }

  public int update(int id,String phoneNumber,String regno,String user,String course,String year,String regDate) {
      ContentValues contentValues=new ContentValues();
      contentValues.put(DatabaseHelper.REGNO,regno);
      contentValues.put(DatabaseHelper.USER,user);
      contentValues.put(DatabaseHelper.COURSE,course);
      contentValues.put(DatabaseHelper.STUDYEAR,year);
      contentValues.put(DatabaseHelper.REGDATE,regDate);
      contentValues.put(DatabaseHelper.PHONE,phoneNumber);
      return this.database.update(DatabaseHelper.TB_NAME,contentValues,DatabaseHelper._ID+"="+id,null);
  }

  ArrayList<User> fetchUsers(){
   String sql="SELECT * FROM "+DatabaseHelper.TB_NAME;
   ArrayList<User> Users=new ArrayList<>();
   Cursor cursor=this.database.rawQuery(sql,null);

   if (cursor.moveToFirst()){
       do { String phoneNumber,registrationNumber,UserName,Course,YearOfStudy,RegDate;
           int id=Integer.parseInt(cursor.getString(0));
           phoneNumber=cursor.getString(1);
           registrationNumber=cursor.getString(2);
           UserName=cursor.getString(3);
           Course=cursor.getString(4);
           YearOfStudy=cursor.getString(5);
           RegDate=cursor.getString(6);

           Users.add(new User(id,phoneNumber,registrationNumber,UserName,Course,YearOfStudy,RegDate));
       }while (cursor.moveToNext());
   }
   cursor.close();
   return Users;

  }

  public void insertMessage(String message,String status,String category){
        ContentValues values=new ContentValues();
        values.put(DatabaseHelper.MSG,message);
        values.put(DatabaseHelper.STATUS,status);
        values.put(DatabaseHelper.CATEGORY,category);
        this.database.insert(DatabaseHelper.MSG_TABLE,null,values);

  }

    public void delete(int id){
        this.database.delete(DatabaseHelper.TB_NAME,DatabaseHelper._ID+"="+id,null);

    }
    ArrayList<String> fetchContacts(){
        String sql="SELECT phone FROM "+DatabaseHelper.TB_NAME;
        ArrayList<String> numbers=new ArrayList<>();
        Cursor cursor=this.database.rawQuery(sql,null);

        if (cursor.moveToFirst()){
            do { String phoneNumber;

                phoneNumber=cursor.getString(cursor.getColumnIndex("phone"));
  numbers.add(phoneNumber);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return numbers;

    }
    ArrayList<Messages> fetchMessages(String category){
        String sql="SELECT * FROM "+DatabaseHelper.MSG_TABLE +" WHERE "+DatabaseHelper.CATEGORY+"='"+category+"';";
        ArrayList<Messages> messages=new ArrayList<>();
        Cursor cursor=this.database.rawQuery(sql,null);

        if (cursor.moveToFirst()){
            do { String phoneNumber,message,mess,categ,status;
                int id=Integer.parseInt(cursor.getString(0));
                message=cursor.getString(1);
                phoneNumber=message.split("\\.")[0];
                mess=message.replace(phoneNumber,"");
                status=cursor.getString(2);


                messages.add(new Messages(id,mess,status,category,phoneNumber));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return messages;

    }




}
