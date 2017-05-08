/**
 * Created by sarakitzing on 2017-05-08.
 */
public class RunClient {

    public static void main (String[] args){
        Client client;
        client = new Client("userCode","compiledCode","testCode");
        client.startRunning();
    }

}
