// package bgu.spl.net.impl.tftp;

// import java.io.IOException;
// import java.io.OutputStream;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.util.concurrent.BlockingQueue;




// public class CommandProccessorThread implements Runnable {

//     BlockingQueue<String> commandQueue; 
//     boolean terminate = false;
//     CommandParser commandParser;
//     OutputStream outputStream;


//     Util.State state;
//     SendData sendData;

//     public CommandProccessorThread(BlockingQueue<String> commandQueue, CommandParser commandParser,
//             OutputStream outputStream) {
//         this.commandQueue = commandQueue;
//         this.commandParser = commandParser;
//         this.outputStream = outputStream;
//     }

//     @Override
//     public void run() {
//         // TODO Auto-generated method stub
//         while(!terminate){
//             String command;
//             try {
//                 command = commandQueue.take();
//                 String op = Util.getOpcode(command);
//                 byte[] packet = null;
//                 if ("ACK".equals(op) && state == Util.State.SendingData) {
//                     int arg = getIntArg(command);
//                     packet = this.sendData.makePacket(arg);
//                     packet = padDataPacket(packet, arg);
//                 } else if ("ACK".equals(op) && state == Util.State.ReceivingData) {
//                     // read data
//                     packet = commandParser.parse("ACK");
//                 } else if ("ACK".equals(op)) {
//                     System.err.println("ACK");
//                 } else if("WRQ".equals(op)){
//                     Path filePath = Paths.get(System.getProperty("user.dir")).resolve("client").resolve(getFileName(command));
//                     sendData = new SendData(filePath);
//                     packet = commandParser.parse(command);
//                     state = Util.State.SendingData;
//                 } else {
//                     packet = commandParser.parse(command);
//                 } 
//                 if(packet != null){
//                     outputStream.write(packet);
//                     outputStream.flush();
//                 }
//             } catch (InterruptedException e) {
//                 e.printStackTrace();
//             } catch (IOException e) {
//                 e.printStackTrace();
//             }
//         }
//     } 

//     private String getArg(String command){
//         return command.split(" ")[1];
//     }

//     private int getIntArg(String command) {
//         return Integer.valueOf(getArg(command));
//     }

//     private String getFileName(String command){
//        String[] splitCommand = command.split(" ");
//        String fileName = splitCommand[1];
//        for (int i = 2; i < splitCommand.length; i++) {
//             fileName = fileName  + " " + splitCommand[i];
//        } 
//        return fileName;
//     }


// }


