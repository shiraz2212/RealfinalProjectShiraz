package com.example.finalprojectshiraz;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Adoption extends AppCompatActivity {

    @Override
    /**
     * هذه الدالة تستدعي في كل مرة يتم انشاء هذا النموذج لتعيين المحتوي
     *
     * @param savedInstanceState هذا المتغير لاستدعاء الكلاس الأب للنموذج الذي تم انشاءه فيه
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adoption);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}