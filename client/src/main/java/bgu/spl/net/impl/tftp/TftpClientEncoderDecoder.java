package bgu.spl.net.impl.tftp;

import java.util.Arrays;

import bgu.spl.net.api.MessageEncoderDecoder;

public class TftpClientEncoderDecoder implements MessageEncoderDecoder<byte[]> {
    //TODO: Implement here the TFTP encoder and decoder

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private int size;

    @Override
    public byte[] decodeNextByte(byte nextByte) {
        // TODO: implement this
        if(len >= 2){
            switch (bytes[1]) {
                case 1: case 2: case 7: case 8:
                    if(decodeNextByteUntilZero(nextByte)) {return bytes;}
                case 3:
                    if(decodeNextByteKnownPacketLength(nextByte, 6)) {return bytes;}
                case 4:
                    if(decodeNextByteFixedSize(nextByte, 4)) {return bytes;}
                case 5:
                    if(decodeNextByteError(nextByte)) {return bytes;}
                case 6: case 10:
                    return bytes; //will cause an error due to len = 2 always?
                 case 9:
                 if(decodeNextByteBcast(nextByte)) {return bytes;}
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
    
    private boolean decodeNextByteUntilZero(byte nextByte){
        if(nextByte == 0) {return true;}
        pushByte(nextByte);
        return false;
    }
    
    private boolean decodeNextByteKnownPacketLength(byte nextByte, int opcodeLength){
        if(len == 4){
            size =  opcodeLength + ( short ) ((( short ) bytes [2]) << 8 | ( short ) ( bytes [3]) );
        }
        pushByte(nextByte);
        return size == len;
    }

    private boolean decodeNextByteFixedSize(byte nextByte, int size){
        pushByte(nextByte);
        return size == len;        
    }

    private boolean decodeNextByteBcast(byte nextByte){
        if(len >= 3 && nextByte == 0) {return true;}
        pushByte(nextByte);
        return false;
    }

    private boolean decodeNextByteError(byte nextByte){
        if(len >= 4 && nextByte == 0) {return true;}
        pushByte(nextByte);
        return false;
    }

}