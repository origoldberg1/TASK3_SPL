package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.VolatileCallSite;
import java.net.Socket;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private final MessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Socket sock;
    private final Connections<T> connections;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    static volatile int connectionCounter = 0;
    private volatile int id;
    private volatile String userName;
    private volatile String fileToWritePath;

    public BlockingConnectionHandler(Socket sock, Connections<T> connections, MessageEncoderDecoder<T> reader, MessagingProtocol<T> protocol) {
        this.sock = sock;
        this.connections = connections;
        this.encdec = reader;
        this.protocol = protocol;
        this.id = connectionCounter++;
        this.userName = null;
        this.fileToWritePath = null;
    }

    @Override
    public void run() {
        try (Socket sock = this.sock) { //just for automatic closing
            this.protocol.start(connectionCounter, connections, this); 
            int read;

            in = new BufferedInputStream(sock.getInputStream());

            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                T nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    T response = protocol.process(nextMessage);
                    if (response != null) {
                        out.write(encdec.encode(response));
                        out.flush();
                    }
                }
            }

        } catch (IOException ex) { 
            ex.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
    }

    @Override
    public void send(T msg) {
        try {
            OutputStream out = sock.getOutputStream();
            out.write(encdec.encode(msg));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Connections<T> getConnections(){ //we add this method
        return connections;
    }

    public int id(){ //we add this method
        return id;
    }

    public String getName() {//we add this method
        return this.userName;
    }

    public String setName(String userName){ //we add this method
        return this.userName=userName;
    }

    public int getId(){ //we add this method
        return this.id;
    }

    public String getFileToWritePath(){ //we add this method
        return fileToWritePath;
    }
    
    public void setFileToWritePath(String fileToWritePath){
        this.fileToWritePath=fileToWritePath;
    }
}
