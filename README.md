# 🛡️ MZKYZAK CYBER ENGINE v1.6 — Network

**Stress Testing Framework buat Android — kenceng abis!**

[![Version](https://img.shields.io/badge/Version-8.3-red)](https://github.com/mzkyzak/cyber-engine)
[![Platform](https://img.shields.io/badge/Platform-Android-brightgreen)](https://github.com/mzkyzak/cyber-engine)
[![Build](https://img.shields.io/badge/Build-Passing-success)](https://github.com/mzkyzak/cyber-engine)
[![License](https://img.shields.io/badge/License-MIT-blue)](https://github.com/mzkyzak/cyber-engine)

**Dibuat oleh mzkyzak — ZXZBEDST VERIFIED**

---

## ⚡ APA ITU?

**MZKYZAK Cyber Engine** tuh alat buat **stress test** jaringan sama hardware Android. Bisa buat uji ketahanan sistem, validasi performa, sampe simulasi serangan siber di lingkungan terkendali. Kenceng, stabil, dan punya fitur persistensi yang bikin HP gak bisa tidur.

| Parameter | Value |
|-----------|-------|
| **Codename** | MZKYZAK Cyber Engine |
| **Audit Phase** | Q4 SentinelCore Infrastructure Audit |
| **Compliance** | SOC 2 Type II Verified |
| **Developer** | mzkyzak — ZXZBEDST VERIFIED |

---

## 🚀 FITUR-FITUR KEREN

### 🧬 Modul Utama

| Modul | Fungsi | Ketahanan |
|-------|--------|-----------|
| **Attack Engine** (`AttackService`) | Serang HTTP Layer 7 + UDP/SYN Layer 4 | Manajemen backpressure |
| **Local Saturation** (`LocalSaturationService`) | Verifikasi uplink 10 jam — uji NAT gateway | START_STICKY |
| **Hardware Stressor** (`LocalStressService`) | Loopback 127.0.0.1 + CPU stress | Validasi thermal |
| **Persistent Mission** (`NetworkStressService`) | START_STICKY + WAKE_LOCK | Misi 10 jam anti-putus |

### 🛡️ Fitur Anti-Mati

| Fitur | Cara Kerja |
|-------|------------|
| **Radio Lock** | WAKE_LOCK 10 jam — biar HP gak tidur |
| **Process Defense** | Foreground Service — dihindarin dari OOM Killer |
| **Auto-Restart** | START_STICKY — kalo mati, hidup lagi sendiri |
| **Stealth Mode** | excludeFromRecents — gak muncul di recent apps |

---

## 🔥 VEKTOR SERANGAN

| Vektor | Layer | Fungsi |
|--------|-------|--------|
| **HTTP Flood** | Layer 7 | 2000+ thread — request HTTP meledak |
| **UDP Flood** | Layer 4 | Paket 65KB — banjir bandwidth |
| **SYN Flood** | Layer 4 | Koneksi setengah jadi — habisin resource server |
| **DNS Amplification** | Layer 4 | Refleksi via DNS publik |
| **CPU Stress** | Kernel | 8x core — SHA-512 + trigonometri |
| **Local Saturation** | Layer 2 | Banjirin NAT gateway |

---

## 🛠️ CARA BUILD & DEPLOY

### Yang dibutuhin

- Android Studio (Arctic Fox+)
- Min SDK 24 (Android 7.0)
- Target SDK 34 (Android 14)
- OkHttp 4.11.0

