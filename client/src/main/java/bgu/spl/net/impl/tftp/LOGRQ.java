package bgu.spl.net.impl.tftp;

import java.io.IOException;
import java.io.OutputStream;

import bgu.spl.net.api.Command;

public class LOGRQ implements Command{
    OutputStream outputStream;
    String userName;

    
    public LOGRQ(OutputStream outputStream, String userName) {
        this.outputStream = outputStream;
        this.userName = userName;
    }


    @Override
    public void execute() {
        byte[] msg = Util.padPacketEndZero((byte)7, userName.getBytes());
        try {
            outputStream.write(msg);
            outputStream.flush();
        } catch (IOException e) {}
    }


}
