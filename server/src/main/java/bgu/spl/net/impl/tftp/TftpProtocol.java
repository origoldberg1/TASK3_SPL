package bgu.spl.net.impl.tftp;
import bgu.spl.net.api.BidiMessagingProtocol;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.Connections;

public class TftpProtocol implements BidiMessagingProtocol <byte[]>  {
    
    private BlockingConnectionHandler <byte[]> handler;
    private TftpConnections connectionsObj;
    private volatile boolean shouldTerminate=false;  
    private HoldsDataToSend dataToSend; //
    private int connectionId;
    public HoldsDataToSend packetToSend() //we add that
    {
        return dataToSend; 
    }
    
    @Override
    public void start(int connectionId, Connections <byte[]> connections, ConnectionHandler <byte[]>  connectionHandler) {
        // TODO implement this
        this.handler = (BlockingConnectionHandler<byte[]>) connectionHandler;
        this.connectionsObj= (TftpConnections) connections;
        this.connectionId=connectionId;
        this.dataToSend=null;
        System.out.println("start");
        //throw new UnsupportedOperationException("Unimplemented method 'start'");
    }

    @Override
    public void process(byte[] message) 
    {
        // TODO implement this
        System.out.println("process");
        switch (message[1])
        {
            case 1:
                new RRQ().execute(message, handler, connectionsObj);
                break;
            case 2:
                new WRQ().execute(message, handler, connectionsObj);
                break;
            case 3:
                new DATA().execute(message, handler, connectionsObj);   
                break;
            case 4:
                dataToSend.sendPacket(message);                 
            case 6:
                new DIRQ().execute(message, handler, connectionsObj);
                break;
            case 7:
                new LOGRQ().execute(message, handler, connectionsObj);
                break;
            case 8:
                new DELRQ().execute(message, handler, connectionsObj);
                break;
            case 10:
                new DISC().execute(message, handler, connectionsObj);
                // if(handler.getName()==null){
                // setShouldTerminate();
                // }
                // else{ //is logged in
                // connectionsObj.send(handler.getId(), new ACK(new byte[]{0,0}).getAck());
                //}
                break;
            default:
            connectionsObj.send(handler.getId(),new ERROR (4).getError());
        }
    } 
    

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
        //throw new UnsupportedOperationException("Unimplemented method 'shouldTerminate'");
    }

    public void setShouldTerminate()
    {
        shouldTerminate=true;
    }

    public void setHoldsDataToSend(HoldsDataToSend dataToSend)
    {
        this.dataToSend=dataToSend;
    }

}