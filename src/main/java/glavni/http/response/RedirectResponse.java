package glavni.http.response;

public class RedirectResponse extends Response{
    @Override
    public String getResponseString() {
        return "HTTP/1.1 303 OK\r\nContent-Type: text/html\r\nLocation: /quotes\r\n";
    }
}
