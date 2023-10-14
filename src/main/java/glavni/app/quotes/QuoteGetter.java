package glavni.app.quotes;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class QuoteGetter {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Gson gson = new Gson();

    public Quote getQuoteFromServerHelper(){
        try {
            socket = new Socket("localhost", 8181);
            in = new BufferedReader( new InputStreamReader( socket.getInputStream()));
            out = new PrintWriter( new OutputStreamWriter( socket.getOutputStream()), true);

            StringBuilder quoteReq = new StringBuilder();
            quoteReq.append("GET / HTTP/1.1\r\nHost: localhost:8181\r\n");
            out.println(quoteReq.toString());

            String line = in.readLine();
            do {
                System.out.println(line);
                line = in.readLine();
            } while (!line.trim().equals(""));

            String quoteJSON = in.readLine();
            System.out.println("QUOTE: " + quoteJSON);

            Quote quote = gson.fromJson(quoteJSON, Quote.class);

            socket.close();
            in.close();
            out.close();

            return quote;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
