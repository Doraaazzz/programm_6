package Server;

import Commands.AbstractCommand;
import Commands.Save;
import Utility.CollectionManager;
import Utility.Deserializer;
import Utility.Serializer;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class Server {
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;
    private int port;
    private InputStream stream;
    private Serializer serializer;
    private int portForAns;
    private InetAddress hostForAns;
    private Deserializer deserializer;

    public Server(int p) {
        this.port = p;
        boolean success = false;
        while (!success) {
            try {
                datagramSocket = new DatagramSocket(port);
                success = true;
                System.out.println("Сервер поднят и доступен по порту " + port + " .");
            } catch (SocketException e) {
                port = (int) (Math.random() * 20000 + 10000);
            }
        }
        stream = System.in;
        Module.setCollectionManager(new CollectionManager());
        serializer = new Serializer();
        deserializer = Deserializer.get();
    }

    private void test(){
        try {
            String zapros = (String) getObject();
            System.out.println(zapros);
            sendObject("complete!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            AbstractCommand command = null;
            while (command == null) {
                try {
                    command = (AbstractCommand) getObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            boolean result = Module.running(command);
            if (result) {
                Module.addMessage("Выполнение успешно.");
            } else {
                Module.addMessage("Выполнить команду не удалось.");
            }
            sendObject(Module.messageFlush());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (stream.available() > 0) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                if (reader.readLine().equals("save")) {
                    Save save = new Save("save", "desc");
                    save.setCollectionManager(Module.getCollectionManager());
                    save.exec();
                    System.out.println("Коллекция сохранена.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Object getObject() throws IOException {
        datagramPacket = new DatagramPacket(new byte[10*1024],10*1024);
        datagramSocket.receive(datagramPacket);
        hostForAns = datagramPacket.getAddress();
        portForAns = datagramPacket.getPort();
        return deserializer.deSerialization(ByteBuffer.wrap(datagramPacket.getData()));
    }

    private void sendObject(Serializable o) throws IOException {
        ByteBuffer buf = serializer.serialize(o);
        datagramPacket = new DatagramPacket(buf.array(), buf.array().length, hostForAns, portForAns);
        datagramSocket.send(datagramPacket);
    }
}
