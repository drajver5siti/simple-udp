import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {

    public String sender;
    public String receiver;
    public String messageText;
    public ArrayList<String> fileContents;

    public Message () {
    }

    public Message (String sender, String receiver, String messageText, ArrayList<String> fileContents) {
        this.sender = sender;
        this.receiver = receiver;
        this.messageText = messageText;
        this.fileContents = fileContents;
    }
}
