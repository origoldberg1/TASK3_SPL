package bgu.spl.net.impl.tftp;

import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class KeyBoardThread implements Runnable{

    boolean terminate = false;
    Util.State state = Util.State.Simple; 
    CommandParser commandParser;
    OutputStream outputStream;
    CurrentCommand currentCommand;


    
    public KeyBoardThread(CommandParser commandParser, OutputStream outputStream, CurrentCommand currentCommand) {
        this.commandParser = commandParser;
        this.outputStream = outputStream;
        this.currentCommand = currentCommand;
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
                        currentCommand.setFilePath(filePath);
                        currentCommand.setState(STATE.Writing);
                        currentCommand.setSendData(new SendData(outputStream, filePath.toString()));
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
