package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    private static final String SETUP_FILE_NAME = "setup-server.txt";
    private static final String LOG_FILE_NAME = "server.log";
    private List<Connection> connections = Collections.synchronizedList(new ArrayList<Connection>());
    private Logger logger = new Logger(LOG_FILE_NAME);

    public Server() {
        try (final ServerSocket serverSocket = new ServerSocket(portFromSetupFile(SETUP_FILE_NAME))) {
            while (true) {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket, this);
                connections.add(connection);
                connection.start();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка запуска сервера. Проверьте номер порта!");
        } catch (IOException e) {
            System.out.println("Ошибка запуска сервера");
        } finally {
            closeAll();
        }
    }

    public synchronized void log(String msg) {
        logger.log(msg);
    }

    public synchronized void sendMessageAll(String msg) {
        for (Connection connection : connections) {
            connection.sendMsg(msg);
        }
        log(msg);
    }

    public synchronized void closeAll() {
        try {
            for (Connection connection : connections) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized int countUsers() {
        return connections.size();
    }

    public synchronized void delUser(Connection con) {
        connections.remove(con);
    }

    public int portFromSetupFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            return Integer.parseInt(line);
        } catch (FileNotFoundException e) {
            System.out.println("Конфигурационный файл не найден!");
        } catch (Exception e) {
            System.out.println("Ошибка конфигурационного файла");
        }
        System.exit(0);
        return 0;
    }
}
