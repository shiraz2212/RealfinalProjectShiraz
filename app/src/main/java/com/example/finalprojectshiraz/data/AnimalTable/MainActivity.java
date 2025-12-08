package com.example.finalprojectshiraz.data.AnimalTable;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshiraz.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    //تعريف صفات للكائن المرئي spnr
    private Spinner spnrAnimalType;
    private FloatingActionButton fabAddAnimal;
    private ListView lstvAnimals;
    private MyAnimalAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstvAnimals=(ListView) findViewById(R.id.lstvAnimals);
        MyAnimalAdapter=new MyAnimalAdapter(this,R.layout.task_item_layout)
        lstvAnimals.setAdapter(adapter);

    }

}
