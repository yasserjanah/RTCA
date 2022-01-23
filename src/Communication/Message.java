package Communication;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private String destination;
    private String source;
    private String message;
    private boolean broadCast;
    private String ip;
    private LocalDateTime dateTime;

    public Message(String destination, String source, String message, boolean broadCast) {
        this.destination = destination;
        this.source = source;
        this.message = message;
        this.broadCast = broadCast;
        dateTime = LocalDateTime.now();
    }

    public Message(String destination, String source, String message, boolean broadCast, String ip) {
        this.destination = destination;
        this.source = source;
        this.message = message;
        this.broadCast = broadCast;
        this.ip = ip;
        dateTime = LocalDateTime.now();
    }

    public String getDestination() {
        return destination;
    }

    public String getSource() {
        return source;
    }

    public String getMessage() {
        return message;
    }

    public boolean isBroadCast() {
        return broadCast;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getIp() {
        return ip;
    }

    @Override
    public String toString() {
        return " {" +
                " destination='" + destination + '\'' +
                ", source='" + source + '\'' +
                ", message='" + message + '\'' +
                ", broadCast=" + broadCast +
                ", ip='" + ip + '\'' +
                ", dateTime=" + dateTime +
                " }";
    }
}
