package Client;

import Commands.Connect;
import Utility.Deserializer;
import Utility.Serializer;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Client {

    private InetSocketAddress addr;
    private DatagramChannel datagramChannel;
    private Deserializer deserializer;
    private Serializer serializer;

    public Client(int p) {
        addr = new InetSocketAddress("localhost", p);
        deserializer = Deserializer.get();
        serializer = new Serializer();
        try {
            datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(false);
            System.out.println(run(new Connect("connect", "desc")));
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    public String run(Serializable o1) {
        String out = "";
        try {
            open();
            sendObject(o1);
            out = (String) getObject();
            close();
        } catch (IOException e) {
            return "Отсутствует связь с сервером.";
        }
        return out;
    }

    private void sendObject(Serializable object) throws IOException {
        datagramChannel.send(serializer.serialize(object), addr);
    }

    private Object getObject() throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(10 * 1024);
        WaitAnswer answer = new WaitAnswer();
        answer.waitReceive(datagramChannel, buf);
        Object o = deserializer.deSerialization(buf);
        return o;
    }

    private void close() throws IOException {
        datagramChannel.close();
    }

    private void open() throws IOException {
        datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);
    }

    //    private void findServer() throws Disconnect {
//        System.out.println("Подключаюсь к серверу...");
//        String result = run(new Connect("connect","подключение к серверу."));
//        if(!(result.equals("Выполнение успешно.\n"))) {
//            throw new Disconnect("Подключение не установлено");
//        }
//        System.out.println(result);
//    }
    class WaitAnswer {

        public boolean waitReceive(DatagramChannel channel, ByteBuffer byteBuffer, long timeout) throws IOException {
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start < (timeout * 1000)) {
                SocketAddress address = channel.receive(byteBuffer);
                if (address != null) {
                    return true;
                }
            }
            throw new IOException();
        }

        public boolean waitReceive(DatagramChannel channel, ByteBuffer byteBuffer) throws IOException {
            return waitReceive(channel, byteBuffer, 5);
        }


    }
}