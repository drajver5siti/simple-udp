import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws IOException {

        (new UDPServer()).start();

        var client = new UDPClient();
        var reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String message = reader.readLine();
            var parts = message.split(":");

            if (parts.length != 2) {
                System.out.println("Message must be in format ip:message");
                continue;
            }

            InetAddress address = null;

            try {
                address = InetAddress.getByName(parts[0]);
            } catch (UnknownHostException e) {
                System.out.println("Error: Unknown Host");
                continue;
            }

            var messageContent = parts[1];
            client.sendMessage(address, messageContent);
        }
    }
}