package bgu.spl.net.impl.tftp;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.nio.file.Path;
import java.nio.file.Paths;

public class KeyBoardThread implements Runnable{

    boolean terminate = false;
    Util.State state = Util.State.Simple; 
    CommandParser commandParser;
    OutputStream outputStream;


    
    public KeyBoardThread(CommandParser commandParser, OutputStream outputStream) {
        this.commandParser = commandParser;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        Scanner scanner = new Scanner(System.in);
        while (! terminate) {
            if (scanner.hasNextLine()) {
                String userInput = scanner.nextLine();
                if(Util.getOpcodeValue(userInput.split(" ")[0]) == 2){
                    Path filePath = Paths.get(System.getProperty("user.dir")).resolve("client").resolve(getFileName(userInput));
                    SendData sendData = new SendData(filePath);
                    int blockNumber = 0;
                    byte[] packet;
                    do{
                        packet = sendData.makePacket(blockNumber);
                        try {
                            outputStream.write(padDataPacket(packet, blockNumber));
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
                    while(packet.length == 512);
                }
            }
        }
        scanner.close();
    }

    private byte[] padDataPacket(byte[] packet, int blockNumber){
        byte[] res = new byte[packet.length + 6];
        res[0] = 0;
        res[1] = 3;

        byte[] packetSizeBytes = Util.intToTwoByte(packet.length);
        res[2] = packetSizeBytes[0];
        res[3] = packetSizeBytes[1];

        byte[] blockNumberBytes = Util.intToTwoByte(blockNumber);
        res[4] = blockNumberBytes[0];
        res[5] = blockNumberBytes[1];

        for (int i = 0; i < packet.length; i++) {
            res[i + 6] = packet[i];
        }
        return res;
    }

    private String getFileName(String userInput){
       String[] splitUserInput = userInput.split(" ");
       String fileName = splitUserInput[1];
       for (int i = 2; i < splitUserInput.length; i++) {
            fileName = fileName  + " " + splitUserInput[i];
       } 
       return fileName;
    }
}
