package com.mzkyzak.ddospro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
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
    private Button btnStart, btnStop, btnSaturate, btnStressHp, btnProfStress;
    private TextView tvStatus, tvMethod, tvStats;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isRunning = false;
    private boolean isProfStressing = false;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        etTarget = findViewById(R.id.etTarget);
        etThreads = findViewById(R.id.etThreads);
        etDuration = findViewById(R.id.etDuration);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnSaturate = findViewById(R.id.btnSaturate);
        btnStressHp = findViewById(R.id.btnStressHp);
        btnProfStress = findViewById(R.id.btnProfStress);
        tvStatus = findViewById(R.id.tvStatus);
        tvMethod = findViewById(R.id.tvMethod);
        tvStats = findViewById(R.id.tvStats);

        etTarget.setText("https://target-audit.internal");
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        btnStart.setOnClickListener(v -> startMission(AttackService.class));
        btnStop.setOnClickListener(v -> stopMission());
        btnSaturate.setOnClickListener(v -> startMission(LocalSaturationService.class));
        btnStressHp.setOnClickListener(v -> startMission(LocalStressService.class));
        btnProfStress.setOnClickListener(v -> toggleProfStress());
    }

    private void startMission(Class<?> serviceClass) {
        if (isRunning) return;
        
        String target = etTarget.getText().toString().trim();
        int threads = Integer.parseInt(etThreads.getText().toString());
        int duration = Integer.parseInt(etDuration.getText().toString());

        Intent intent = new Intent(this, serviceClass);
        intent.putExtra("target", target);
        intent.putExtra("threads", threads);
        intent.putExtra("duration", duration);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

        isRunning = true;
        tvStatus.setText("🟢 RUNNING");
        tvStatus.setTextColor(0xFF00FF00);
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);

        if (vibrator != null && Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        }

        handler.postDelayed(updateStats, 1000);
    }

    private void stopMission() {
        stopService(new Intent(this, AttackService.class));
        stopService(new Intent(this, LocalSaturationService.class));
        stopService(new Intent(this, LocalStressService.class));
        stopService(new Intent(this, NetworkStressService.class));
        
        isRunning = false;
        isProfStressing = false;
        btnProfStress.setText("🛡️ PERSISTENT MISSION (10H)");
        tvStatus.setText("🔴 STANDBY");
        tvStatus.setTextColor(0xFFFF0000);
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        handler.removeCallbacks(updateStats);

        if (vibrator != null && Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    private void toggleProfStress() {
        if (isProfStressing) {
            stopService(new Intent(this, NetworkStressService.class));
            isProfStressing = false;
            btnProfStress.setText("🛡️ PERSISTENT MISSION (10H)");
            tvStatus.setText("🔴 STANDBY");
        } else {
            Intent intent = new Intent(this, NetworkStressService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
            isProfStressing = true;
            btnProfStress.setText("⛔ STOP MISSION");
            tvStatus.setText("🟢 MISSION PERSISTENT");
            tvStatus.setTextColor(0xFF00FF00);
            handler.postDelayed(updateStats, 1000);
        }
    }

    private final Runnable updateStats = new Runnable() {
        @Override
        public void run() {
            if (!isRunning) return;
            // Adaptive stats based on which service is running
            long pCount = AttackService.packetCount.get() + 
                          LocalSaturationService.packetCount.get() + 
                          LocalStressService.packetCount.get() +
                          NetworkStressService.packetCount.get();
            long pSpeed = AttackService.packetPerSecond.get() + 
                          LocalSaturationService.packetPerSecond.get() + 
                          LocalStressService.packetPerSecond.get() +
                          NetworkStressService.packetPerSecond.get();
                          
            tvStats.setText("📡 Paket: " + pCount);
            tvMethod.setText("⚡ Speed: " + pSpeed + "/s");
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMission();
    }
}