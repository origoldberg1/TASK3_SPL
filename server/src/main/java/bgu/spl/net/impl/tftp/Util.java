package bgu.spl.net.impl.tftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Util {

    public static byte[] intToByte2(int a){
        return new byte []{(byte)( a >> 8) , ( byte )( a & 0xff ) };
    }
    
    public static int twoByteToInt(byte [] bytes){
        return ( int ) ((( int ) bytes [0]) << 8 | ( int ) ( bytes [1]) );
    }

    public static byte[] readFile(String fileFullPath) throws IOException {
        FileInputStream fis = new FileInputStream(fileFullPath);
        //extracting file size (in bytes)
        long fileSize= new File(fileFullPath).length();
        //reads all data to buffer
        byte[] fileBytes = new byte[(int) fileSize];
        fis.read(fileBytes);
        fis.close();
        return fileBytes;
    }

    public static void writeFile(String fileFullPath, byte[] bytes) throws FileNotFoundException, IOException {
        try (FileOutputStream fos = new FileOutputStream(fileFullPath)) {
            fos.write(bytes);
            fos.close();
        }
    }

    public static byte [] convertListToArr( List<Byte> byteList)
    {
        byte[] byteArray = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) 
        {
            byteArray[i] = byteList.get(i);
        }
        return byteArray;
    }
}
