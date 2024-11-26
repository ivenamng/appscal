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


public class SignUpActivity extends AppCompatActivity {

    TextView tvMasukSini;
    EditText etUserSignup, etEmailSignup, etPassSignup;
    Button btSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    String username = etUserSignup.getText().toString();
                    String email = etEmailSignup.getText().toString();
                    String password = etPassSignup.getText().toString();

                    AppDatabase db = AppDatabase.getInstance(SignUpActivity.this);
                    UserDao userDao = db.userDao();

                    new Thread(() -> {
                        if (userDao.checkIfEmailExists(email) > 0) {
                            runOnUiThread(() -> showToast("Email already exists!"));
                            return;
                        }

                        User user = new User(username, email, password);
                        userDao.registerUser(user);
                        runOnUiThread(() -> showToast("User registered successfully!"));

                        // TODO: Tambah Handling Setelah sukses dibawah ini

//                    Intent intent = new Intent(SignUpActivity.this, GenderActivity.class);
//                    startActivity(intent);
//                    finish();
                    }).start();
                }
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