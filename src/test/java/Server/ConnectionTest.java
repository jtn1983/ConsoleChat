package Server;

import org.junit.Test;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConnectionTest {
    @Test
    public void sendMsg() {
        Server server = mock(Server.class);
        ServerSocket serverSocket = mock(ServerSocket.class);
        Socket socket = mock(Socket.class);

        try {
            when(serverSocket.accept()).thenReturn(socket);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            when(socket.getOutputStream()).thenReturn(out);

            InputStream in = new ByteArrayInputStream("Hello".getBytes(StandardCharsets.UTF_8));
            when(socket.getInputStream()).thenReturn(in);

            when(socket.isClosed()).thenReturn(false);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        Connection connection = new Connection(socket, server);
        connection.start();

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg = reader.readLine();
            assertEquals("Hello", msg);
        }catch (IOException e) {
            fail(e.getMessage());
        }
    }
}