# 🛡️ DDOS v1.5 — masih tahap/next target device overload internet

**High-Performance Stress Testing Framework for Android**

[![Version](https://img.shields.io/badge/Version-1.5-red)](https://github.com/mzkyzak/ddos)
[![Platform](https://img.shields.io/badge/Platform-Android-brightgreen)](https://github.com/mzkyzak/ddos)
[![Build](https://img.shields.io/badge/Build-Passing-success)](https://github.com/mzkyzak/ddos)
[![License](https://img.shields.io/badge/License-MIT-blue)](https://github.com/mzkyzak/ddos)

**Architecture by mzkyzak**  
**Codename:** SentinelFlow  
---

## ⚡ Engagement Context

SentinelFlow is a multi-vector load resilience utility engineered for mobile environments. It prioritizes sustained throughput while maintaining system stability through advanced backpressure management.

**Dr. Reyes reads every line. Marcus Webb flags every outlier. Strand requires total silence.**

---

## 🚀 Technical Architecture

### 🧬 Core Components

| Component | Description |
|-----------|-------------|
| **Dispatcher Engine** | Custom `OkHttpClient` dispatcher with `maxRequests` saturation |
| **Stability Layer** | `AtomicInteger` throughput counters & `ScheduledExecutorService` for precision metrics |
| **Thread Pool** | CPU core × 6 — scalable & stable |
| **Wake Lock** | CPU tetap aktif selama serangan |
| **Foreground Service** | Prioritas tinggi, tidak di-kill sistem |

### 📡 Attack Vectors

| Vector | Layer | Description |
|--------|-------|-------------|
| **HTTP Flood** | Layer 7 | Persistent HTTP/1.1 pipes with Gzip bomb payloads & cache-control bypass |
| **UDP Flood** | Layer 4 | High-frequency Datagram streams optimized for MTU efficiency (1400 bytes) |
| **SYN Simulator** | Layer 4 | Half-open socket connection simulation for connection table exhaustion |
| **DNS Amplification** | Layer 4 | Reflection via public DNS resolvers (8.8.8.8, 1.1.1.1, 9.9.9.9) |

### 🛡️ Stealth & Bypass

| Feature | Function |
|---------|----------|
| **Cloudflare Bypass** | Automated rotation of fake `__cfduid` cookies & `cf_clearance` injection |
| **WAF Evasion** | Randomized `X-Forwarded-For` IP spoofing & User-Agent pool cycling |
| **System Stealth** | Manifest-level `excludeFromRecents` for persistent background operation |
| **Gzip Bomb** | 1KB compressed → 50MB decompressed — CPU & memory exhaustion |
| **Range Header** | Request partial content (bytes=0-XXXX) — heavy server parsing |

---

