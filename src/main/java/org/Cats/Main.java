package org.Cats;

public class Main {
    public static void main(String[] args) {
        SaveManager saveManager = new SaveManager();
        // Create the data directory if it doesn't exist
        saveManager.createDirectory();

        ApiManager apiManager = new ApiManager();
        // Fetch the cat image from the API and save it
        apiManager.requestImageFromApi();
    }
}
