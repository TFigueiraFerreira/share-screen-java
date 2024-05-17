package client;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;

public class ScreenShareClient {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tela Remota");
        JLabel label = new JLabel();
        frame.add(label);
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        try (Socket socket = new Socket("127.0.0.1", 5000)) {
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));

            while (true) {
                int length = dataInputStream.readInt();
                if (length > 0) {
                    byte[] imageBytes = new byte[length];
                    dataInputStream.readFully(imageBytes);

                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
                    BufferedImage image = ImageIO.read(byteArrayInputStream);
                    if (image != null) {
                        SwingUtilities.invokeLater(() -> {
                            label.setIcon(new ImageIcon(image));
                            frame.revalidate();
                            frame.repaint();
                        });
                    }
                } else {
                    System.out.println("Tamanho da imagem inválido recebido: " + length);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

