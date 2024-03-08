package bgu.spl.net.impl.tftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Util {

    enum State {
        SendingData,
        ReceivingData,
        Simple
    };
    
    public static byte[] intToTwoByte(int a){
        return new byte []{(byte)( a >> 8) , ( byte )( a & 0xff ) };
    }

    public static int twoByteToInt(byte [] bytes){
        //return ( int ) ((( int ) bytes [0]) << 8 | ( int ) ( bytes [1]) );
        return (int) (bytes[0]) << 8 | (short) (bytes[1]) & 0x00ff;

    }

    public static byte getOpcodeValue(String str){
        switch (str) {
            case "RRQ":
                return 1;
            case "WRQ":
                return 2;
            case "DATA":
                return 3;
            case "ACK":
                return 4;
            case "ERROR":
                return 5;
            case "DIRQ":
                return 6;
            case "LOGRQ":
                return 7;
            case "DELRQ":
                return 8;
            case "BCAST":
                return 9;
            case "DISC":
                return 10;
    
            default:
                return 0;
        }
    }

    public static String getOpcode(String command) {
        return command.split(" ")[0];
    }

    public static byte[] padDataPacket(byte[] packet, int blockNumber){
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

    public static byte[] padPacketEndZero(byte opcode, byte[] packet){
        byte[] res = new byte[packet.length + 3];
        res[0] = 0;
        res[1] = opcode;
        res[res.length - 1] = 0;
        int indent = 2;
        for (int i = 0; i < packet.length; i++) {
            res[i + indent] = packet[i];
        }
        return res;
    }

    public static String getFileName(String userInput){
        String[] splitUserInput = userInput.split(" ");
        String fileName = splitUserInput[1];
        for (int i = 2; i < splitUserInput.length; i++) {
             fileName = fileName  + " " + splitUserInput[i];
        } 
        return fileName;
     }

    
    public static int getOpcode(byte[] msg){
        return msg[1];
    }

    public static byte[] readFile(String fileFullPath) throws IOException {
        FileInputStream fis = new FileInputStream(fileFullPath);
        //extracting file size (in bytes)
        long fileSize= new File(fileFullPath).length();
        //reads all data to buffer
        byte[] fileBytes = new byte[(int) fileSize];
        fis.read(fileBytes);
        fis.close();
        return fileBytes;
    }

    public static void writeFile(String fileName, byte[] bytes) throws FileNotFoundException, IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(bytes);
            fos.close(); // There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        }
    }

}
