package bgu.spl.net.impl.tftp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;

enum State {
    WritingFile,
    ReadingFile,
    Simple
};


public class CommandProccessorThread implements Runnable {

    BlockingQueue<String> commandQueue; 
    boolean terminate = false;
    CommandParser commandParser;
    OutputStream outputStream;

    State state;
    String fileName;
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(10);

    public CommandProccessorThread(BlockingQueue<String> commandQueue, CommandParser commandParser,
            OutputStream outputStream) {
        this.commandQueue = commandQueue;
        this.commandParser = commandParser;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(!terminate){
            String command;
            try {
                command = commandQueue.take();
                String op = getOp(command);
                Path filePath = FileSystems.getDefault().getPath("").resolve(fileName );
                byte[] packet;
                if ("ACK".equals(op) && state == State.WritingFile) {
                    int arg = getIntArg(command);
                    packet = sendFileData(filePath, arg);
                } else if ("ACK".equals(op) && state == State.ReadingFile) {
                    // read data
                    packet = commandParser.parse("ACK");
                } else if ("ACK".equals(op)) {
                    System.err.println("ACK");
                } else {
                    packet = commandParser.parse(command);
                }
                outputStream.write(packet);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } 

    private String getOp(String command) {
        return command.split(" ")[0];
    }

    private String getPacketSize(String command) {
        return command.split(" ")[1];
    }

    private String getArg(String command){
        return command.split(" ")[2];
    }

    private int getIntArg(String command) {
        return Integer.valueOf(getArg(command));
    }


}


