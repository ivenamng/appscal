package ibm.mobile.appscal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBarConsumed;
    private float progr = 0;
    private static final int REQUEST_MENU = 101;
    private static final int REQUEST_MENU_LUNCH = 102;
    private static final int REQUEST_MENU_DINNER = 103;

    private int kalori;
    private int kaloriMakan;
    private int kaloriKonsumsiPagi = 0;
    private int kaloriKonsumsiSiang = 0;
    private int kaloriKonsumsiMalam = 0;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ambil data
        Intent intentU = getIntent();
        String gender = intentU.getStringExtra("GENDER_INPUT");
        String inputBB = intentU.getStringExtra("BB_INPUT");
        String inputTB = intentU.getStringExtra("TB_INPUT");
        String inputU = intentU.getStringExtra("UMUR_INPUT");

        TextView sarapan = findViewById(R.id.TargetFixSarapan);
        TextView lunch = findViewById(R.id.TargetFixLunch);
        TextView dinner = findViewById(R.id.TargetFixDinner);
        TextView caloriesLeft = findViewById(R.id.CaloriesLeftNumb);
        TextView caloriesEaten = findViewById(R.id.CaloriesEatenNumb);

        int bb = Integer.parseInt(inputBB);
        int tb = Integer.parseInt(inputTB);
        int umr = Integer.parseInt(inputU);

        progressBarConsumed = findViewById(R.id.progressBar);

        // Inisialisasi SharedPreferences
        sharedPreferences = getPreferences(MODE_PRIVATE);

        // Target fix sarapan, makan siang, makan malam
        if (gender.equals("pria")) {
            kalori = (int) Math.ceil(66.5 + (13.75 * bb) + (5.003 * tb) - (6.75 * umr));
            kaloriMakan = 0;

            int sarapanPria = (int) Math.ceil(0.25 * kalori);
            int lunchPria = (int) Math.ceil(0.4 * kalori);
            int dinnerPria = (int) Math.ceil(0.35 * kalori);

            sarapan.setText(Integer.toString(sarapanPria));
            lunch.setText(Integer.toString(lunchPria));
            dinner.setText(Integer.toString(dinnerPria));
            caloriesLeft.setText(Integer.toString(kalori));
            caloriesEaten.setText(Integer.toString(kaloriMakan));

        } else if (gender.equals("wanita")) {
            kalori = (int) Math.ceil(655.1 + (9.563 * bb) + (1.850 * tb) - (4.676 * umr));
            kaloriMakan = 0;

            int sarapanWanita = (int) Math.ceil(0.25 * kalori);
            int lunchWanita = (int) Math.ceil(0.4 * kalori);
            int dinnerWanita = (int) Math.ceil(0.35 * kalori);

            sarapan.setText(Integer.toString(sarapanWanita));
            lunch.setText(Integer.toString(lunchWanita));
            dinner.setText(Integer.toString(dinnerWanita));
            caloriesLeft.setText(Integer.toString(kalori));
            caloriesEaten.setText(Integer.toString(kaloriMakan));
        }

        // Tombol sarapan, makan siang, makan malam
        Button btn_sarapan = findViewById(R.id.buttonSarapan);
        Button btn_lunch = findViewById(R.id.buttonLunch);
        Button btn_dinner = findViewById(R.id.buttonDinner);

        btn_sarapan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = "sarapan";
                startDaftarMenuActivity(time);
            }
        });

        btn_lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = "lunch";
                startDaftarMenuActivity(time);
            }
        });

        btn_dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = "dinner";
                startDaftarMenuActivity(time);
            }
        });
    }

    private void startDaftarMenuActivity(String time) {
//        Intent intent = new Intent(MainActivity.this, DaftarMenuActivity.class);
//        intent.putExtra("TIME", time);
//        startActivityForResult(intent, getRequestCode(time));
    }

    private int getRequestCode(String time) {
        switch (time) {
            case "sarapan":
                return REQUEST_MENU;
            case "lunch":
                return REQUEST_MENU_LUNCH;
            case "dinner":
                return REQUEST_MENU_DINNER;
            default:
                return -1;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String time = getTimeFromRequestCode(requestCode);

        if (resultCode == RESULT_OK) {
            int consumedCalories = data.getIntExtra("KONSUMSI", 0);
            String namaMakanan = data.getStringExtra("MAKANAN");

            updateKaloriKonsumsi(time, consumedCalories);
            updateSharedPreferences();

            float progressPercentage = (float) (kaloriKonsumsiPagi+kaloriKonsumsiSiang+kaloriKonsumsiMalam) / kalori * 100;

            // Set nilai progress bar
            progressBarConsumed.setProgress((int) progressPercentage);

            // Set TextView KonsumsiSarapan, KonsumsiLunch, atau KonsumsiDinner sesuai waktu
            TextView konsumsiTextView;
            switch (time) {
                case "sarapan":
                    konsumsiTextView = findViewById(R.id.TargetSarapan);
                    break;
                case "lunch":
                    konsumsiTextView = findViewById(R.id.TargetLunch);
                    break;
                case "dinner":
                    konsumsiTextView = findViewById(R.id.TargetDinner);
                    break;
                default:
                    konsumsiTextView = null;
            }

            if (konsumsiTextView != null) {
                konsumsiTextView.setText(String.valueOf(consumedCalories));

                TextView caloriesLeft = findViewById(R.id.CaloriesLeftNumb);
                TextView caloriesEaten = findViewById(R.id.CaloriesEatenNumb);
                TextView foodNameSarapan = findViewById(R.id.InfoMakan);
                TextView foodNameLunch = findViewById(R.id.InfoMakanLunch);
                TextView foodNameDinner = findViewById(R.id.InfoMakanDinner);

                kalori -= consumedCalories;
                caloriesLeft.setText(Integer.toString(kalori));
                caloriesEaten.setText(Integer.toString(consumedCalories));

                if(time.equals("sarapan")) {
                    foodNameSarapan.setText(namaMakanan);
                } else if(time.equals("lunch")) {
                    foodNameLunch.setText(namaMakanan);
                } else if(time.equals("dinner")) {
                    foodNameDinner.setText(namaMakanan);
                }
            }
        }
    }

    private void updateKaloriKonsumsi(String time, int consumedCalories) {
        if (time.equals("sarapan")) {
            kaloriKonsumsiPagi += consumedCalories;
        } else if (time.equals("lunch")) {
            kaloriKonsumsiSiang += consumedCalories;
        } else if (time.equals("dinner")) {
            kaloriKonsumsiMalam += consumedCalories;
        }
    }

    private void updateSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("kaloriKonsumsiPagi", kaloriKonsumsiPagi);
        editor.putInt("kaloriKonsumsiSiang", kaloriKonsumsiSiang);
        editor.putInt("kaloriKonsumsiMalam", kaloriKonsumsiMalam);
        editor.apply();
    }

    private String getTimeFromRequestCode(int requestCode) {
        switch (requestCode) {
            case REQUEST_MENU:
                return "sarapan";
            case REQUEST_MENU_LUNCH:
                return "lunch";
            case REQUEST_MENU_DINNER:
                return "dinner";
            default:
                return "";
        }
    }
}