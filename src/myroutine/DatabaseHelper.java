package com.myroutine.myroutine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by basmatebe on 3/30/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "MyRoutine";
    //table 1
    public static final String TABLE_NAME1 = "super_table";
    public static final String COL1_T1 = "ID";
    public static final String COL2_T1 = "PASSWORD";
    public static final String COL3_T1 = "EMAIL";
    //table 2
    public static final String TABLE_NAME2 = "task_table";
    public static final String COL1_T2 = "ID";
    public static final String COL2_T2 = "NAME";
    public static final String COL3_T2 = "DAY";
    public static final String COL4_T2 = "TIME";
    //table 3
    public static final String TABLE_NAME3 = "step_table";
    public static final String COL1_T3 = "ID";
    public static final String COL2_T3 = "NAME";
    public static final String COL3_T3 = "PIC";
    public static final String COL4_T3 = "AUDIO";
    public static final String COL5_T3 = "TIMER";
    public static final String COL6_T3 = "TASK_ID";


    private static final String CREATE_TABLE_NAME1 = "CREATE TABLE "
            + TABLE_NAME1 + "(" + COL1_T1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL2_T1 + " TEXT,"+ COL3_T1+" TEXT"+")";

    private static final String CREATE_TABLE_NAME2 = "CREATE TABLE "
            + TABLE_NAME2 + "(" + COL1_T2 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL2_T2 + " TEXT," + COL3_T2 + " TEXT,"
            + COL4_T2 + " TEXT" + ")";

    private static final String CREATE_TABLE_NAME3 = "CREATE TABLE "
            + TABLE_NAME3 + "(" + COL1_T3 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL2_T3 + " TEXT," + COL3_T3 + " TEXT,"
            + COL4_T3 + " TEXT," + COL5_T3+ " TEXT,"+ COL6_T3+ " INTEGER" + ")";





    public DatabaseHelper(Context context) {
        //last param is for how many tables to initiate at first create
        //if change number it'll erase everything and create from scratch
        super(context, DB_NAME, null, 3);
        //to check
        //SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_NAME1);
        db.execSQL(CREATE_TABLE_NAME2);
        db.execSQL(CREATE_TABLE_NAME3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME3);
        //Create tables again
        onCreate(db);

    }

    /*public boolean insertDate(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2_T1, name);
        //if res eq to -1 then not inserted
        Long res = db.insert(TABLE_NAME1, null,cv);
        if (res == -1){
            return false;
        }else{
            return true;
        }

    }*/
    //random read/write access to your result
    public Cursor getAllData(String table){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+ table, null);
        return res;
    }

    public List<Task> getAllTasks(){

        List<Task> tasks = new LinkedList<Task>();
        String query = "SELECT  * FROM " + TABLE_NAME2;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Task task = null;
        if (cursor.moveToFirst()) {
            do {
                task = new Task();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setName(cursor.getString(1));
                task.setDay(cursor.getString(2));
                task.setTime(cursor.getString(3));

                // Add task to tasks
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        //debugging purpose
        Log.d("getAllTasks()", tasks.toString());

        // return tasks
        return tasks;
    }


    public List<Task> getAllTasksOrderByTime(){

        List<Task> tasks = new LinkedList<Task>();
        String query = "SELECT  * FROM " + TABLE_NAME2 + " ORDER BY "+COL4_T2 + " ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Task task = null;
        if (cursor.moveToFirst()) {
            do {
                task = new Task();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setName(cursor.getString(1));
                task.setDay(cursor.getString(2));
                task.setTime(cursor.getString(3));

                // Add task to tasks
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        //debugging purpose
        Log.d("getAllTasks()", tasks.toString());

        // return tasks
        return tasks;
    }

    public List<Step> getAllSteps(){

        List<Step> steps = new LinkedList<Step>();
        String query = "SELECT  * FROM " + TABLE_NAME3;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Step step = null;
        if (cursor.moveToFirst()) {
            do {
                 step = new Step();
                step.setId(Integer.parseInt(cursor.getString(0)));
                step.setName(cursor.getString(1));
                step.setPic(cursor.getString(2));
                step.setAudio(cursor.getString(3));
                step.setTimer(cursor.getString(4));
                step.setTaskID(cursor.getInt(5));

                // Add task to tasks
                steps.add(step);
            } while (cursor.moveToNext());
        }
        //debugging purpose
        Log.d("getAllSteps()", steps.toString());

        // return tasks
        return steps;


    }
    //get all steps from one task
    public List<Step> getAllSteps(int id){

        List<Step> steps = new LinkedList<Step>();
        String query = "SELECT  * FROM " + TABLE_NAME3  + " WHERE "
                + COL6_T3 + " = " +"'"+ id+"'";;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Step step = null;
        if (cursor.moveToFirst()) {
            do {
                step = new Step();
                step.setId(Integer.parseInt(cursor.getString(0)));
                step.setName(cursor.getString(1));
                step.setPic(cursor.getString(2));
                step.setAudio(cursor.getString(3));
                step.setTimer(cursor.getString(4));
                step.setTaskID(cursor.getInt(5));

                // Add task to tasks
                steps.add(step);
            } while (cursor.moveToNext());
        }
        //debugging purpose
        Log.d("getAllSteps()", steps.toString());

        // return tasks
        return steps;

    }

    public Step getStep(int id){

        //List<Step> steps = new LinkedList<Step>();
        String query = "SELECT  * FROM " + TABLE_NAME3  + " WHERE "
                + COL1_T3 + " = " +"'"+ id+"'";;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Step step = null;
        if (cursor.moveToFirst()) {
            do {
                step = new Step();
                step.setId(Integer.parseInt(cursor.getString(0)));
                step.setName(cursor.getString(1));
                step.setPic(cursor.getString(2));
                step.setAudio(cursor.getString(3));
                step.setTimer(cursor.getString(4));
                step.setTaskID(cursor.getInt(5));

                // Add task to tasks
                //steps.add(step);
            } while (cursor.moveToNext());
        }
        //debugging purpose
        Log.d("getAllSteps()", step.toString());

        // return tasks
        return step;

    }

    public Task getTask(String name){

        String query = "SELECT  * FROM " + TABLE_NAME2 + " WHERE "
                + COL2_T2 + " = " +"'"+ name+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Task task = null;
        if (cursor.moveToFirst()) {
            do {
                task = new Task();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setName(cursor.getString(1));
                task.setDay(cursor.getString(2));
                task.setTime(cursor.getString(3));

                // Add task to tasks

            } while (cursor.moveToNext());
        }
        //debugging purpose
        Log.d("getAllTasks()", task.toString());

        // return tasks
        return task;

    }

    public Task getTask(int id){

        String query = "SELECT  * FROM " + TABLE_NAME2 + " WHERE "
                + COL1_T2 + " = " +"'"+ id+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Task task = null;
        if (cursor.moveToFirst()) {
            do {
                task = new Task();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setName(cursor.getString(1));
                task.setDay(cursor.getString(2));
                task.setTime(cursor.getString(3));

                // Add task to tasks

            } while (cursor.moveToNext());
        }
        //debugging purpose
        Log.d("getAllTasks()", task.toString());

        // return tasks
        return task;

    }
    public SuperUser getUser(){

        String query = "SELECT  * FROM " + TABLE_NAME1 + " WHERE "
                + COL1_T1 + " = " +"'"+ String.valueOf(1)+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        SuperUser user = null;
        if (cursor.moveToFirst()) {
            do {
                user = new SuperUser();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setPassword(cursor.getString(1));
                user.setEmail(cursor.getString(2));



            } while (cursor.moveToNext());
        }
        //debugging purpose
        Log.d("getAllTasks()", user.toString());

        // return tasks
        return user;

    }

    public Cursor getSuperUser(String password){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME1 + " WHERE "
                + COL2_T1 + " = " +"'"+ password+"'";

        Cursor res = db.rawQuery(selectQuery, null);
        return res;
    }
    public boolean addToSuperTable(SuperUser user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2_T1, user.getPassword());
        cv.put(COL3_T1, user.getEmail());
        //if res eq to -1 then not inserted
        Long res = db.insert(TABLE_NAME1, null,cv);
        if (res == -1){
            return false;
        }else{
            return true;
        }

    }
    public int updateSuperUser(SuperUser user) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        //name, pic, audio, timer
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(COL2_T1, user.getPassword()); // get name
        values.put(COL3_T1, user.getEmail()); // get pic



        // 3. updating row
        //theres only one user
        int i = db.update(TABLE_NAME1, //table
                values, // column/value
                COL1_T1+" = ?", // selections
                new String[] { String.valueOf(1) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    public boolean addTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2_T2, task.getName());
        cv.put(COL3_T2, task.getDay());
        cv.put(COL4_T2, task.getTime());
        //if res eq to -1 then not inserted
        Long res = db.insert(TABLE_NAME2, null,cv);
        if (res == -1){
            return false;
        }else{
            return true;
        }
    }
    public boolean addStep(Step step){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2_T3, step.getName());
        cv.put(COL3_T3, step.getPic());
        cv.put(COL4_T3, step.getAudio());
        cv.put(COL5_T3, step.getTimer());
        cv.put(COL6_T3, step.getTaskID());
        //if res eq to -1 then not inserted
        Long res = db.insert(TABLE_NAME3, null,cv);
        if (res == -1){
            return false;
        }else{
            return true;
        }
    }

    public void deleteStep(int id) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_NAME3, //table name
                COL1_T3+" = ?",  // selections
                new String[] { String.valueOf(id) }); //selections args

        // 3. close
        db.close();

        //log
        //Log.d("deleteStep", step.toString());

    }

    public void deleteTask(int id) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_NAME2, //table name
                COL1_T2+" = ?",  // selections
                new String[] { String.valueOf(id) }); //selections args

        // 3. close
        db.close();

        //log
        //Log.d("deleteStep", step.toString());

    }
    //returns number of rows affected
    public int updateStep(Step step) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        //name, pic, audio, timer
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(COL2_T3, step.getName()); // get name
        if(step.getPic() != null){
            values.put(COL3_T3, step.getPic()); // get pic
        }
        if(step.getAudio() != null){
            values.put(COL4_T3, step.getAudio()); // get audio
        }
        values.put(COL5_T3, step.getTimer()); // get timer

        // 3. updating row
        int i = db.update(TABLE_NAME3, //table
                values, // column/value
                COL1_T3+" = ?", // selections
                new String[] { String.valueOf(step.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    public int updateTask(Task task) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        //name, pic, audio, timer
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(COL2_T2, task.getName()); // get name
        values.put(COL3_T2, task.getDay()); // get pic
        values.put(COL4_T2, task.getTime()); // get audio


        // 3. updating row
        int i = db.update(TABLE_NAME2, //table
                values, // column/value
                COL1_T2+" = ?", // selections
                new String[] { String.valueOf(task.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }


    public void deleteAllStep(int id) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_NAME3, //table name
                COL6_T3+" = ?",  // selections
                new String[] { String.valueOf(id) }); //selections args

        // 3. close
        db.close();

        //log
        //Log.d("deleteBook", book.toString());

    }

}
