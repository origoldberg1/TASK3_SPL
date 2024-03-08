package bgu.spl.net.impl.tftp;

import java.nio.file.Path;

enum STATE{
    RRQ,
    WRQ,
    DIRQ,
    LOGRQ,
    DELERQ,
    Unoccupied
}

public class CurrentCommand {
    private Path filePath;
    private STATE state;
    private SendData sendData;
    private ReceiveData receiveData;
    

    public synchronized Path getFilePath() {
        return filePath;
    }
    
    public synchronized void setFilePath(Path filePath) {
        this.filePath = filePath;
    }
    
    public synchronized STATE getState() {
        return state;
    }
    
    public synchronized void setState(STATE state) {
        this.state = state;
    }

    public synchronized SendData getSendData() {
        return sendData;
    }

    public synchronized void setSendData(SendData sendData) {
        this.sendData = sendData;
    }

    public ReceiveData getReceiveData() {
        return receiveData;
    }

    public void setReceiveData(ReceiveData receiveData) {
        this.receiveData = receiveData;
    }

}
