package bgu.spl.net.impl.tftp;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;
import java.util.ArrayList;

public class RRQ implements Command<byte[]> {

    @Override
    public void execute(byte[] arg, BlockingConnectionHandler <byte[]> handler, TftpConnections connectionsObject) 
    {
        byte [] bytesFileName= new byte[arg.length-2];//acording ori we get args withoud the last byte 
        for(int i=2; i<arg.length-2; i++)
        {
            bytesFileName[i-2]=arg[i];
        }
        String fileName = new String(bytesFileName, StandardCharsets.UTF_8);             
        try{
            Path filePath = Paths.get("server/Flies"+fileName); 
            byte[] dataByte = Files.readAllBytes(filePath);
            sendPacketsOf(dataByte, handler, connectionsObject);
            // connectionsObject.send(handler.getId(), dataByPacskets); //TODO: converting to packets

        }catch(IOException e){connectionsObject.send(handler.getId(),new ERROR(1).getError());}
    }

    public static void sendPacketsOf(byte [] dataByte, BlockingConnectionHandler <byte[]> handler, TftpConnections connectionsObject)
    {
        List<Byte> byteList = new ArrayList<>();
        int block=1;
        int leftBytes=dataByte.length;
        for(int i=0; i<dataByte.length; i++)
        {
            List<Byte> packetList = new ArrayList<>();
            byte [] packetArr;
            byteList.add((byte)0); //filling opcode field
            byteList.add((byte)3); //filling opcode field
            byteList.add((byte)0); //filling first byte of packet size 
            if(leftBytes<512)
            {
                byteList.add((byte)leftBytes); //filling second byte of packet size 
            }
            else
            {
                byteList.add((byte)512); //filling second byte of packet size 
            }
            //TODO: filling block field
            for(int k=1; k<=512 && k<=leftBytes; k++) //filling data field
            {
                byteList.add(dataByte[k]);
            }
            leftBytes=leftBytes-512;
            block++;
            packetArr=convertListToArr(packetList);
            connectionsObject.send(handler.getId(), packetArr); 

            if (leftBytes==0) //we finished with the last packet which included 512 bytes so we should add one more packet
            {
                packetArr=new byte [6];
                packetArr[0]=(byte)0; //fiiling opcode field
                packetArr[1]=(byte)3; //fiiling opcode field
                packetArr[2]=(byte)3; //filling size packet field
                packetArr[3]=(byte)3;  //filling size packet field
                //packetArr[4]= TODO:filling block field
                //packetArr[5]= TODO:filling block field
                connectionsObject.send(handler.getId(), packetArr);
                break;
            }
        }
    }

    public static byte [] convertListToArr( List<Byte> packetList)
    {
        byte[] byteArray = new byte[packetList.size()];
        for (int i = 0; i < packetList.size(); i++) 
        {
            byteArray[i] = packetList.get(i);
        }
        return byteArray;
    }
    
}