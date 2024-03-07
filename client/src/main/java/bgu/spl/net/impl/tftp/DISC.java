package bgu.spl.net.impl.tftp;

import java.io.IOException;
import java.io.OutputStream;

import bgu.spl.net.api.Command;

public class DISC implements Command{
    OutputStream outputStream;

    
    public DISC(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void execute() {
        try {
            outputStream.write(new byte[]{0,10});
            outputStream.flush();
        } catch (IOException e) {}
    }

}
