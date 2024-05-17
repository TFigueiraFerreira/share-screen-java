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
        JFrame frame = new JFrame("Tela Remota"); // Cria uma janela para exibir a tela remota
        JLabel label = new JLabel(); // Cria um label para conter a imagem
        frame.add(label); // Adiciona o label à janela
        frame.setSize(1920, 1080); // Define o tamanho da janela
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define a operação de fechamento da janela
        frame.setVisible(true); // Torna a janela visível

        try (Socket socket = new Socket("127.0.0.1", 5000)) { // Conecta ao servidor na porta 5000
            InputStream inputStream = socket.getInputStream(); // Obtém o stream de entrada do socket
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(inputStream)); // Cria um DataInputStream para ler dados

            while (true) {
                int length = dataInputStream.readInt(); // Lê o tamanho da imagem
                if (length > 0) { // Verifica se o tamanho da imagem é positivo
                    byte[] imageBytes = new byte[length]; // Cria um array de bytes para armazenar a imagem
                    dataInputStream.readFully(imageBytes); // Lê a imagem em bytes

                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes); // Converte os bytes para um InputStream
                    BufferedImage image = ImageIO.read(byteArrayInputStream); // Converte o InputStream para um BufferedImage

                    if (image != null) { // Verifica se a imagem foi lida corretamente
                        label.setIcon(new ImageIcon(image)); // Define a imagem no label
                        frame.revalidate(); // Atualiza o layout da janela
                        frame.repaint(); // Repaint a janela para atualizar a imagem
                    }
                } else {
                    System.out.println("Tamanho da imagem inválido recebido: " + length); // Loga se um tamanho de imagem inválido for recebido
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Imprime qualquer exceção que ocorra
        }
    }
}


