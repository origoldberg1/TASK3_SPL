package bgu.spl.net.impl.tftp;
public class ACK 
{
  private byte [] ackInByte;

    public ACK(byte blockNumber)
    {
            ackInByte= new byte[]{(byte)0x00, (byte)0x04, (byte)0x00, blockNumber}; //TODO     
    }

    public byte [] getAck()
    {
    return ackInByte;
    }
}

    
    