package server;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;

public class ScreenShareServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) { // Cria um servidor na porta 5000
            System.out.println("Server waiting for connection ");
            Socket socket = serverSocket.accept(); // Espera por uma conexão
            System.out.println("Client connected ");

            // Define a região de captura como 1920 x 1080 pixels
            Rectangle screenRect = new Rectangle(1920, 1080);

            Robot robot = new Robot(); // Cria um objeto Robot para capturar a tela
            OutputStream outputStream = socket.getOutputStream(); // Obtém a stream de saída do socket
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            long lastCaptureTime = System.nanoTime(); // Tempo da última captura

            int desiredFPS = 60;
            long desiredFrameTime = 1000 / desiredFPS;

            while (true) {
                long startTime = System.currentTimeMillis();

                // Captura a tela
                BufferedImage screenCapture = robot.createScreenCapture(screenRect);

                // Envia a imagem capturada para o cliente
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(screenCapture, "png", byteArrayOutputStream);

                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                dataOutputStream.writeInt(imageBytes.length); // Envia o tamanho da imagem para o cliente
                dataOutputStream.flush();

                outputStream.write(imageBytes); // Envia a imagem em bytes para o cliente
                outputStream.flush();

                // Mede o tempo decorrido desde a última captura
                long currentTime = System.nanoTime();
                long elapsedTime = currentTime - lastCaptureTime;
                System.out.println("Tempo decorrido desde lastCaptureTime: " + elapsedTime + " nanossegundos");



                // Calcula o tempo de espera até a próxima captura
                long waitTime = desiredFrameTime - elapsedTime;

                // Ajusta a próxima captura conforme necessário
                if (waitTime > 0) {
                    Thread.sleep(waitTime); // Converte nanossegundos para milissegundos
                }

                // Atualiza o tempo da última captura
                lastCaptureTime = System.nanoTime();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

