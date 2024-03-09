package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.tftp.TftpConnections;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
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
    private volatile int id; //we add this
    private volatile String userName; //we add this
    // private volatile String fileToWritePath; //we add it

    public BlockingConnectionHandler(Socket sock, Connections<T> connections, MessageEncoderDecoder<T> reader, MessagingProtocol<T> protocol) {
        this.sock = sock;
        this.connections = connections;
        this.encdec = reader;
        this.protocol = protocol;
        this.id=connectionCounter; //make sure with ori it is ok to increase connectionCounter just in run method
        this.userName=null; //we add it
        // this.fileToWritePath=null; //we add it
        this.connections.connect(connectionCounter, this);

    }

    @Override
    public void run() {
        try (Socket sock = this.sock) {
            this.protocol.start(connectionCounter, connections, this); //just for automatic closing
            connectionCounter ++;
            int read;

            in = new BufferedInputStream(sock.getInputStream());
            // out = new BufferedOutputStream(sock.getOutputStream());

            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                T nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) { //full packet acccepted
                    protocol.process(nextMessage);
                    // if (response != null) { //means we got al packet from encdec?
                    //     out.write(encdec.encode(response));
                    //     out.flush();
                    // }
                }
            }
            close();
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
    public synchronized void send(T msg){ //implement if needed
        try{
            out = new BufferedOutputStream(sock.getOutputStream());
            byte[] msgEncoded = encdec.encode(msg);
            out.write(msgEncoded);
            out.flush();
        }catch(IOException e){}
    }

    public Connections<T> getConnectionsObject(){ //we add this method
        return connections;
    }

    public int id(){ //we add this method
        return id;
    }

    public String getName(){ //we add this method
        return this.userName;
    }

    public String setName(String userName){ //we add this method
        return this.userName=userName;
    }

    public int getId(){ //we add this method
        return this.id;
    }

    // public String getFileToWritePath(){ //we add this method
    //     return fileToWritePath;
    // }
    
    // public synchronized void setFileToWritePath(String fileToWritePath){
    //    this.fileToWritePath=fileToWritePath;
    // }

    public MessagingProtocol<T> getProtocol() //we add that
    {
        return protocol;
    }
}
