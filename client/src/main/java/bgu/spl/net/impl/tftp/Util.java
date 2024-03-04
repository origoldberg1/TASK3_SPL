package bgu.spl.net.impl.tftp;

public class Util {

    enum State {
        SendingData,
        ReceivingData,
        Simple
    };
    
    public static byte[] intToTwoByte(int a){
        return new byte []{(byte)( a >> 8) , ( byte )( a & 0xff ) };
    }

    public static int twoByteToInt(byte [] bytes){
        return ( int ) ((( int ) bytes [0]) << 8 | ( int ) ( bytes [1]) );
    }

    public static byte getOpcodeValue(String str){
        switch (str) {
            case "RRQ":
                return 1;
            case "WRQ":
                return 2;
            case "DATA":
                return 3;
            case "ACK":
                return 4;
            case "ERROR":
                return 5;
            case "DIRQ":
                return 6;
            case "LOGRQ":
                return 7;
            case "DELRQ":
                return 8;
            case "BCAST":
                return 9;
            case "DISC":
                return 10;
    
            default:
                return 0;
        }
    }

    public static String getOpcode(String command) {
        return command.split(" ")[0];
    }
}
