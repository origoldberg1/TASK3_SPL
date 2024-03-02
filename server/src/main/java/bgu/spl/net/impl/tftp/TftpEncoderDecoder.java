package bgu.spl.net.impl.tftp;

import java.util.Arrays;

import bgu.spl.net.api.MessageEncoderDecoder;

public class TftpEncoderDecoder implements MessageEncoderDecoder<byte[]> {
    //TODO: Implement here the TFTP encoder and decoder

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;

    @Override
    public byte[] decodeNextByte(byte nextByte) {
        // TODO: implement this
        if(len >= 2 && nextByte == 0){
            switch (bytes[1]) {
                case 1: case 2: case 7: case 8:
                    if(decodeNextByteLogrqDelrqRrqWrq(nextByte)) {return bytes;}
                case 3:
                    return new DATA().execute(message);
                case 4:
                    return new ACK().execute(message);
                case 5:
                    return new ERROR().execute(message);
                case 6:
                    return bytes;
                 case 9:
                    return new BCAST().execute(message);
                case 10:
                    return new DISC().execute(message);
                default:
                    break;
            }
        }
        else{
            pushByte(nextByte);
        }
        return null;
    }

    
    @Override
    public byte[] encode(byte[] message) {
        //TODO: implement this
        byte[] res = new byte[message.length + 1];
        for (int i = 0; i < message.length; i++) {
            res[i] = message[i];
        }
        res[res.length - 1] = 0;
        return res;
    }
    
    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }
    
    private boolean decodeNextByteLogrqDelrqRrqWrq(byte nextByte){
        if(nextByte == 0) {return true;}
        pushByte(nextByte);
        return false;
    }
    

}