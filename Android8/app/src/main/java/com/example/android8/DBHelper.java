package com.example.android8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

// Создаем класс для работы с базой данных
public class DBHelper extends SQLiteOpenHelper {

    // Название базы данных
    public static final String DATABASE_NAME = "students.db";

    // Название таблицы
    public static final String TABLE_NAME = "students";

    // Названия столбцов
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AGE = "age";

    // Конструктор класса
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    // Метод для создания таблицы
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создаем SQL-запрос для создания таблицы
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_AGE + " INTEGER" +
                ")";
        // Выполняем SQL-запрос
        db.execSQL(sql);
    }

    // Метод для обновления таблицы
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Удаляем старую таблицу
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Создаем новую таблицу
        onCreate(db);
    }

    // Метод для добавления студента в базу данных
    public boolean insertStudent(Student student) {
        // Получаем объект для записи данных
        SQLiteDatabase db = this.getWritableDatabase();
        // Создаем объект для хранения значений
        ContentValues contentValues = new ContentValues();
        // Добавляем значения в объект
        contentValues.put(COLUMN_NAME, student.getName());
        contentValues.put(COLUMN_AGE, student.getAge());
        // Вставляем данные в таблицу
        long result = db.insert(TABLE_NAME, null, contentValues);
        // Закрываем соединение с базой данных
        db.close();
        // Возвращаем результат операции
        return result != -1;
    }

    // Метод для получения всех студентов из базы данных
    public Cursor getAllStudents() {
        // Получаем объект для чтения данных
        SQLiteDatabase db = this.getReadableDatabase();
        // Создаем SQL-запрос для получения всех данных из таблицы с псевдонимом для столбца id
        String sql = "SELECT id AS _id, name, age FROM " + TABLE_NAME;
        // Получаем курсор для обхода результатов запроса
        Cursor cursor = db.rawQuery(sql, null);
        // Возвращаем курсор
        return cursor;
    }

    // Метод для обновления данных студента в базе данных
    public boolean updateStudent(Student student) {
        // Получаем объект для записи данных
        SQLiteDatabase db = this.getWritableDatabase();
        // Создаем объект для хранения значений
        ContentValues contentValues = new ContentValues();
        // Добавляем значения в объект
        contentValues.put(COLUMN_NAME, student.getName());
        contentValues.put(COLUMN_AGE, student.getAge());
        // Обновляем данные в таблице по идентификатору студента
        int result = db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[]{String.valueOf(student.getId())});
        // Закрываем соединение с базой данных
        db.close();
        // Возвращаем результат операции
        return result > 0;
    }

    // Метод для удаления студента из базы данных
    public boolean deleteStudent(int id) {
        // Получаем объект для записи данных
        SQLiteDatabase db = this.getWritableDatabase();
        // Удаляем данные из таблицы по идентификатору студента
        int result = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        // Закрываем соединение с базой данных
        db.close();
        // Возвращаем результат операции
        return result > 0;
    }
}
