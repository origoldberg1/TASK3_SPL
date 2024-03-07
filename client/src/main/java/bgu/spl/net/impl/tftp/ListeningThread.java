package bgu.spl.net.impl.tftp;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;

import bgu.spl.net.api.MessageEncoderDecoder;

public class ListeningThread implements Runnable{

    private boolean terminate = false;
    private InputStream inputStream;
    private MessageEncoderDecoder<byte[]> encdec;
    CurrentCommand currentCommand;

    
    public ListeningThread(InputStream inputStream, CurrentCommand currentCommand) {
        this.inputStream = inputStream;
        this.encdec = new TftpClientEncoderDecoder();
        this.currentCommand = currentCommand;
    }


    @Override
    public void run() {
        int read;
        try {
            while (!terminate && (read = inputStream.read()) >= 0) {
                byte[] nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) { 
                    //TODO: check what is this
                    System.out.println("output bytes: (length =)" + nextMessage.length);
                    for (int i = 0; i < nextMessage.length; i++){
                        System.err.println(i + ": " + nextMessage[i]);
                    }
                    if(Util.getOpcode(nextMessage) == 4 && currentCommand.getState().equals(STATE.Writing)){
                        if(!currentCommand.getSendData().sendPacket()){
                            currentCommand.setState(STATE.Unoccupied);
                        }
                    }
                        System.out.println("lisening thread: receiving ack number " + Util.twoByteToInt(new byte[]{nextMessage[2], nextMessage[3]}));

                    }
                }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}

