import java.io.IOException;

public class ApiThread implements Runnable{
    private final String value;
    private final String key;
    private final String token;

    public ApiThread(String value, String key, String token) {
        this.value = value;
        this.key = key;
        this.token = token;
    }

    @Override
    public void run() {
        try {
            ApiCall.ApiDescription(value, key, token);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
