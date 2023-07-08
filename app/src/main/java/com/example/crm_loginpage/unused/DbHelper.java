package com.example.crm_loginpage.unused;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class DbHelper extends SQLiteOpenHelper {
    public static final String PACKUDB= "PACKUSZDB";
    public static final String COL_QTY= "QTY";
    public static final String COL_ID= "ID";
    public static final String COL_DESC= "DESCR";
    public static final int VNUM=3;
    public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "packu.db",null,VNUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createtblstmt =  "CREATE TABLE " + PACKUDB + " ("+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_QTY+ " INT , "+ COL_DESC+ " TEXT)" ;
//        String createtblstmt2 =  "CREATE TABLE " + PACKUDB + " ("+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_QTY+ " INT, "+ COL_DESC+ " TEXT)" ;

        db.execSQL(createtblstmt);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+PACKUDB);
        onCreate(db);
    }

    public boolean addone(PackDB packDB){

        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_QTY, packDB.getQty());
        cv.put(COL_DESC, packDB.getDescr());
        long insert = db.insert(PACKUDB,null,cv);
        return insert != -1;
    }

    public List<PackDB> search_packu(int i){
        List<PackDB> lt= new ArrayList<>();
        String sqrdel= "SELECT *FROM "+ PACKUDB +" WHERE "+ COL_QTY +">= "+i+" ORDER BY "+ COL_QTY ; //+ " >= "+ i ;
        SQLiteDatabase db1= this.getReadableDatabase();
        Cursor cursor = db1.rawQuery(sqrdel,null);
        if(cursor.moveToFirst()){
            do{
                int pcID= cursor.getInt(0);
                int qty= cursor.getInt(1);
                String dec= cursor.getString(2);
                PackDB packDB1 =  new PackDB(pcID,qty,dec);
                lt.add(packDB1);
            }while(cursor.moveToNext() );
        }
        else{
        }
        return lt;
    }
    public List<PackDB> getalldetails(){
        List<PackDB> lt= new ArrayList<>();

        String sqr= "SELECT *FROM "+ PACKUDB+ " ORDER BY QTY";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqr,null);
        if(cursor.moveToFirst()){
            do{
                int pcID= cursor.getInt(0);
                int qty= cursor.getInt(1);
                String dec= cursor.getString(2);
                PackDB packDB1 =  new PackDB(pcID,qty,dec);
                lt.add(packDB1);
            }while(cursor.moveToNext() );
            return lt;
        }
        else{
        }
        return lt;
    }

    public boolean delone(PackDB packDB){

        SQLiteDatabase db1= this.getWritableDatabase();
        String sqrdel= "DELETE FROM "+ PACKUDB +" WHERE "+ COL_ID +" = "+ packDB.getId() ;
        Cursor cursor = db1.rawQuery(sqrdel,null);
        return cursor.moveToFirst();
    }

    public boolean editone(PackDB packDB, int val) {

        SQLiteDatabase db1= this.getWritableDatabase();
        String val1= Integer.toString(val);
        String val2= Integer.toString(1);
        String str1= val2+"*"+ val1;
        String sqr= "UPDATE "+ PACKUDB +" SET "+ COL_QTY +" = ?"  + " , "+COL_DESC+" = ?" + " WHERE "  + COL_QTY + " = "+packDB.getQty() + " AND "+ COL_ID +" = "+packDB.getId();
        Cursor cursor=db1.rawQuery( sqr , new String[] {val1,str1 });
        return cursor.moveToFirst();
    }
}
