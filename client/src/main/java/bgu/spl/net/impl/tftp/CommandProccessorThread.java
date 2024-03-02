package bgu.spl.net.impl.tftp;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

public class CommandProccessorThread implements Runnable {

    BlockingQueue<String> commandQueue; 
    boolean terminate = false;
    CommandParser commandParser;
    OutputStream outputStream;

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
                byte[] packet = commandParser.parse(command);
                outputStream.write(packet);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } 
}
