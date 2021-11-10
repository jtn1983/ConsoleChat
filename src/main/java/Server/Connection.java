package Server;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class Connection extends Thread {
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private Server server;
    private String name = "";

    public Connection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            name = in.readLine();
            server.sendMessageAll(name + " присоединился");
            server.sendMessageAll("Сейчас в чате: " + server.countUsers() + " пользователя");

            String str = "";
            while (true) {

                str = in.readLine();
                if (str == null || str.equals("exit")) break;
                server.sendMessageAll(name + ": " + str);
                server.logMessage(name + "(" + new Date() + ")" + ": " + str + "\n");
            }

            server.sendMessageAll(name + " вышел из чата");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.println(new Date() + ": " + msg);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
            server.delUser(this);

        } catch (Exception e) {
            System.err.println("Потоки не закрылись!");
        }
    }
}
