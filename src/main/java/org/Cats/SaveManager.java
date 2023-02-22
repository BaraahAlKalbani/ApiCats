package org.Cats;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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
            String imageUrl = catImages[0].getUrl();
            BufferedImage image = ImageIO.read(new URL(imageUrl));
            byte[] imageData = getImageData(image);
            byte[] hash = getHash(imageData);
            String imageName = hash.toString() + ".jpg";
            File imageFile = new File(dataDirectory, imageName);
            // check if a cat image with the same name already exists
            if (imageFile.exists()) {
                System.out.println("A cat image with the same name already exists: " + imageName);
                return;
            }
            ImageIO.write(image, "jpg", imageFile);
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
        File tempFile = File.createTempFile("image", ".jpg");
        ImageIO.write(image, "jpg", tempFile);
        FileInputStream fileInputStream = new FileInputStream(tempFile);
        byte[] imageData = new byte[(int) tempFile.length()];
        fileInputStream.read(imageData);
        tempFile.deleteOnExit();
        return imageData;
    }

    /**
     * Gets the hash for a byte array of data.
     * @param data the byte array of data to get the hash for.
     * @return a byte array containing the hash.
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available.
     */
    private byte[] getHash(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(data);
    }
}
