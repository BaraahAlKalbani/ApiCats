package org.Cats;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SaveManager {
    // the directory where the data will be saved
    private File dataDirectory;

    /**
     * Constructor for SaveManager.
     * Initializes dataDirectory as a new file object with name "data".
     */
    public SaveManager() {
        this.dataDirectory = new File("data");
    }

    /**
     * Creates the data directory if it does not already exist.
     * Throws an exception if it cannot be created.
     */
    public void createDirectory() {
        if (!dataDirectory.exists()) {
            if (!dataDirectory.mkdir()) {
                try {
                    System.err.println("Could not create data directory");
                } catch (RuntimeException e) {
                    System.out.println("Unable to create data directory");
                }
            }
        }
    }

    /**
     * Saves a cat image to the data directory.
     * @param catImages an array of CatImage objects to be saved.
     */
    public void saveImage(CatImage[] catImages) {
        try {
            // get the URL of the first cat image
            String imageUrl = catImages[0].getUrl();
            // read the image from the URL and store it as a BufferedImage
            BufferedImage image = ImageIO.read(new URL(imageUrl));
            // get the image data as a byte array
            byte[] imageData = getImageData(image);
            // generate the hash string from the image data using SHA-256 algorithm
            String imageName = getHashAsString(imageData) + ".jpg";
            // create a new file object with the hashed image name in the data directory
            File imageFile = new File(dataDirectory, imageName);
            // check if a cat image with the same name already exists
            if (imageFile.exists()) {
                System.out.println("A cat image with the same name already exists: " + imageName);
                return;
            }
            // write the image to the file using ImageIO.write() method
            ImageIO.write(image, "jpg", imageFile);
            // print the message to the console indicating that the cat image has been saved
            System.out.println("Saved cat image: " + imageName);
        } catch (IOException e) {
            System.out.println(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the image data for a BufferedImage object.
     * @param image the BufferedImage object to get the data from.
     * @return a byte array containing the image data.
     */
    private byte[] getImageData(BufferedImage image) throws IOException {
        // Create a temporary file with prefix "image" and extension ".jpg"
        File tempFile = File.createTempFile("image", ".jpg");
        // Write the image data to the temporary file
        ImageIO.write(image, "jpg", tempFile);
        // Read the data from the temporary file into a byte array
        FileInputStream fileInputStream = new FileInputStream(tempFile);
        byte[] imageData = new byte[(int) tempFile.length()];
        fileInputStream.read(imageData);
        // Mark the temporary file to be deleted when the JVM exits
        tempFile.deleteOnExit();
        // Return the image data as a byte array
        return imageData;
    }


    /**
     * Gets the hash for a byte array of data as a string.
     * @param data the byte array of data to get the hash for.
     * @return a string containing the hash.
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available.
     */
    private String getHashAsString(byte[] data) throws NoSuchAlgorithmException {
        // Get an instance of the SHA-256 message digest algorithm
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        // Compute the hash for the data
        byte[] hash = digest.digest(data);
        // Convert the hash to a hexadecimal string
        StringBuilder hashString = new StringBuilder();
        for (byte b : hash) {
            hashString.append(String.format("%02x", b));
        }
        // Return the hexadecimal string representation of the hash
        return hashString.toString();
    }
}