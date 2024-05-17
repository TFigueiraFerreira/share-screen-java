package app;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.ExportException;
import javax.imageio.ImageIO;

public class Main {
    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(5000)){ //Cria um servidor na porta 5000
            System.out.println("Server waiting for connection ");
            Socket socket = serverSocket.accept(); //Espera por uma conexão
            System.out.println("Client connected ");

            Robot robot = new Robot(); //Cria um objeto Robot para screen share
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()); //Define o tamanho da tela
            OutputStream outputStream = socket.getOutputStream(); //Obter a stream de saída do socket
        }
        catch (Exception exception){
            exception.printStackTrace();
    }

    }
}