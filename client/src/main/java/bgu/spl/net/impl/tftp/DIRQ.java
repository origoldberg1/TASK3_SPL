package bgu.spl.net.impl.tftp;

import java.io.IOException;
import java.io.OutputStream;

import bgu.spl.net.api.Command;

public class DIRQ implements Command{

    OutputStream outputStream;

    
    public DIRQ(OutputStream outputStream) {
        this.outputStream = outputStream;
    }


    @Override
    public void execute() {
        // TODO Auto-generated method stub
        try {
            outputStream.write(new byte[]{0,7});
            outputStream.flush();
        } catch (IOException e) {}
    }

}
