package Server;


public class App {
    public static void main(String[] args) {
        Server server = new Server(2288);
        while(true) {
            server.run();
        }
    }
}
