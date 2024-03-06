package bgu.spl.net.impl.tftp;

import java.io.IOException;
import java.io.OutputStream;

import bgu.spl.net.api.ClientCommand;

public class LOGRQ implements ClientCommand{
    OutputStream outputStream;
    String userName;

    
    public LOGRQ(OutputStream outputStream, String userName) {
        this.outputStream = outputStream;
        this.userName = userName;
    }


    @Override
    public void execute() {
        // TODO Auto-generated method stub
        byte[] userNameBytes = userName.getBytes(); 
        byte[] msg = new byte[userNameBytes.length + 3];
        msg[0] = 0;
        msg[1] = 7;
        int indent = 2;
        for (int i = 0; i < userNameBytes.length; i++) {
            msg[i + indent] = userNameBytes[i];
        }
        msg[msg.length - 1] = 0;
        try {
            outputStream.write(msg);
            outputStream.flush();
        } catch (IOException e) {}
    }


}
