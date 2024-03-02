package bgu.spl.net.impl.tftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TftpClient {
    //TODO: implement the main logic of the client, when using a thread per client the main logic goes here
    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            args = new String[]{"localhost", "login"};
        }

        if (args.length < 2) {
            System.out.println("you must supply two arguments: host, message");
            System.exit(1);
        }

        //BufferedReader and BufferedWriter automatically using UTF-8 encoding
        try (Socket sock = new Socket(args[0], 7777)) {
                InputStream in = sock.getInputStream();
                OutputStream out = sock.getOutputStream();

            System.out.println("sending message to server");
            byte[] msg = new byte[] {
                0,
                7,
                'o',
                'r',
                'i',
                0
            };
            out.write(msg);
            out.flush();

            System.out.println("awaiting response");
            for (int i = 0; i < 4; i++) {
                int x = in.read();
                System.out.println(x);
            }
            // byte[] response = in.readAllBytes();
            // System.out.println("message from server: " + response);
        }
    }

}
