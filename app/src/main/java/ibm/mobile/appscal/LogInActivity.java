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

public class LogInActivity extends AppCompatActivity {
    TextView tvDaftarSini;
    EditText etEmailLogin, etPassLogin;
    Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

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
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    String email = etEmailLogin.getText().toString();
                    String password = etPassLogin.getText().toString();

                    AppDatabase db = AppDatabase.getInstance(LogInActivity.this);
                    UserDao userDao = db.userDao();

                    new Thread(() -> {
                        User user = userDao.login(email, password);
                        if (user != null) {
                            runOnUiThread(() -> showToast("Login successful! Welcome " + user.getUsername()));

                            // TODO:  Tambah Handling Setelah sukses dibawah ini

//                            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                        } else {
                            runOnUiThread(() -> showToast("Invalid email or password."));
                        }
                    }).start();
                }
            }
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