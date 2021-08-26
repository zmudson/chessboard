package utils;

import javafx.scene.image.*;
import javafx.scene.paint.Color;

public class ImageGenerator {

    // return imageView with indicated part of an image
    public static ImageView getImagePart(String filename, double xPositionPercentage, double yPositionPercentage, double widthPercentage, double heightPercentage) {
        Image image = new Image(filename);
        double currentWidth = image.getWidth();
        double currentHeight = image.getHeight();
        int newWidth = (int)(currentWidth * widthPercentage);
        int newHeight = (int)(currentHeight * heightPercentage);
        int xDelta = (int)(currentWidth * xPositionPercentage);
        int yDelta = (int)(currentHeight * yPositionPercentage);

        // creating a writable image
        WritableImage writableImage = new WritableImage(newWidth, newHeight);

        // reading color from the loaded image
        PixelReader pixelReader = image.getPixelReader();

        // getting the pixel writer
        PixelWriter writer = writableImage.getPixelWriter();

        //r eading the color of the image pixel by pixel
        for(int y = 0; y < newHeight; y++) {
            for(int x = 0; x < newWidth; x++) {
                //retrieving the color of the pixel of the loaded image
                Color color = pixelReader.getColor(x + xDelta, y + yDelta);

                // setting the color to the writable image
                writer.setColor(x, y, color);
            }
        }
        // setting the view for the writable image
        return new ImageView(writableImage);
    }
}
