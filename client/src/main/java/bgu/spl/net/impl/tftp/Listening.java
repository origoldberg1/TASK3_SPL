package bgu.spl.net.impl.tftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import bgu.spl.net.api.MessageEncoderDecoder;

public class Listening implements Runnable{

    private boolean terminate = false;
    private InputStream inputStream;
    private OutputStream outputStream;
    private MessageEncoderDecoder<byte[]> encdec;
    CurrentCommand currentCommand;
    Keyboard keyboard;
    Object waitOnObject;


    private Socket sock;
    final int DATA = 3;
    final int ACK = 4;
    final int ERROR = 5;
    final int BCAST = 9;

    
    public Listening(InputStream inputStream, CurrentCommand currentCommand, OutputStream outputStream, Keyboard keyboard, Object waitOnObject, Socket sock) {
        this.inputStream = inputStream;
        this.encdec = new TftpClientEncoderDecoder();
        this.currentCommand = currentCommand;
        this.outputStream = outputStream;
        this.keyboard = keyboard;
        this.waitOnObject = waitOnObject;
        this.sock = sock;
    }

    
    @Override
    public void run(){
        int read;
        try {
            while (!terminate && (read = inputStream.read()) >= 0) {
                byte[] nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) { 
                    //TODO: remove before submitting
                    // System.out.println("output bytes: (length =)" + nextMessage.length);
                    // for (int i = 0; i < nextMessage.length; i++){
                    //     System.err.println(i + ": " + nextMessage[i]);
                    // }
                    int opcode = Util.getOpcode(nextMessage);
                    STATE curState = currentCommand.getState();
                    if (curState.equals(STATE.RRQ)){
                        handleRRQ(nextMessage, opcode);
                    } 
                    else if(curState.equals(STATE.WRQ)){
                        handleWRQ(nextMessage, opcode);
                    }
                    else if(curState.equals(STATE.DIRQ)){
                        handleDIRQ(nextMessage, opcode);
                    }
                    else if(curState.equals(STATE.LOGRQ)){
                        handleLOGRQ(nextMessage, opcode);
                    }
                    else if(curState.equals(STATE.DELRQ)){
                        handleDELRQ(nextMessage, opcode);
                    } 
                    else if(curState.equals(STATE.DISC)){
                        handleDISC(nextMessage, opcode);
                    }
                    
                    if(opcode == BCAST){
                        handleBCAST(nextMessage);
                    }

                    if(!terminate && currentCommand.getState().equals(STATE.Unoccupied)){
                        synchronized(waitOnObject){
                            waitOnObject.notifyAll();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public void handleRRQ(byte[] nextMessage, int opcode){
        switch (opcode) {
            case DATA:
                new ACK(outputStream, nextMessage).execute();;
                if(!currentCommand.getReceiveData().processPacket(nextMessage)){
                    System.out.println("RRQ "+ currentCommand.getReceiveData().getFileName() + " complete");
                    currentCommand.resetFields();
                }
                break;
            
            case ERROR:
                try {
                    currentCommand.deleteRRQFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handleERROR(nextMessage);  

            default:
                //TODO: handle?
                break;
        }
    }    
    public void handleWRQ(byte[] nextMessage, int opcode){
        switch (opcode) {
            case ACK:
                handleACK(nextMessage);
                if(!currentCommand.getSendData().sendPacket()){
                    System.out.println("WRQ "+ currentCommand.getSendData().getFileName() + " complete");
                    currentCommand.resetFields();
                }
                break;
            
            case ERROR:
                handleERROR(nextMessage);
                break;
        }

    }    
    public void handleDIRQ(byte[] nextMessage, int opcode){
        switch (opcode) {
            case DATA:
                new ACK(outputStream, nextMessage).execute();
                if(!currentCommand.getReceiveData().processPacket(nextMessage)){
                    System.out.println("before reseFields");
                    currentCommand.resetFields();
                }
                break;
            
            case ERROR:
                handleERROR(nextMessage);
                break;
        }
    }    

    public void handleLOGRQ(byte[] nextMessage, int opcode){
        switch (opcode) {
            case ACK:
                handleACK(nextMessage);
                currentCommand.resetFields();
                break;
            
            case ERROR:
                handleERROR(nextMessage);
                break;

        }
    }

    public void handleDELRQ(byte[] nextMessage, int opcode){
        switch (opcode) {
            case ACK:
                handleACK(nextMessage);
                currentCommand.resetFields();
                break;
            
            case ERROR:
                handleERROR(nextMessage);
                break;

        }
    }    
    public void handleDISC(byte[] nextMessage, int opcode){
        switch (opcode) {
            case ACK:
                handleACK(nextMessage);
                break;
                
            case ERROR:
                handleERROR(nextMessage);
                break;
            }        

        currentCommand.resetFields();
        terminate = true;

        try {
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void handleERROR(byte[] nextMessage){
        System.out.println("ERROR " + Util.twoByteToInt(new byte[]{nextMessage[2], nextMessage[3]}));
        currentCommand.resetFields();
    }

    public void handleBCAST(byte[] nextMessage){
        String action;
        if(nextMessage[2] == 0) {action = "del ";} 
        else {action = "add ";}
        System.out.println("BCAST " + action + Util.extractString(nextMessage, 3));
    }

    public void handleACK(byte[] nextMessage){
        System.out.println("ACK " + Util.twoByteToInt(new byte[]{nextMessage[2], nextMessage[3]}));
    }

}












// public void run1() {
//     int read;
//     try {
//         while (!terminate && (read = inputStream.read()) >= 0) {
//             byte[] nextMessage = encdec.decodeNextByte((byte) read);
//             if (nextMessage != null) { 
//                 //TODO: remove before submitting
//                 System.out.println("output bytes: (length =)" + nextMessage.length);
//                 for (int i = 0; i < nextMessage.length; i++){
//                     System.err.println(i + ": " + nextMessage[i]);
//                 }
//                 int opcode = Util.getOpcode(nextMessage);
//                 STATE curState = currentCommand.getState();
//                 if(opcode == DATA){
//                     if(curState.equals(STATE.RRQ)){
//                         new ACK(outputStream, nextMessage).execute();;
//                         if(!currentCommand.getReceiveData().processPacket(nextMessage)){
//                             System.out.println("RRQ "+ currentCommand.getReceiveData().getFileName() + " complete");
//                             currentCommand.resetFields();
//                         }

//                     }
//                     else if(curState.equals(STATE.DIRQ)){
//                         new ACK(outputStream, nextMessage).execute();;
//                         if(!currentCommand.getReceiveData().processPacket(nextMessage)){
//                             currentCommand.resetFields();
//                         }
//                     }
//                     //TODO: handle case state != RRQ/DIRQ

//                 } else if(opcode == ACK){
//                     System.out.println("ACK " + Util.twoByteToInt(new byte[]{nextMessage[2], nextMessage[3]}));
//                     if(curState.equals(STATE.WRQ)){
//                         if(!currentCommand.getSendData().sendPacket()){
//                             System.out.println("WRQ "+ currentCommand.getSendData().getFileName() + " complete");
//                             currentCommand.resetFields();
//                         }
//                         //currentCommand.resetFields(); //mistake?
//                     } else{
//                         if(curState.equals(STATE.DISC)){
//                             synchronized(keyboard){
//                                 keyboard.terminate = true;
//                             } 
//                         }
//                         else if(curState.equals(STATE.LOGRQ)){
//                             synchronized(keyboard){
//                                 keyboard.loggedIn = true;
//                             }
//                         }
//                     }
//                 } else if(opcode == BCAST){
//                     System.out.println("BCAST " + nextMessage[2] + " " + Util.extractString(nextMessage, 3));
//                     currentCommand.resetFields();
//                 }
//                 else if(opcode == ERROR){
//                     System.out.println("ERROR " + Util.twoByteToInt(new byte[]{nextMessage[2], nextMessage[3]}) + Util.extractString(nextMessage, 4));
//                     if(curState.equals(STATE.RRQ)){
//                         currentCommand.deleteRRQFile();
//                     }
//                     currentCommand.resetFields();
//                 }
                
//                 if(curState.equals(STATE.Unoccupied)){
//                     synchronized(waitOnObject){
//                         waitOnObject.notifyAll();
//                     }
//                 }
//             }
//         }
//     } catch (IOException e) {}
