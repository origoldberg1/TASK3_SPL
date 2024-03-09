package bgu.spl.net.impl.tftp;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;

import bgu.spl.net.api.MessageEncoderDecoder;

public class Listening implements Runnable{

    private boolean terminate = false;
    private InputStream inputStream;
    private MessageEncoderDecoder<byte[]> encdec;
    CurrentCommand currentCommand;
    final int DATA = 3;
    final int ACK = 4;
    final int ERROR = 5;
    final int BCAST = 9;

    
    public Listening(InputStream inputStream, CurrentCommand currentCommand) {
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
                    //TODO: remove before submitting
                    System.out.println("output bytes: (length =)" + nextMessage.length);
                    for (int i = 0; i < nextMessage.length; i++){
                        System.err.println(i + ": " + nextMessage[i]);
                    }
                    int opcode = Util.getOpcode(nextMessage);
                    if(opcode == DATA){
                        if(currentCommand.getState().equals(STATE.RRQ)){
                            if(!currentCommand.getReceiveData().processPacket(nextMessage)){
                                
                                currentCommand.setState(STATE.Unoccupied);
                                //TODO: reset all fields of currentCommand

                            }
                        }
                        //TODO: handle case state != RRQ
                    } else if(opcode == ACK){
                        System.out.println("ACK " + Util.twoByteToInt(new byte[]{nextMessage[2], nextMessage[3]}));
                        if(currentCommand.getState().equals(STATE.WRQ)){
                            if(!currentCommand.getSendData().sendPacket()){
                                currentCommand.setState(STATE.Unoccupied);
                                //TODO: reset all fields of currentCommand
                            }
                            //TODO: handle case state != WRQ

                        }
                    } else if(opcode == BCAST){
                        System.out.println("BCAST " + nextMessage[2] + " " + Util.extractString(nextMessage, 3));
                    }
                    else if(opcode == ERROR){
                        System.out.println("ERROR " + Util.twoByteToInt(new byte[]{nextMessage[2], nextMessage[3]}) + Util.extractString(nextMessage, 4));
                    }
                }
            }
        } catch (IOException e) {}
    }
    
}

