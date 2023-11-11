import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    // ip:message
    private static final Pattern MESSAGE_PATTERN = Pattern.compile("^(.+):(.+)");

    // ip:file=/path/to/file
    private static final Pattern FILEMESSAGE_PATTERN = Pattern.compile("^(.+):file=(.+)$");

    private static Message buildMessage(String sender, String text) throws FileNotFoundException, InvalidMessageFormatException {

        var matcher = Main.FILEMESSAGE_PATTERN.matcher(text);
        if (matcher.matches()) {
            var receiver = matcher.group(1);
            var filePath = matcher.group(2);

            var file = new File(filePath);
            Scanner scanner = new Scanner(file);
            ArrayList<String> lines = new ArrayList<>();

            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }

            scanner.close();

            return new Message(sender, receiver, file.getName(), lines);
        }

        matcher = Main.MESSAGE_PATTERN.matcher(text);
        if (matcher.matches()) {
            var receiver = matcher.group(1);
            var message = matcher.group(2);

            return  new Message(sender, receiver, message, new ArrayList<>());
        }

        throw new InvalidMessageFormatException();

    }

    public static void main(String[] args) throws UnknownHostException {

        var server = new UDPServer();
        server.start();

        var client = new UDPClient();
        var reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String input = "";
            try {
                input = reader.readLine();
            } catch (IOException e) {
                System.out.println("SYSTEM: Unable to read from System.IN, program will abort");
                System.exit(-1);
            }
            Message message = null;

            try {
                message = Main.buildMessage(InetAddress.getLocalHost().getHostAddress(), input);
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
                continue;
            } catch (InvalidMessageFormatException e) {
                System.out.println("Message must be in format ip:message or ip:file=filePath");
                continue;
            }

            InetAddress address = null;

            try {
                address = InetAddress.getByName(message.receiver);
            } catch (UnknownHostException e) {
                System.out.println("Error: Unknown Host");
                continue;
            }

            try {
                client.sendMessage(address, message);
            } catch (IOException e) {
                System.out.println("SYSTEM: Unable to convert message into a UDP packet");
                continue;
            }
        }
    }
}