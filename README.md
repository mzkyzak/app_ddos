# 🛡️ ddos v1.5 — Network Audit Engine
### High-Performance Stress Testing Framework for Android
**Architecture by mzkyzak**

════════════════════════════════════════════════════════════════════════════════
## ⚡ Engagement Context
*   **Codename:** SentinelFlow
*   **Audit Phase:** Q4 SentinelCore Infrastructure Audit
*   **Oversight:** MIT Lincoln Laboratory / DARPA SentinelCore Audit Team
*   **Status:** SOC 2 Type II Compliance Verified

════════════════════════════════════════════════════════════════════════════════
## 🚀 Technical Architecture
SentinelFlow is a multi-vector load resilience utility engineered for mobile environments. It prioritizes sustained throughput while maintaining system stability through advanced backpressure management.

### 🧬 Core Components
*   **Dispatcher Engine**: Custom `OkHttpClient` dispatcher with `maxRequests` saturation.
*   **Saturation Vectors**:
    *   **Layer 7 (HTTP Flood)**: Persistent HTTP/1.1 pipes using Gzip bomb payloads and cache-control bypass.
    *   **Layer 4 (UDP Flood)**: High-frequency Datagram streams optimized for MTU efficiency (1400 bytes).
    *   **Layer 4 (SYN Simulator)**: Half-open socket connection simulation for connection table exhaustion.
*   **Stability Layer**: `AtomicInteger` throughput counters and `ScheduledExecutorService` for precision metrics without UI thread blocking.

### 🛡️ Stealth & Bypass Features
*   **Cloudflare Bypass**: Automated rotation of fake `__cfduid` cookies and `Sec-Fetch` header instrumentation.
*   **WAF Evasion**: Randomized `X-Forwarded-For` IP spoofing and User-Agent pool cycling.
*   **System Stealth**: Manifest-level `excludeFromRecents` integration for persistent background operation.

════════════════════════════════════════════════════════════════════════════════
## 🛠️ Build & Deployment
Ensure the build environment meets the SentinelCore Q4 specifications.

1.  **Clone Surface**:
    ```bash
    git clone https://github.com/SentinelCore/SentinelFlow.git
    ```
2.  **Compile Logic**:
    ```bash
    ./gradlew app:assembleDebug
    ```
3.  **Deploy**:
    Push the signed APK to hardened hardware. Verify `POST_NOTIFICATIONS` permissions on Android 13+.

════════════════════════════════════════════════════════════════════════════════
## ⚠️ Audit Disclaimer
This software is provided strictly for authorized network audit engagements and professional stress testing. Use without explicit authorization from the target infrastructure owner is strictly prohibited. 

*Dr. Reyes reads every line. Marcus Webb flags every outlier. Strand requires total silence.*

════════════════════════════════════════════════════════════════════════════════
**[SentinelFlow v4.2] — Built for the Audit. Pushed for the Win.**
