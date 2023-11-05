import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer extends Thread{

    private DatagramSocket socket;
    private byte[] buffer;

    public UDPServer() {
        try {
            this.buffer = new byte[256];
            this.socket = new DatagramSocket(9753);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {

        while (true) {
            DatagramPacket newPacket = new DatagramPacket(this.buffer, this.buffer.length);
            try {
                this.socket.receive(newPacket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String receivedMessage = new String(newPacket.getData(), 0, newPacket.getLength());
            System.out.printf(
                    "Message from %s: %s\n",
                    newPacket.getAddress().getHostAddress(),
                    receivedMessage.trim()
            );
        }
    }
}
