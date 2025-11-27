package com.example.finalprojectshiraz.data.AnimalTable;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finalprojectshiraz.R;

public class MyAnimalAdapter extends ArrayAdapter<Animal>
{
    //המזהה של קובץ עיצוב הפריט
    private final int itemLayout;
    /**
     * constructor

     * @param context קישור להקשר (מסך- אקטיביטי)
     * @param resource עיצוב של פריט שיציג הנתונים של העצם
     */

    public MyAnimalAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.itemLayout = resource;
    }

    /**
     *

     * בונה פריט גרפי אחד בהתאם לעיצוב והצגת נתוני העצם עליו
     * @param position מיקום הפריט החל מ 0
     * @param convertView
     * @param parent רכיב האוסף שיכיל את הפריטים כמו listview
     * @return . פריט גרפי שמציג נתוני עצם אחד
     */

    @NonNull

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //בניית הפריט הגרפי מתו קובץ העיצוב
        View vitem= convertView;
        if(vitem==null)
            vitem= LayoutInflater.from(getContext()).inflate(itemLayout,parent,false);
        //קבלת הפניות לרכיבים בקובץ העיצוב
        ImageView imageView=vitem.findViewById(R.id.imgVitm);
        TextView tvItemTitle=vitem.findViewById(R.id.tvItemTitle);
        TextView tvItemText=vitem.findViewById(R.id.tvItemText);
        TextView tvItemImportance=vitem.findViewById(R.id.tvItemImportance);
        ImageButton imgBtnSendSms=vitem.findViewById(R.id.imgBtnSendSmsitm);
        ImageButton imgBtnCall=vitem.findViewById(R.id.imgBtnCallitm);
        ImageButton imgBtnEdit=vitem.findViewById(R.id.imgBtnEdititm);
        ImageButton imgBtnDelete=vitem.findViewById(R.id.imgBtnDeleteitm);
        //קבלת הנתון (עצם) הנוכחי
        Animal current=getItem(position);





        return vitem;
    }

}
