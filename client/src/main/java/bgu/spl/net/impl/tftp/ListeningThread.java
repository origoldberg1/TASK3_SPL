package bgu.spl.net.impl.tftp;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;

import bgu.spl.net.api.MessageEncoderDecoder;

public class ListeningThread implements Runnable{

    private boolean terminate = false;
    private InputStream inputStream;
    private BlockingQueue<String> commandQueue;
    private MessageEncoderDecoder<byte[]> encdec;

    
    public ListeningThread(InputStream inputStream, BlockingQueue<String> commandQueue) {
        this.inputStream = inputStream;
        this.commandQueue = commandQueue;
        this.encdec = new TftpClientEncoderDecoder();
    }


    @Override
    public void run() {
        int read;
        try {
            while (!terminate && (read = inputStream.read()) >= 0) {
                byte[] nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    System.out.println("output bytes: (length =)" + nextMessage.length);
                    for (int i = 0; i < nextMessage.length; i++){
                        System.err.println(i + ": " + nextMessage[i]);
                    }
                    if(nextMessage[0] == 0 && nextMessage[1] == 4){
                        try {
                            commandQueue.put("ACK" +String.valueOf(Util.twoByteToInt(new byte[]{nextMessage[2], nextMessage[3]})));
                        } catch (InterruptedException e) {}
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
