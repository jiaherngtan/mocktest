package vtp2022.dayx.mocktest;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private int port;
    private ArrayList<String> dir;

    public HttpServer(int port, ArrayList<String> dir) {
        this.port = port;
        this.dir = dir;
    }

    public void go() {
        System.out.println("Server App");

        try {
            System.out.println("Server started at " + port);
            // dir validation, if fails, stop server and exit program
            // loop through all the dir in dir ArrayList
            for (String path : dir) {
                // pass dir into a File object
                File file = new File(path);
                // checkpoint #1: check if path exists
                if (file.exists()) {
                    System.out.println("Directory " + path + " exists");
                } else {
                    System.out.printf("Directory %s doesn't exist\n", path);
                    System.out.println("System exit 1");
                    System.exit(1);
                }
                // checkpoint #2: check if path is a directory
                if (file.isDirectory()) {
                    System.out.println(path + " is a directory");
                } else {
                    System.out.printf("%s isn't a direcotry\n", path);
                    System.out.println("System exit 1");
                    System.exit(1);
                }
                // checkpoint #3: check if path readable by server
                if (file.canRead()) {
                    System.out.println(path + " can be read");
                } else {
                    System.out.printf("%s can't be read\n", path);
                    System.out.println("System exit 1");
                    System.exit(1);
                }
            }

            // create a thread pool with 3 threads
            ExecutorService threadPool = Executors.newFixedThreadPool(3);
            ServerSocket server = new ServerSocket(port);

            while (true) {
                // listen on the port
                System.out.println("Waiting for incoming connection");
                Socket socket = server.accept();
                System.out.println("Connected");

                HttpClientConnection hcc = new HttpClientConnection(socket, dir);
                // submit the thread to threadpool
                threadPool.submit(hcc);
                System.out.println("Submitted to threadpool");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
