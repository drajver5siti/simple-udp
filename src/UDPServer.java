import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer extends Thread{

    private DatagramSocket socket;
    private byte[] buffer;

    public UDPServer() {
        try {
            this.buffer = new byte[1024];
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

            try {
                var message = this.packetToMessage(newPacket);

                if (message.fileContents.isEmpty()) {
                    System.out.printf(
                            "Message from %s: %s\n",
//                            message.sender,
                            newPacket.getAddress().getHostAddress(),
                            message.messageText
                    );
                } else {
                    System.out.printf(
                            "File message from %s, file name is %s\nFile content:\n%s\n",
//                            message.sender,
                            newPacket.getAddress().getHostAddress(),
                            message.messageText,
                            message.fileContents
                                    .stream()
                                    .reduce((a, b) -> a + "\n" + b)
                                    .get()
                    );
                }

            } catch (IOException e) {
                System.out.println("SYSTEM: Unable to convert UDP packet into a Message");
                continue;
            }
        }
    }

    private Message packetToMessage(DatagramPacket packet) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData());
        ObjectInputStream input = new ObjectInputStream(bis);

        try {
            return (Message) input.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException();
        } finally {
            bis.close();
            input.close();
        }
    }
}
