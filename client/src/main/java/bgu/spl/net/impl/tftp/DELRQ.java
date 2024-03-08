package bgu.spl.net.impl.tftp;

import java.io.IOException;
import java.io.OutputStream;

import bgu.spl.net.api.Command;

public class DELRQ implements Command{
    OutputStream outputStream;
    String fileName;

    public DELRQ(OutputStream outputStream, String fileName) {
        this.outputStream = outputStream;
        this.fileName = fileName;
    }

    @Override
    public void execute() {
        byte[] fileNameBytes = fileName.getBytes(); 
        byte[] msg = Util.padPacketEndZero((byte)8, fileNameBytes);
        try {
            outputStream.write(msg);
            outputStream.flush();
        } catch (IOException e) {}
    }

}
