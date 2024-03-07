package bgu.spl.net.impl.tftp;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.nio.file.Path;
import java.nio.file.Paths;

public class KeyBoardThread implements Runnable{

    boolean terminate = false;
    Util.State state = Util.State.Simple; 
    CommandParser commandParser;
    OutputStream outputStream;


    
    public KeyBoardThread(CommandParser commandParser, OutputStream outputStream) {
        this.commandParser = commandParser;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        Scanner scanner = new Scanner(System.in);
        while (! terminate) {
            if (scanner.hasNextLine()) {
                String userInput = scanner.nextLine();
                switch (Util.getOpcodeValue(userInput.split(" ")[0])){
                    case 2:
                        String fileName = Util.getFileName(userInput);
                        Path filePath = Paths.get(System.getProperty("user.dir")).resolve("client").resolve(fileName);
                        new WRQ(outputStream, filePath, fileName).execute();
                        break;
                    case 6:
                        new DIRQ(outputStream).execute();
                        break;
                    case 7:
                        new LOGRQ(outputStream, userInput.split(" ")[1]).execute();
                        break;
                    case 10:
                        new DISC(outputStream).execute();
                        break;

                    default:
                        break;
                }
                    
            }
        }
        scanner.close();
    }




}
