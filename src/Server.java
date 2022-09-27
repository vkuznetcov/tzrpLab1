import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {
    public static void main(String[] args) {
        System.out.println("server started");
        try(ServerSocket serverSocket = new ServerSocket(8080);
            Socket clientSocket = serverSocket.accept();
            InputStream inputStream = clientSocket.getInputStream()){
            byte[] imageBytes = inputStream.readAllBytes();
            BufferedImage noisedImage = readFromBytes(imageBytes);
            ImageIO.write(noisedImage, "jpg", new File(
                    "C:\\Users\\vkuzn\\Desktop\\tzrpLab1\\lilnicenoised.jpg"));
            BufferedImage filteredImage = filterImage(noisedImage);
            ImageIO.write(filteredImage, "jpg", new File(
                    "C:\\Users\\vkuzn\\Desktop\\tzrpLab1\\lilnicefiltered.jpg"));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static BufferedImage readFromBytes(byte[] imageBytes) throws IOException {
        ByteArrayInputStream byteArrayBuffer= new ByteArrayInputStream(imageBytes);
        return ImageIO.read(byteArrayBuffer);
    }

    private static BufferedImage filterImage(BufferedImage noisedImage){
        Color[] pixel=new Color[9];
        int[] R = new int[9];
        int[] B = new int[9];
        int[] G = new int[9];

        for(int i=1;i<noisedImage.getWidth()-1;i++)
            for(int j=1;j<noisedImage.getHeight()-1;j++)
            {
                pixel[0]=new Color(noisedImage.getRGB(i-1,j-1));
                pixel[1]=new Color(noisedImage.getRGB(i-1,j));
                pixel[2]=new Color(noisedImage.getRGB(i-1,j+1));
                pixel[3]=new Color(noisedImage.getRGB(i,j+1));
                pixel[4]=new Color(noisedImage.getRGB(i+1,j+1));
                pixel[5]=new Color(noisedImage.getRGB(i+1,j));
                pixel[6]=new Color(noisedImage.getRGB(i+1,j-1));
                pixel[7]=new Color(noisedImage.getRGB(i,j-1));
                pixel[8]=new Color(noisedImage.getRGB(i,j));
                for(int k=0;k<9;k++){
                    R[k]=pixel[k].getRed();
                    B[k]=pixel[k].getBlue();
                    G[k]=pixel[k].getGreen();
                }
                Arrays.sort(R);
                Arrays.sort(G);
                Arrays.sort(B);
                noisedImage.setRGB(i,j,new Color(R[4],B[4],G[4]).getRGB());
            }
        return noisedImage;
    }
}
