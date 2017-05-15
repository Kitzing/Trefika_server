/**
 * @author Sara Kitzing
 * Creates and runs the server
 */
public class RunClient {

    public static void main (String[] args){
        Client client;
        client = new Client("compiledCode","testCode");
        client.startRunning();
    }

}
