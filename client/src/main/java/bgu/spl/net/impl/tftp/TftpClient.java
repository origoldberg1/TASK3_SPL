package bgu.spl.net.impl.tftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TftpClient {
    public final static Object waitOnObject = new Object();

    //TODO: implement the main logic of the client, when using a thread per client the main logic goes here
    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            args = new String[]{"localhost"};
        }

        //BufferedReader and BufferedWriter automatically using UTF-8 encoding
        Socket sock = new Socket(args[0], 7777);
        InputStream in = sock.getInputStream();
        OutputStream out = sock.getOutputStream();
        CurrentCommand currentCommand = new CurrentCommand();
        currentCommand.setState(STATE.Unoccupied);

        final CommandParser commandParser = new CommandParser();
        final KeyBoard keyBoradThread = new KeyBoard(commandParser, out, currentCommand);
        final Listening listeningThread = new Listening(in, currentCommand, out);

        new Thread(keyBoradThread).start();
        new Thread(listeningThread).start();

    }

}
