import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

public class UDPClient {

    private DatagramSocket socket;

    public UDPClient() {
        try {
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(InetAddress ipAddress, Message message) throws IOException {
        byte[] buffer = this.messageToBytes(message);
        DatagramPacket newPacket = new DatagramPacket(buffer, buffer.length, ipAddress, 9753);
        this.socket.send(newPacket);
    }

    private byte[] messageToBytes(Message message) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(message);
        out.flush();

        return bos.toByteArray();
    }
}
