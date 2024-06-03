package com.zjs.todolist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.zjs.todolist.Model.ToDoModel;

import java.util.LinkedList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "todo.db";
    public static final String TODO_TABLE = "ACHIEVEMENT";
    public static final String ID = "ID";
    public static final String STATUS = "STATUS";
    public static final String TASK = "TASK";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " +
                        TODO_TABLE + "(" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        STATUS + " INTEGER, " +
                        TASK + " TEXT"
                        + ")"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertTask(ToDoModel task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TASK,task.getTask());
        cv.put(STATUS, 0);

        long insert = db.insert(TODO_TABLE, null, cv);
        return insert != -1;
    }

    //Update Task (TEXT)
    public void updateTask(ToDoModel task) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "UPDATE " +
                TODO_TABLE + " SET " +
                TASK + " = ? WHERE " +
                ID + " = ?";
        db.execSQL(queryString, new Object[]{task.getTask(), task.getId()});
        db.close();
    }

    public void updateStatus(ToDoModel task) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "UPDATE " +
                TODO_TABLE + " SET " +
                STATUS + " = ? WHERE " +
                ID + " = ?";
        db.execSQL(queryString, new Object[]{task.getStatus(), task.getId()});
        db.close();
    }

    public List<ToDoModel> getTasks() {
        List<ToDoModel > tasks = new LinkedList<>();

        String queryString = "SELECT * FROM " + TODO_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int status = cursor.getInt(1);
                String taskText = cursor.getString(2);

                tasks.add(new ToDoModel(id, status, taskText));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return tasks;
    }

    public boolean deleteTask(ToDoModel task) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TODO_TABLE, ID + " = ?", new String[]{String.valueOf(task.getId())}) > 0;
    }





}
