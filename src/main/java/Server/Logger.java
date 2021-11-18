package Server;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Logger {

    private String filename;

    public Logger(String filename) {
        this.filename = filename;
    }

    public void log(String msg) {
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(new Date() + " " + msg + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
