package com.example.finalprojectshiraz.data.AnimalTable;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.finalprojectshiraz.Map;
import com.example.finalprojectshiraz.R;
import com.example.finalprojectshiraz.data.AppDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
            tvType.setText("Breed: " + (current.getBreed() != null ? current.getBreed() : "N/A"));
            tvAge.setText("Age: " + current.getAge());
            tvLocation.setText("Location: " + (current.getLocation() != null ? current.getLocation() : "N/A"));

            // تحميل الصورة باستخدام Glide
            // Glide يستطيع التعامل مع الروابط (URL) وحتى نصوص Base64 تلقائياً
            if (current.getImage() != null && !current.getImage().isEmpty()) {
                    imageView.setImageBitmap(stringToBitmap(current.getImage()));
            } else {
                imageView.setImageResource(android.R.drawable.ic_menu_camera);
            }

            btnMap.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), Map.class);
                intent.putExtra("ANIMAL_NAME", current.getName());
                intent.putExtra("LOCATION", current.getLocation());
                getContext().startActivity(intent);
            });


            btnDelete.setOnClickListener(v -> {
                new android.app.AlertDialog.Builder(getContext())
                        .setTitle("Delete Animal")
                        .setMessage("Are you sure you want to delete this animal?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            deleteAnimal(current);
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            });



        }

        return vitem;
    }

    private void deleteAnimal(Animal current) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("animals");
        dbRef.child(current.getKeyid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "Animal deleted successfully", Toast.LENGTH_SHORT).show();
                    new Thread(() -> {
                        AppDatabase.getDB(getContext()).animalQuery().delete(current);
                        ((android.app.Activity)getContext()).runOnUiThread(() -> {
                            remove(current);
                            notifyDataSetChanged();
                            Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show();
                        });
                    }).start();
                }
                else {
                    Toast.makeText(getContext(), "Failed to delete animal", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    /**
     * Decodes the image string and returns the corresponding Bitmap object.
     *
     * @param imageString the image string to decode
     * @return the decoded Bitmap object
     */
    private Bitmap stringToBitmap(String imageString) {
        if (imageString == null || imageString.isEmpty()) return null;
        try {
            byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            return null;
        }
    }


}
