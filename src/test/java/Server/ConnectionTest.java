package Server;

import org.junit.Test;

import javax.net.SocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConnectionTest {



    @Test
    public void sendMsg() throws IOException {

        Socket socket = mock(Socket.class);

        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));



    }

}