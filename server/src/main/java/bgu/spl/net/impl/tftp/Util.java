package bgu.spl.net.impl.tftp;

public class Util {

    public static byte[] intToByte2(int a){
        return new byte []{(byte)( a >> 8) , ( byte )( a & 0xff ) };
    }
    
    public static int twoByteToInt(byte [] bytes){
        return ( int ) ((( int ) bytes [0]) << 8 | ( int ) ( bytes [1]) );
    }
}
