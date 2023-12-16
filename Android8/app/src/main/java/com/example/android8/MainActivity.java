package com.example.android8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    // Создаем объекты для отображения элементов интерфейса
    private ListView listView;
    private EditText editTextId;
    private EditText editTextName;
    private EditText editTextAge;
    private Button buttonAdd;
    private Button buttonUpdate;
    private Button buttonDelete;

    // Создаем адаптер для связи данных с элементами списка
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализируем объект для работы с базой данных
        dbHelper = new DBHelper(this);

        // Инициализируем объекты для отображения элементов интерфейса
        listView = findViewById(R.id.listView);
        editTextId = findViewById(R.id.editTextId);
        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);

        // Инициализируем адаптер для связи данных с элементами списка
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, dbHelper.getAllStudents(), new String[]{DBHelper.COLUMN_NAME}, new int[]{android.R.id.text1}, 0);

        // Устанавливаем адаптер для списка
        listView.setAdapter(adapter);

        // Устанавливаем слушатель для нажатия на элемент списка
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Получаем курсор, связанный с адаптером
                Cursor cursor = adapter.getCursor();
                // Перемещаем курсор на выбранную позицию
                cursor.moveToPosition(position);
                // Получаем данные из курсора
                int studentId = cursor.getInt(cursor.getColumnIndex("_id")); // Используем псевдоним "_id" вместо "id"
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int age = cursor.getInt(cursor.getColumnIndex("age"));
                // Заполняем поля ввода данными студента
                editTextId.setText(String.valueOf(studentId));
                editTextName.setText(name);
                editTextAge.setText(String.valueOf(age));
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем данные из полей ввода
                String name = editTextName.getText().toString();
                int age = Integer.parseInt(editTextAge.getText().toString());
                // Создаем объект студента
                Student student = new Student(0, name, age);
                // Добавляем студента в базу данных
                boolean result = dbHelper.insertStudent(student);
                // Если операция успешна, то обновляем курсор и очищаем поля ввода
                if (result) {
                    Toast.makeText(MainActivity.this, "Студент добавлен", Toast.LENGTH_SHORT).show();
                    adapter.swapCursor(dbHelper.getAllStudents());
                    editTextId.setText("");
                    editTextName.setText("");
                    editTextAge.setText("");
                } else {
                    // Если операция неуспешна, то выводим сообщение об ошибке
                    Toast.makeText(MainActivity.this, "Ошибка добавления студента", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Устанавливаем слушатель для нажатия на кнопку обновления
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем данные из полей ввода
                int id = Integer.parseInt(editTextId.getText().toString());
                String name = editTextName.getText().toString();
                int age = Integer.parseInt(editTextAge.getText().toString());
                // Создаем объект студента
                Student student = new Student(id, name, age);
                // Обновляем данные студента в базе данных
                boolean result = dbHelper.updateStudent(student);
                // Если операция успешна, то обновляем курсор и очищаем поля ввода
                if (result) {
                    Toast.makeText(MainActivity.this, "Студент обновлен", Toast.LENGTH_SHORT).show();
                    adapter.swapCursor(dbHelper.getAllStudents());
                    editTextId.setText("");
                    editTextName.setText("");
                    editTextAge.setText("");
                } else {
                    // Если операция неуспешна, то выводим сообщение об ошибке
                    Toast.makeText(MainActivity.this, "Ошибка обновления студента", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Устанавливаем слушатель для нажатия на кнопку удаления
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем идентификатор студента из поля ввода
                int id = Integer.parseInt(editTextId.getText().toString());
                // Удаляем студента из базы данных
                boolean result = dbHelper.deleteStudent(id);
                // Если операция успешна, то обновляем курсор и очищаем поля ввода
                if (result) {
                    Toast.makeText(MainActivity.this, "Студент удален", Toast.LENGTH_SHORT).show();
                    adapter.swapCursor(dbHelper.getAllStudents());
                    editTextId.setText("");
                    editTextName.setText("");
                    editTextAge.setText("");
                } else {
                    // Если операция неуспешна, то выводим сообщение об ошибке
                    Toast.makeText(MainActivity.this, "Ошибка удаления студента", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}