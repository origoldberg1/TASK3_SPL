package bgu.spl.net.impl.tftp;

import java.io.Serializable;

import bgu.spl.net.impl.rci.Command;

public class DATA implements Command<byte[]> {

    @Override
    public byte[] execute(byte[] arg) {
        // TODO Auto-generated method stub
        System.out.println("packet size=" + Util.twoByteToInt(new byte[]{arg[2], arg[3]}));
        byte[] ack={(byte)0x00, (byte)0x04, arg[4], arg[5]};
        return ack;
    }

}
