package cl.kibernum.apirest.config;

public final class Env {
    private Env() {
    }

    public static String baseUrl() {
        String fromEnv = System.getenv("BASE_URL");
        return (fromEnv == null || fromEnv.isBlank()) ? "http://localhost:8081" : fromEnv;
    }

    public static int timeoutMs() {
        String raw = System.getenv("TIMEOUT_MS");
        if (raw == null || raw.isBlank())
            return 10_000;

        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            return 10_000;
        }
    }

}
