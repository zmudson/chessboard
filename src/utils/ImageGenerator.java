package utils;

import javafx.scene.image.*;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageGenerator {
    public static ImageView getImagePart(String filename, double xPositionPercentage, double yPositionPercentage, double widthPercentage, double heightPercentage) {
        Image image = new Image(filename);
        double currentWidth = image.getWidth();
        double currentHeight = image.getHeight();
        int newWidth = (int)(currentWidth * widthPercentage);
        int newHeight = (int)(currentHeight * heightPercentage);
        int xDelta = (int)(currentWidth * xPositionPercentage);
        int yDelta = (int)(currentHeight * yPositionPercentage);
        //Creating a writable image
        WritableImage writableImage = new WritableImage(newWidth, newHeight);

        //Reading color from the loaded image
        PixelReader pixelReader = image.getPixelReader();

        //getting the pixel writer
        PixelWriter writer = writableImage.getPixelWriter();

        //Reading the color of the image
        for(int y = 0; y < newHeight; y++) {
            for(int x = 0; x < newWidth; x++) {
                //Retrieving the color of the pixel of the loaded image
                Color color = pixelReader.getColor(x + xDelta, y + yDelta);

                //Setting the color to the writable image
                writer.setColor(x, y, color);
            }
        }
        //Setting the view for the writable image
        return new ImageView(writableImage);
    }
}
