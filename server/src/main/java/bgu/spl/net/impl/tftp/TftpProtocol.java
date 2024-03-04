package bgu.spl.net.impl.tftp;
import bgu.spl.net.api.BidiMessagingProtocol;
import bgu.spl.net.srv.BlockingConnectionHandler;

public class TftpProtocol implements BidiMessagingProtocol<byte[]>  {

    boolean shouldTerminate = false;
    private BlockingConnectionHandler <byte[]> handler;
    private TftpConnections connectionsObj;    

    
    @Override
    public void start(int connectionId, TftpConnections connectionsObj, BlockingConnectionHandler <byte[]>  connectionHandler) {
        // TODO implement this
        this.handler=connectionHandler;
        this.connectionsObj=connectionsObj;
        System.out.println("start");
        //throw new UnsupportedOperationException("Unimplemented method 'start'");

    }

    @Override
    public void process(byte[] message) {
        // TODO implement this
        System.out.println("process");
        if(message.length > 1){
            if(message[1]!=7 && handler.getName()==null)
            {
                connectionsObj.send(handler.getId(), new ERROR(6).getError());
            }
            else
            {
                switch (message[1]) 
                {
                    case 1:
                        new RRQ().execute(message, handler, connectionsObj);
                    case 2:
                        new WRQ().execute(message, handler, connectionsObj);
                    case 3:
                        new DATA().execute(message, handler, connectionsObj);                    
                    case 6:
                        new DIRQ().execute(message, handler, connectionsObj);
                    case 7:
                        new LOGRQ().execute(message, handler, connectionsObj);
                    case 8:
                        new DELRQ().execute(message, handler, connectionsObj);
                    case 10:
                        new DISC().execute(message, handler, connectionsObj);
                    default:
                        connectionsObj.send(handler.getId(),new ERROR (4).getError());
                }
            }
        }
        connectionsObj.send(handler.getId(),new ERROR (4).getError()); 
        //throw new UnsupportedOperationException("Unimplemented method 'process'");
    }

    @Override
    public boolean shouldTerminate() {
        // TODO implement this
        System.out.println("should terminate: " + shouldTerminate);
        return shouldTerminate;
        //
        //throw new UnsupportedOperationException("Unimplemented method 'shouldTerminate'");
    }
}
