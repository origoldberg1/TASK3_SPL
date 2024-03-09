package bgu.spl.net.impl.tftp;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import bgu.spl.net.api.Command;

public class WRQ implements Command{
    OutputStream outputStream;
    String fileName;
    CurrentCommand currentCommand;

    public WRQ(OutputStream outputStream, String fileName, CurrentCommand currentCommand) {
        this.outputStream = outputStream;
        this.fileName = fileName;
        this.currentCommand = currentCommand;
    }

    @Override
    public void execute() {
        //Path filePath = Paths.get(System.getProperty("user.dir")).resolve("client").resolve(getFileName(userInput));
        Path filePath = Paths.get(System.getProperty("user.dir")).resolve("client").resolve(fileName);
        currentCommand.setFilePath(filePath);
        currentCommand.setState(STATE.WRQ);
        currentCommand.setSendData(new SendData(fileName, outputStream));
        byte[] fileNameBytes = fileName.getBytes();
        byte[] packet = Util.padPacketEndZero((byte)2, fileNameBytes);
        try {
            outputStream.write(packet);
            outputStream.flush();
        } catch (IOException e) {}
    }
}


