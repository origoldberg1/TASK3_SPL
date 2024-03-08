package bgu.spl.net.impl.tftp;
import java.util.ArrayList;
import java.util.List;
import bgu.spl.net.srv.BlockingConnectionHandler;

//This class implements sending packets mechanism as a result of RRQ request
public class HoldsDataToSend 
{
    
    private int block;
    private byte [] dataToSend;
    private TftpConnections connectionsObj;
    private boolean sendEmptyPacket;
    private BlockingConnectionHandler handler;
    private final int PACKET_SIZE=512;

    
    public boolean errorFound(byte[] arg)
    {
        //error 4- illegal TFTP operation- unknown opcode
        if((arg[1]>7 || arg[1]<1 ||arg.length <= 1)){ 
            connectionsObj.send(handler.getId(),new ERROR (4).getError());
            return true;
        }
        // error 6- user not logged in
        if(arg[1] != 7 && handler.getName()==null){ 
            connectionsObj.send(handler.getId(), new ERROR(6).getError());
            return true;
        }
        return false;   
    }

    public HoldsDataToSend(TftpConnections connectionsObj,byte[] dataToSend, BlockingConnectionHandler handler)
    {
       this.connectionsObj=connectionsObj;
       this.dataToSend=dataToSend;
       sendEmptyPacket=false;
       this.handler=handler;  
    }

    public void sendPacket(byte [] message)
    { 
        if(!errorFound(message))
        {
            int firstIndex=(block-1)*PACKET_SIZE;
            int leftBytes=dataToSend.length-(block-1)*PACKET_SIZE;
            if(leftBytes==0){
                sendEmptyPacket();
                leftBytes=-1; 
            }
            if(leftBytes>0){
                List<Byte> byteList = new ArrayList<>();
                byte [] packetArr;
                byteList.add((byte)0); //filling opcode field
                byteList.add((byte)3); //filling opcode field
                byteList.add((byte)0); //filling first byte of packet size 
                if(leftBytes<PACKET_SIZE){
                    byteList.add((byte)leftBytes); //filling second byte of packet size field
                }
                else{
                    byteList.add((byte)PACKET_SIZE); //filling second byte of packet size field
                } 
                //filling block field
                byte [] blockField=Util.intToTwoByte(block);
                byteList.add(blockField[0]);
                byteList.add(blockField[1]);
                //filling data field
                for(int k=firstIndex; k<=firstIndex+PACKET_SIZE-1 && k<dataToSend.length; k++) 
                {
                    byteList.add(dataToSend[k]);
                }
                block++;
                packetArr=Util.convertListToArr(byteList);
                connectionsObj.send(handler.getId(), packetArr); 
            }      
        }
    }

    // public static byte [] convertListToArr( List<Byte> packetList)
    // {
    //     byte[] byteArray = new byte[packetList.size()];
    //     for (int i = 0; i < packetList.size(); i++) 
    //     {
    //         byteArray[i] = packetList.get(i);
    //     }
    //     return byteArray;
    // }

    public void sendEmptyPacket()
    {
        byte [] blockField=Util.intToTwoByte(block);
        byte [] emptyPacket= new byte[] {0,0,0,0,blockField[0], blockField[1]};
        connectionsObj.send(handler.getId(), emptyPacket); 
    }
}

    
    