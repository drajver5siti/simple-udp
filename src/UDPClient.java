import java.io.IOException;
import java.net.*;

public class UDPClient {

    private DatagramSocket socket;
    private byte[] buffer;

    public UDPClient() {
        try {
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(InetAddress ipAddress, String message) {
        this.buffer = message.getBytes();
        DatagramPacket newPacket = new DatagramPacket(this.buffer, this.buffer.length, ipAddress, 9753);
        try {
            this.socket.send(newPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
