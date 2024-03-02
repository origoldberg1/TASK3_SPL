package bgu.spl.net.impl.tftp;


import bgu.spl.net.impl.rci.Command;

public class LOGRQ implements Command<byte[]> {

    @Override
    public byte[] execute(byte[] arg) {
        // TODO Auto-generated method stub
        return new byte[]{0,4,0,0};
    }
    
}
