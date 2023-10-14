package glavni.http;

import glavni.app.RequestHandler;
import glavni.http.response.Response;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.StringTokenizer;

public class ServerThread implements Runnable{

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    public ServerThread(Socket client) {
        this.client = client;

        try{
            in = new BufferedReader( new InputStreamReader( client.getInputStream()));
            out = new PrintWriter( new BufferedWriter( new OutputStreamWriter( client.getOutputStream())), true);
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
            String requestLine = in.readLine(); //prva linija zahteva -> drzi http metod i putanju koja se gadja
            StringTokenizer stringTokenizer = new StringTokenizer(requestLine);

            String httpMethod = stringTokenizer.nextToken();
            String path = stringTokenizer.nextToken();

            int contentLength= 0;
            System.out.println("\n HTTP ZAHTEV: \n");
            do{
                System.out.println(requestLine);
                requestLine = in.readLine();
                if (requestLine.contains("Content-Length")){
                    contentLength = Integer.parseInt(requestLine.split(": ")[1]);
                }
            } while (!requestLine.trim().equals(""));

            HashMap<String, String> postParameters = new HashMap<>();
            if (httpMethod.equals(HttpMethod.POST.toString())){
                char buff[] = new char[contentLength];
                in.read(buff);
                String params = new String(buff);
                String[] paramArr = params.split("&");
                for (String param: paramArr) {
                    System.out.println("Parameters: " + param);
                    postParameters.put(param.split("=")[0],
                            URLDecoder.decode(param.split("=")[1], StandardCharsets.UTF_8.name()));
                }
            }

            Request request = new Request(HttpMethod.valueOf(httpMethod), path, postParameters);
            System.out.println(request.getMethod() + request.getPath() + request.getPostParameters());
            RequestHandler requestHandler = new RequestHandler();
            Response response = requestHandler.handle(request);
            System.out.println("\nHTTP odgovor:\n");
            if (response != null)
                System.out.println(response.getResponseString());

            if (response != null)
                out.println(response.getResponseString());

            in.close();
            out.close();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
