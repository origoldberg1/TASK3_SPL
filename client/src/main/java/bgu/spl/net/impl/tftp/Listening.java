package bgu.spl.net.impl.tftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

import bgu.spl.net.api.MessageEncoderDecoder;

public class Listening implements Runnable{

    private boolean terminate = false;
    private InputStream inputStream;
    private OutputStream outputStream;
    private MessageEncoderDecoder<byte[]> encdec;
    CurrentCommand currentCommand;
    Keyboard keyboard;
    Object waitOnObject;
    final int DATA = 3;
    final int ACK = 4;
    final int ERROR = 5;
    final int BCAST = 9;

    
    public Listening(InputStream inputStream, CurrentCommand currentCommand, OutputStream outputStream, Keyboard keyboard, Object waitOnObject) {
        this.inputStream = inputStream;
        this.encdec = new TftpClientEncoderDecoder();
        this.currentCommand = currentCommand;
        this.outputStream = outputStream;
        this.keyboard = keyboard;
        this.waitOnObject = waitOnObject;
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
                            new ACK(outputStream, nextMessage).execute();;
                            if(!currentCommand.getReceiveData().processPacket(nextMessage)){
                                System.out.println("RRQ "+ currentCommand.getReceiveData().getFileName() + " complete");
                                currentCommand.resetFields();
                            } else if(currentCommand.getState().equals(STATE.DIRQ)){
                                new ACK(outputStream, nextMessage).execute();;
                                if(!currentCommand.getReceiveData().processPacket(nextMessage)){
                                    currentCommand.resetFields();
                                }
                            }

                        }
                        else if(currentCommand.getState().equals(STATE.DIRQ)){}

                        //TODO: handle case state != RRQ/DIRQ

                    } else if(opcode == ACK){
                        System.out.println("ACK " + Util.twoByteToInt(new byte[]{nextMessage[2], nextMessage[3]}));
                        if(currentCommand.getState().equals(STATE.WRQ)){
                            if(!currentCommand.getSendData().sendPacket()){
                                System.out.println("WRQ "+ currentCommand.getSendData().getFileName() + " complete");
                                currentCommand.resetFields();
                            }
                            currentCommand.resetFields();
                        } else{
                            if(currentCommand.getState().equals(STATE.DISC)){
                                synchronized(keyboard){
                                    keyboard.terminate = true;
                                } 
                            }
                            else if(currentCommand.getState().equals(STATE.LOGRQ)){
                                synchronized(keyboard){
                                    keyboard.loggedIn = true;
                                }
                            }
                        }
                    } else if(opcode == BCAST){
                        System.out.println("BCAST " + nextMessage[2] + " " + Util.extractString(nextMessage, 3));
                        currentCommand.resetFields();
                    }
                    else if(opcode == ERROR){
                        System.out.println("ERROR " + Util.twoByteToInt(new byte[]{nextMessage[2], nextMessage[3]}) + Util.extractString(nextMessage, 4));
                        if(currentCommand.getState().equals(STATE.RRQ)){
                            currentCommand.deleteRRQFile();
                        }
                        currentCommand.resetFields();
                    }
                    
                    if(currentCommand.getState().equals(STATE.Unoccupied)){
                        synchronized(waitOnObject){
                            waitOnObject.notifyAll();
                        }
                    }
                }
            }
        } catch (IOException e) {}
    }
    


}

