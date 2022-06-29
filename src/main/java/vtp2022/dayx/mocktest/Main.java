package vtp2022.dayx.mocktest;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // set default port
        int port = 3000;
        // ArrayList to store a list of directories
        ArrayList<String> dir = new ArrayList<String>();
        // check for --docRoot argument, default set as false
        boolean docRootExist = false;
        // get user arguments in command line
        try {
            for (int i = 0; i < args.length; i++) {
                // if --port argument present, update port
                if (args[i].equals("--port")) {
                    port = Integer.parseInt(args[i + 1]);
                }
                // if --docRoot argument present,
                // seperate by ":" and store in dir
                if (args[i].equals("--docRoot")) {
                    String[] argsList = args[i + 1].split(":");
                    for (String item : argsList) {
                        dir.add(item);
                    }
                    docRootExist = true;
                }
            }
            // if -docRoot argument not present, default set as ./static
            if (docRootExist == false) {
                dir.add("./static");
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Please provide a port number or directory else it will be set as default");
        }

        // =====CHECKPOINTS=====
        // System.out.println(port);
        // System.out.println(dir);
        // =====CHECKPOINTS=====

        // instantiate the server class, pass in the port and dir
        HttpServer httpServer = new HttpServer(port, dir);
        // invoke the server class go() method
        httpServer.go();
    }
}

// list of command line args check available in README.md
