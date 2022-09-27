import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.Random;

public class Client {
    public static void main(String[] args) {

        try (Socket socket = new Socket("127.0.0.1", 8080);
             OutputStream outputStream = socket.getOutputStream()) {
            BufferedImage image = ImageIO.read(new File("resources/lilnicex.jpg"));
            BufferedImage noisedImage = addNoise(image);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            ImageIO.write(noisedImage, "jpeg", byteBuffer);
            outputStream.write(byteBuffer.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static BufferedImage addNoise(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        //Se convierte a Blanco y Negro
        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {

                Color c = new Color(originalImage.getRGB(j, i));
                int red = (int) (c.getRed() * 0.299);
                int green = (int) (c.getGreen() * 0.587);
                int blue = (int) (c.getBlue() * 0.114);
                int rgb = range(red + green + blue, 8);
                Color newColor = new Color(rgb, rgb, rgb);

                originalImage.setRGB(j, i, newColor.getRGB());
            }
        }
        return originalImage;
    }

    public static int range(int n, double prob) {
        double res = ((100 * prob) / 10);

        int[] array = new int[(int) res];
        array[0] = 1;
        array[1] = 255;

        for (int i = 2; i <= res - 2; i++) {
            array[i] = n;
        }
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

}
