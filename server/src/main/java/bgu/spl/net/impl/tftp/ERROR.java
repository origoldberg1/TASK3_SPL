package bgu.spl.net.impl.tftp;
import java.io.UnsupportedEncodingException;

public class ERROR 
{
  private final byte [][] msgList;
  private byte [] errorInByte;

    public ERROR(int value)
    {
        msgList = new byte[8][];
        try{
                msgList[0] = "Not defined, see error message (if any)".getBytes("UTF-8");
                msgList[1] = "File not found – RRQ DELRQ of non-existing file".getBytes("UTF-8");
                msgList[2] = "Access violation – File cannot be written, read or deleted".getBytes("UTF-8");
                msgList[3] = "Disk full or allocation exceeded – No room in disk".getBytes("UTF-8");
                msgList[4] = "Illegal TFTP operation – Unknown Opcode".getBytes("UTF-8");
                msgList[5] = "File already exists – File name exists on WRQ".getBytes("UTF-8");
                msgList[6] = "User not logged in – Any opcode received before Login completes".getBytes("UTF-8");
                msgList[7] = "User already logged in – Login username already connected".getBytes("UTF-8");
    }catch(UnsupportedEncodingException e){}
    

        //Building the apropiate error
        errorInByte= new byte [msgList[value].length+5];
        errorInByte[0]=(byte)0x00;
        errorInByte[1]=(byte)0x05;
        errorInByte[2]=(byte)0x00;
        errorInByte[3]=(byte)value;
        errorInByte[errorInByte.length-1]=(byte)0x00;
        for(int i=0; i<msgList[value].length; i++)
        {
             errorInByte[i+4]=msgList[value][i];
        }

    }

    public byte [] getError()
    {
    return errorInByte;
    }
}

    
    