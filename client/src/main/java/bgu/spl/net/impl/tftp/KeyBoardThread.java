package bgu.spl.net.impl.tftp;

import java.util.Scanner;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class KeyBoardThread implements Runnable{

    BlockingQueue<String> commandQueue;
    boolean terminate = false;

    
    public KeyBoardThread(BlockingQueue<String> commandQueue) {
        this.commandQueue = commandQueue;
    }


    @Override
    public void run() {
        // TODO Auto-generated method stub
        Scanner scanner = new Scanner(System.in);
        while (! terminate) {
            if (scanner.hasNextLine()) {
                String userInput = scanner.nextLine();
                try {
                    commandQueue.put(userInput);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        scanner.close();
    }
}
