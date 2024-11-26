package ibm.mobile.appscal;

import android.annotation.SuppressLint;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {
    TextView tvDaftarSini;
    EditText etEmailLogin, etPassLogin;
    Button btLogin;

    private DatabaseReference userDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        userDb = FirebaseDatabase.getInstance().getReference("users");

        // inisialisasi elemen
        tvDaftarSini = findViewById(R.id.tvDaftarSini);
        etEmailLogin = findViewById(R.id.etUserLogIn);
        etPassLogin = findViewById(R.id.etPassLogIn);
        btLogin = findViewById(R.id.btLogIn);

        // text-link Sign-up jika belum ada akun
        tvDaftarSini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // button Log-In
        btLogin.setOnClickListener(view -> {
            String email = etEmailLogin.getText().toString();
            String password = etPassLogin.getText().toString();

            userDb.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if (user.getPassword().equals(password)) {
                                Toast.makeText(LogInActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(LogInActivity.this, "Password Salah!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(LogInActivity.this, "User Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(LogInActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    private boolean isValid() {
        String email = etEmailLogin.getText().toString();
        String password = etPassLogin.getText().toString();

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