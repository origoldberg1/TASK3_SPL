package bgu.spl.net.impl.tftp;
public class BCAST //using by Connections class
{
  private byte [] bcastInByte;

    public BCAST(byte [] fileName, byte deletedOrAdded)
    {
        byte [] bcastMsg= new byte [fileName.length+4];
        bcastMsg[0]=(byte)0x00;
        bcastMsg[1]=(byte)0x09;
        bcastMsg[2]=(byte)deletedOrAdded;
        bcastMsg[bcastMsg.length-1]=(byte)0x00;
        for(int i=0; i<fileName.length; i++)//filling the file Name was deleted or added
        {
            bcastMsg[i+3]=fileName[0]; //fileName starts in third cell according to BCAST Packet format
        }
    }

    public byte [] getBcast()
    {
        return bcastInByte;
    }
}

    
    