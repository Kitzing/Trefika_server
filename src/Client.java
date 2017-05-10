import java.io.*;
import java.net.*;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * Created by sarakitzing on 2017-05-02.
 */

public class Client {
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String userCode;
    private String result;
    private ServerSocket server;
    private Socket connection;
    private Interpreter bsh;
    private PrintStream out;
    private PrintStream err;
    private String userFile;
    private String compFile;
    private String errFile;
    private int length;

    //constructor
    public Client(String userFile, String compFile, String errFile){
        this.userFile = userFile;
        this.compFile = compFile;
        this.errFile = errFile;
        this.length = 0;
        try {
            out = new PrintStream(new FileOutputStream(compFile));
            err = new PrintStream(new FileOutputStream(errFile));
            bsh = new Interpreter(new StringReader(""), out, err, false );
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

    }

    //set up and run server
    public void startRunning(){
        try {
            server = new ServerSocket(6789, 100);
            while(true) {
            try {
                //connect and compile code
                waitForConnection();
                setupStreams();
                whileCompile();

            } catch (EOFException eofException) {
                System.out.println("Server ended connection");
            } finally {


            }
            }
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    //wait for connection, then display connaction information
    private void waitForConnection() throws IOException {
        System.out.println("Waiting for connection ... ");
        connection = server.accept();
        System.out.println("Now connected to " + connection.getInetAddress().getHostName());
    }


    //set up streams
    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        System.out.println("Streams are now setup");
    }


    //compile code
    private void whileCompile() throws IOException{
        try{
            this.userCode = (String) input.readObject();
            System.out.println("Compiling code ... ");
            compileCode(userCode);
            output.writeObject(result);
            System.out.println("Done compiling");
        } catch(ClassNotFoundException e){
            System.out.println("Don't know object type");
        }
    }

    //close the streams and sockets
    private void shutDown(){
        System.out.println("Closing connections...");
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    //calls beanshell commands to compile the code
    private void compileCode(String userCode){
        try {
            bsh.eval(userCode);
            readFile(compFile);
        }catch(EvalError e){
            this.result = "Error";
            System.out.println("Error");

        }

    }

    //reads file
    public void readFile(String file){
        try {
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();

            while(line != null){
                sb.append(" " + line);
                line = reader.readLine();
            }

            result = clearOldData(sb).toString().trim();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

    }


    private StringBuilder clearOldData(StringBuilder sb) {
        sb.delete(0, length);
        length += sb.length();
        return sb;
    }

}
