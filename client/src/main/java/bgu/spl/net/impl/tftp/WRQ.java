package bgu.spl.net.impl.tftp;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import bgu.spl.net.api.ClientCommand;

public class WRQ implements ClientCommand{
    OutputStream outputStream;
    Path filePath;

    @Override
    public void execute() {
        //Path filePath = Paths.get(System.getProperty("user.dir")).resolve("client").resolve(getFileName(userInput));
        SendData sendData = new SendData(filePath);
        int blockNumber = 0;
        byte[] packet;
        do{
            packet = sendData.makePacket(blockNumber);
            try {
                outputStream.write(Util.padDataPacket(packet, blockNumber));
                outputStream.flush();
                System.out.println("keyboard thread: sending packet number " + blockNumber);
                synchronized(TftpClient.waitOnObject){
                    try {
                        TftpClient.waitOnObject.wait();
                    } catch (InterruptedException e) {}
                }

            } catch (IOException e) {}
            blockNumber ++;
        }   
        while(packet.length == 512);    }

}
