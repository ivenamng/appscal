package ibm.mobile.appscal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GenderActivity extends AppCompatActivity {
    private Button btPria, btWanita;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);

        // inisialisasi elemen
        btPria = findViewById(R.id.btPria);
        btWanita = findViewById(R.id.btWanita);

        // next step for Pria
        btPria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gender = "pria";
//
//                Intent intent = new Intent(GenderActivity.this, beratBadanActivity.class);
//                intent.putExtra("GENDER_INPUT", gender);
//                startActivity(intent);
//                finish();
            }
        });

        // next step for Wanita
        btWanita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gender = "wanita";
//
//                Intent intent = new Intent(GenderActivity.this, beratBadanActivity.class);
//                intent.putExtra("GENDER_INPUT", gender);
//                startActivity(intent);
//                finish();
            }
        });

    }
}