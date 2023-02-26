package org.Cats;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class ApiManager {
    private static final String CAT_API = "https://api.thecatapi.com/v1/images/search";
    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final Gson gson = new Gson();

    /**
     * Fetches a cat image from the Cat API, saves it to a file, and handles errors
     */
    public void requestImageFromApi() {
        try {
            // Fetch the cat image from the API
            Request request = new Request.Builder().url(CAT_API).get().build();
            Response response = httpClient.newCall(request).execute();
            // Get the response body and parse it into an array of CatImage objects
            String responseBody = response.body().string();
            CatImage[] catImages = gson.fromJson(responseBody, CatImage[].class);
            // Check if any cat images were returned
            if (catImages.length == 0) {
                System.err.println("No cat images found in response");
                return;
            }
            // Create a SaveManager object to save the image to a file
            SaveManager saveManager = new SaveManager();
            // Create the data directory if it doesn't already exist
            saveManager.createDirectory();
            // Save the cat image to a file
            saveManager.saveImage(catImages);
        } catch (IOException e) {
            System.err.println("Error requesting cat image from API: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Error creating data directory: " + e.getMessage());
        }
    }
}