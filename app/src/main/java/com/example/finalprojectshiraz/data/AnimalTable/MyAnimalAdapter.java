package com.example.finalprojectshiraz.data.AnimalTable;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finalprojectshiraz.Map;
import com.example.finalprojectshiraz.R;
import com.example.finalprojectshiraz.data.AppDatabase;

import java.util.List;

public class MyAnimalAdapter extends ArrayAdapter<Animal> {
    private final int itemLayout;

    public MyAnimalAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.itemLayout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View vitem = convertView;
        if (vitem == null) {
            vitem = LayoutInflater.from(getContext()).inflate(itemLayout, parent, false);
        }

        Animal current = getItem(position);

        ImageView imageView = vitem.findViewById(R.id.imgVitm);
        TextView tvTitle = vitem.findViewById(R.id.tvItmTitle);
        TextView tvType = vitem.findViewById(R.id.tvItmType);
        TextView tvAge = vitem.findViewById(R.id.tvItmAge);
        TextView tvLocation = vitem.findViewById(R.id.tvItmLocation);
        ImageView btnMap = vitem.findViewById(R.id.imgBtnMap);
        ImageView btnDelete = vitem.findViewById(R.id.imgBtnDeleteitm);

        if (current != null) {
            tvTitle.setText(current.getName());
            tvType.setText("Type: " + current.getType());
            tvAge.setText("Age: " + current.getAge());
            tvLocation.setText("Location: " + current.getLocation());

            // Image loading logic would go here (e.g., using Glide or URI)
            // imageView.setImageURI(Uri.parse(current.getImage()));

            btnMap.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), Map.class);
                intent.putExtra("ANIMAL_NAME", current.getName());
                intent.putExtra("LOCATION", current.getLocation());
                getContext().startActivity(intent);
            });

            btnDelete.setOnClickListener(v -> {
                new Thread(() -> {
                    AppDatabase.getDB(getContext()).animalQuery().delete(current);
                    ((android.app.Activity)getContext()).runOnUiThread(() -> {
                        remove(current);
                        notifyDataSetChanged();
                        Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show();
                    });
                }).start();
            });
        }

        return vitem;
    }
}
