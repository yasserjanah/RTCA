package Communication;

import java.io.Serializable;

public class Response implements Serializable {

    private String type;
    private String content;

    public Response(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public Response(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
