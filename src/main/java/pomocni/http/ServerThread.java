package pomocni.http;

import com.google.gson.Gson;
import glavni.http.HttpMethod;
import pomocni.storage.QuoteStorage;

import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.StringTokenizer;

public class ServerThread implements Runnable{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Gson gson = new Gson();

    public ServerThread(Socket socket) {
        try{
            this.socket = socket;
            in = new BufferedReader( new InputStreamReader( socket.getInputStream()));
            out = new PrintWriter( new OutputStreamWriter( socket.getOutputStream()), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            String request = in.readLine();
            StringTokenizer tokenizer = new StringTokenizer(request);

            String method = tokenizer.nextToken();
            String path = tokenizer.nextToken();
            //System.out.println("METOD: " + method + ", PUTANJA: " + path +".");

            System.out.println("\n HTTP ZAHTEV KLIJENTA: \n");
            int cnt = 0;
            do{
                System.out.println(request);
                System.out.println(cnt++);
                request = in.readLine();
            } while (!request.trim().equals(""));
            //System.out.println("Izaso iz do while");
            Random helper = new Random();
            if (method.equals(HttpMethod.GET.toString()) && path.equals("/")){
                //System.out.println("USO U IF ZA GET / ");
                StringBuilder quoteForToday = new StringBuilder();
                int random = helper.nextInt(16); //velicina liste u quoteStorage je 16
                quoteForToday.append("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n");
                quoteForToday.append(gson.toJson(QuoteStorage.getInstance().getQuotes().get(random)));

                System.out.println("\n QUOTE FOR TODAY: \n");
                System.out.println(quoteForToday.toString());

                out.println(quoteForToday.toString());
            } else {
                //System.out.println("Uso u else i salje 404");
                StringBuilder response = new StringBuilder();
                response.append("HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\n");
                out.println(response.toString());
            }

            in.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
