package bgu.spl.net.impl.tftp;
public class ACK 
{
  private byte [] ackInByte;

    public ACK(String opcode)
    {
        if(opcode=="LOGRQ" || opcode=="WRQ" || opcode=="DELERQ"|| opcode=="DISC") //according to instructions in 3.2.6
        {
            ackInByte= new byte[]{(byte)0x00, (byte)0x09, (byte)0x00, (byte)0x00};
        }
        else //acknowledge Data packet
        {
            ackInByte= new byte[]{(byte)0x00, (byte)0x09, (byte)0x00, (byte)0x00}; //TODO
        }
    }

    public byte [] getAck()
    {
    return ackInByte;
    }
}

    
    