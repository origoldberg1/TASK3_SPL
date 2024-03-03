package bgu.spl.net.impl.tftp;

import java.io.Serializable;

import bgu.spl.net.impl.rci.Command;

public class WRQ implements Command<byte[]> {

    @Override
    public byte[] execute(byte[] arg) {
        // TODO Auto-generated method stub
       System.out.println("length of msg = " + arg.length);
       return new byte[] {0, 4, 0, 0};
    }
    
}