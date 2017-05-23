/**
 * @author Sara Kitzing
 * Responsible for creating and running the server
 * Uses Client
 */
public class RunClient {

    public static void main (String[] args){
        Client client;
        client = new Client("compiledCode","testCode");
        client.startRunning();
    }

}
