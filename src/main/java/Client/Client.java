package Client;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.Scanner;

public class Client {
    private final String SETUP_FILE_NAME = "setup-client.txt";
    private final String LOG_FILENAME = "client.log";
    public BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private FileWriter writer = new FileWriter(LOG_FILENAME, true);

    public Client() throws IOException {
        Scanner scanner = new Scanner(System.in);
        try{
            socket = new Socket();
            String[] setup = setupClientFromFile();
            socket.connect(new InetSocketAddress(setup[0], Integer.parseInt(setup[1])), 1000);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Введите никнейм");
            String name = scanner.nextLine();
            out.println(name);

            Sender sender = new Sender();
            sender.start();

            String str = "";
            while (true) {
                str = scanner.nextLine();
                out.println(str);
                if (str.equals("exit")) break;
                logMessage(name + "(" + new Date() + ")" + ": " + str);
            }
            sender.stopped();

        }catch (SocketTimeoutException e) {
            System.out.println("Сервер не отвечает. Проверьте настройки подключения");
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (socket != null) {
                close();
            }
        }
    }

    public String[] setupClientFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SETUP_FILE_NAME))) {
            String line = reader.readLine();
            return line.split(" ");
        }catch (FileNotFoundException e) {
                System.out.println("Файл настроек не найден");
        } catch (Exception e) {
            System.out.println("Ошибка конфигурационного файла");
        }
        System.exit(0);
        return null;
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Потоки не закрыты");
        }
    }

    public synchronized void logMessage(String msg) {
        try {
            writer.write(msg + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Sender extends Thread {

        public boolean stopped = false;

        public void stopped(){
            stopped = true;
        }

        @Override
        public void run(){
            try {
                while (!stopped){
                    String str = in.readLine();
                    System.out.println(str);
                }
            } catch (IOException e) {
                System.out.println("Ошибка получения сообщения");
                e.printStackTrace();
            }
        }
    }
}