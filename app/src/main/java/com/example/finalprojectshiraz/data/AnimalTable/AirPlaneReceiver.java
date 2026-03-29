package com.example.finalprojectshiraz.data.AnimalTable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

public class AirPlaneReceiver extends BroadcastReceiver {
    private Button save;//נגדיר תכונה של לחצן בתוך המחלקה.
    public AirPlaneReceiver(Button btnAddTask) { //נצור שיטה בונה המקבלת כפרמטר הפניה של הלחצן, ונציב בתוך התכונה.
        save=btnAddTask;
    }

    public  AirPlaneReceiver()
    {

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // Check if the action is Airplane Mode change
        if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(intent.getAction())) {
            boolean isEnabled = intent.getBooleanExtra("state", false);
            if (isEnabled) {
                Toast.makeText(context, "System: Airplane Mode is ON. Sync is disabled.", Toast.LENGTH_LONG).show();
                save.setEnabled(false);//נשנה את המצב הלחצן בתוך onReceive
                save.setText("AirPalne is on");//נשנה את המצב הלחצן בתוך onReceive
            } else {
                Toast.makeText(context, "System: Airplane Mode is OFF. Sync is back!", Toast.LENGTH_LONG).show();
                save.setEnabled(true);//נשנה את המצב הלחצן בתוך onReceive
                save.setText("AirPalne is off");//נשנה את המצב הלחצן בתוך onReceive
            }
        }
    }
}



