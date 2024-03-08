package bgu.spl.net.impl.tftp;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import bgu.spl.net.api.Command;

public class WRQ implements Command{
    OutputStream outputStream;
    String fileName;

    public WRQ(OutputStream outputStream, String fileName) {
        this.outputStream = outputStream;
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        //Path filePath = Paths.get(System.getProperty("user.dir")).resolve("client").resolve(getFileName(userInput));
        byte[] fileNameBytes = fileName.getBytes();
        byte[] packet = Util.padPacketEndZero((byte)2, fileNameBytes);
        try {
            outputStream.write(packet);
            outputStream.flush();
        } catch (IOException e) {}
    }
}
        

        
    //     SendData sendData = new SendData(filePath);
    //     int blockNumber = 0;
    //     do{
    //         packet = sendData.makePacket(blockNumber);
    //         try {
    //             outputStream.write(Util.padDataPacket(packet, blockNumber));
    //             outputStream.flush();
    //             System.out.println("keyboard thread: sending packet number " + blockNumber);
    //             synchronized(TftpClient.waitOnObject){
    //                 try {
    //                     TftpClient.waitOnObject.wait();
    //                 } catch (InterruptedException e) {}
    //             }

    //         } catch (IOException e) {}
    //         blockNumber ++;
    //     }   
    //     while(packet.length == 512);    
    // }


