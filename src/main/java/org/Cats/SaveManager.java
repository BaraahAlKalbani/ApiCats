package org.Cats;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

public class SaveManager {
    private File dataDirectory;

    public SaveManager() {
        this.dataDirectory = new File("data");
    }

    /**
     * Creates the data directory if it doesn't already exist
     */
    public void createDirectory() {
        // Create the data directory if it doesn't exist
        if (!dataDirectory.exists()) {
            if (!dataDirectory.mkdir()) {
                // If the directory cannot be created, print an error message
                try {
                    System.err.println("Could not create data directory");
                }catch (RuntimeException e)
                {
                    System.out.println("Unable to create data directory");
                }
            }
        }
    }
    /**
     * Saves the cat image to a file
     * @param catImages an array of CatImage objects to save to a file
     */
    public void saveImage(CatImage[] catImages) {
        try {
            // Extract the image URL and generate a unique image name
            String imageUrl = catImages[0].getUrl();
            String imageName = UUID.randomUUID().toString() + ".jpg";
            // Create a new file with the generated name
            File imageFile = new File(dataDirectory, imageName);
            // Check if a file with the same name already exists
            if (imageFile.exists()) {
                System.out.println("A cat image with the same name already exists: " + imageName);
                return;
            }
            // Check if a file with the same name already exists
            if (imageFile.exists()) {
                // If a file with the same name exists, print a message and return
                System.out.println("A cat image with the same name already exists: " + imageName);
                return;
            }
            // Read the image from the URL and write it to the file
            BufferedImage image = ImageIO.read(new URL(imageUrl));
            ImageIO.write(image, "jpg", imageFile);
            // Print a message indicating that the image has been saved
            System.out.println("Saved cat image: " + imageName);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}