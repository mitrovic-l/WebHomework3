package glavni.http;

import java.util.HashMap;

public class Request {
    private final HttpMethod method;
    private final String path;
    private HashMap<String, String> postParameters = new HashMap<>();

    public Request(HttpMethod method, String path, HashMap<String, String> postParameters) {
        this.method = method;
        this.path = path;
        this.postParameters = postParameters;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public HashMap<String, String> getPostParameters() {
        return postParameters;
    }

    public void setPostParameters(HashMap<String, String> postParameters) {
        this.postParameters = postParameters;
    }
}
