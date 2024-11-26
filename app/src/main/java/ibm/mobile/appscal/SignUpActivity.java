package ibm.mobile.appscal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpActivity extends AppCompatActivity {

    TextView tvMasukSini;
    EditText etUserSignup, etEmailSignup, etPassSignup;
    Button btSignUp;

    private DatabaseReference userDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userDb = FirebaseDatabase.getInstance().getReference("users");

        // inisialisasi elemen
        tvMasukSini = findViewById(R.id.tvMasukSini);
        etUserSignup = findViewById(R.id.etUserSignUp);
        etEmailSignup = findViewById(R.id.etEmailSignUp);
        etPassSignup = findViewById(R.id.etPassSignUp);
        btSignUp = findViewById(R.id.btSignUp);

        // text-link Log-In jika sudah ada akun
        tvMasukSini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Button Sign-Up
        btSignUp.setOnClickListener(view -> {
            if (isValid()) {
                String username = etUserSignup.getText().toString();
                String email = etEmailSignup.getText().toString();
                String password = etPassSignup.getText().toString();

                String id = userDb.push().getKey(); // Generate unique key
                User user = new User(id, username, email, password);

                userDb.child(id).setValue(user)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Sign Up Berhasil!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, GenderActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Sign Up Gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

    }

    private boolean isValid() {
        String username = etUserSignup.getText().toString().trim();
        String email = etEmailSignup.getText().toString().trim();
        String password = etPassSignup.getText().toString();

        if (username.isEmpty()) {
            showToast("Username cannot be empty");
            return false;
        }

        if (email.isEmpty()) {
            showToast("Email cannot be empty");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Invalid email format");
            return false;
        }

        if (password.isEmpty()) {
            showToast("Password cannot be empty");
            return false;
        }

        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}