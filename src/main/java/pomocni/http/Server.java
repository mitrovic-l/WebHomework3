package pomocni.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int PORT = 8181;
    public static void main(String[] args) {
        try{
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true){
                Socket socket = serverSocket.accept();
                new Thread( new ServerThread(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
