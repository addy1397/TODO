package com.a.addy.todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, java.io.Serializable, AdapterView.OnItemSelectedListener {

    public static String IMPORTANCE;
    private Spinner spinner;
    private Button buttonSubmit,buttonPicker,buttonImportance;
    private EditText titleEditText,descriptionEditText,dateEditText;
    private String finaltime,selection;

    private int priority;

    private ArrayList<Task> taskList;

    int day,month,year,hour,minute;
    int dayFinal,monthFinal,yearFinal,hourFinal,minuteFinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        taskList = new ArrayList<>();
        taskList = (ArrayList<Task>) getIntent().getSerializableExtra("TaskList");

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.priority, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(AddActivity.this);


        titleEditText = (EditText) findViewById(R.id.addTitleEditText);
        descriptionEditText = (EditText) findViewById(R.id.addDescriptionEditText);

        buttonPicker = (Button) findViewById(R.id.addDateTimeButton);
        buttonPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calender = Calendar.getInstance();
                year = calender.get(Calendar.YEAR);
                month = calender.get(Calendar.MONTH);
                day = calender.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this,
                        AddActivity.this, year, month, day );
                datePickerDialog.show();
            }
        });

        buttonSubmit = (Button) findViewById(R.id.addAddButton);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title,description,importance;

                title = titleEditText.getText().toString();
                description = descriptionEditText.getText().toString();

                if(selection.equals("High")){
                    priority=1;
                }
                if(selection.equals("Average")){
                    priority=2;
                }
                if(selection.equals("Low")){
                    priority=3;
                }

                Task task = new Task(priority,title,description,finaltime);

                taskList.add(task);

                SharedPreferences sharedPreferences = getSharedPreferences("TASKS",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(taskList);
                editor.putString("taskList",json);
                editor.apply();

                Toast.makeText(AddActivity.this,"Note Added" ,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearFinal = year;
        monthFinal = month;
        dayFinal = dayOfMonth;

        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this, AddActivity.this,
                hour, minute, DateFormat.is24HourFormat(this));

        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hourFinal = hourOfDay;
        minuteFinal = minute;
        finaltime = hourFinal + ":" + minuteFinal + "  " + dayFinal + "-" + monthFinal + "-" + yearFinal;
        buttonPicker.setText(hourFinal + ":" + minuteFinal + "  " + dayFinal + "-" + monthFinal + "-" + yearFinal);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selection = parent.getItemAtPosition(position).toString();
        //Toast.makeText(getApplicationContext(),selection, Toast.LENGTH_SHORT);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
