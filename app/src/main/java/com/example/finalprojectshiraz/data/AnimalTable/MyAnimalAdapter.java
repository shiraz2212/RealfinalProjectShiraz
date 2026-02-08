package com.example.finalprojectshiraz.data.AnimalTable;
import static com.example.finalprojectshiraz.R.*;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshiraz.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyAnimalAdapter extends ArrayAdapter<Animal>
{
    //המזהה של קובץ עיצוב הפריט
    /**
     * constructor

     * @param context קישור להקשר (מסך- אקטיביטי)
     * @param resource עיצוב של פריט שיציג הנתונים של העצם
     */

    public MyAnimalAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.itemLayout = resource;
    }
    private final int itemLayout;

    /**
     *

     * בונה פריט גרפי אחד בהתאם לעיצוב והצגת נתוני העצם עליו
     * @param position מיקום הפריט החל מ 0
     * @param convertView
     * @param parent רכיב האוסף שיכיל את הפריטים כמו listview
     * @return . פריט גרפי שמציג נתוני עצם אחד
     */

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 1. Build the graphic item from the design file
        View vitem = convertView;
        if (vitem == null)
            vitem = LayoutInflater.from(getContext()).inflate(itemLayout, parent, false);

        // 2. Get references to components in the design file
        ImageView imageView = vitem.findViewById(R.id.imgVitm);
        TextView tvItmTitle = vitem.findViewById(R.id.tvItmTitle);
        TextView tvItmText = vitem.findViewById(R.id.tvItmText);
        TextView tvItemImportance = vitem.findViewById(R.id.tvItemImportance);

        ImageButton btnSendSMS = vitem.findViewById(R.id.imgBtnSendSmsitm);
        ImageButton btnEdit = vitem.findViewById(R.id.imgBtnEdititm);
        ImageButton btnCall = vitem.findViewById(R.id.imgBtnCallitm);
        ImageButton btnDel = vitem.findViewById(R.id.imgBtnDeleteitm);

        // 3. Get the current Animal object
        Animal current = getItem(position);
        tvItmTitle.setText(current.getName());
        tvItmText.setText(current.getDescription());
        tvItemImportance.setText("importance:"+current.getType());

        // 4. Display data on the graphic components
        if (current != null) {
            // Check your Animal.java file for these method names:
            tvItmTitle.setText(current.getName()); // Usually getName() for Animals
            tvItmText.setText(current.getBreed()); // Usually getBreed()
            tvItemImportance.setText("Age: " + current.getAge()); // Usually getAge()
        }

        return vitem;
    }
}


