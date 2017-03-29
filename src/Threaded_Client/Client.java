package Threaded_Client;

/**
 * Created by ThiEn on 29.03.2017.
 */
import java.io.*;
import java.net.*;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class Client {
    /*
    - Build instance of 'FutureTask', give it an instance of 'ClientHandler' as parameter, that implements the Interface 'Callable'
    (similar to 'Runnable')
    - Give 'FutureTask' a new thread and start it. 'call'-method from interface 'callable' of ClientHandler is executed
    - complete communication with the server is running. 'call'-method gives the result from server to 'FutureTask' where it's available
    in the main program. Any time and any place
     */
}
