package services;

import okhttp3.*;

import java.io.IOException;

public class InfobipService {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String ACCEPT_HEADER = "Accept";

    private static final String API_KEY = "d51e17cf8f1db1dcd41fc89d3887049e-ae3b01ef-a622-4ada-817a-afe0e9916e6d";
    private static final String BASE_URL = "https://kg5e8.api.infobip.com";

    public void sendSMS(String to, String body) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, "{\"messages\":[{\"destinations\":[{\"to\":\"" + to + "\"}],\"from\":\"ServiceSMS\",\"text\":\"" + body + "\"}]}");

        Request request = new Request.Builder()
                .url(BASE_URL + "/sms/2/text/advanced")
                .method("POST", requestBody)
                .addHeader(AUTHORIZATION_HEADER, "App " + API_KEY)
                .addHeader(CONTENT_TYPE_HEADER, "application/json")
                .addHeader(ACCEPT_HEADER, "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            System.out.println("Message sent successfully! Response: " + response.body().string());
        }
    }
}
