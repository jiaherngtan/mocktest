package vtp2022.dayx.mocktest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class HttpClientConnection implements Runnable {

    private Socket socket;
    private ArrayList<String> directory;

    public HttpClientConnection(Socket s, ArrayList<String> d) {
        socket = s;
        directory = d;
    }

    @Override
    // Runnable has only one method to implement: public void run()
    // this is where you put the job the thread is supposed to run
    public void run() {
        System.out.println("Client thread started");

        try {
            // from client
            InputStream is = socket.getInputStream();
            // html
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            // TODO png?

            // to client
            OutputStream os = socket.getOutputStream();

            while (true) {
                // read from client (browser)
                String req = br.readLine(); // eg. GET /index.html HTTP/1.1
                System.out.printf("Request from client: %s\n", req);
                // break the first line by <space> and store in String array
                // index 0 = request method (GET / POST / PUT / DELETE...)
                // index 1 = name of the resource
                // index 2 = HTTP protocol's version number
                String[] reqArr = req.split(" ");
                for (String item : reqArr)
                    System.out.println(item);

                // Action 1: check if it is a GET method
                // if not, send the response back to client by os.write()
                if (!reqArr[0].equals("GET")) {
                    os.write("HTTP/1.1 405 Method Not Allowed\r\n".getBytes());
                    os.write("\r\n".getBytes());
                    os.write((reqArr[0] + " not supported\r\n").getBytes());
                    break;
                }

                // Action 2: check if resource exists
                String resource = reqArr[1];
                String resourcePath = "";
                boolean resourceExists = false;
                // if not provided, set as /index.html
                if (resource.equals("/")) {
                    resource = "/index.html";
                }

                // =====CHECKPOINTS=====
                System.out.println("RESOURCE: " + resource);
                // System.out.println("DIRECTORY" + directory);
                // =====CHECKPOINTS=====

                // loop through each directory to check for match
                for (int i = 0; i < directory.size(); i++) {
                    // pass each directory into a File object
                    // then list all the items in that directory
                    File file = new File(directory.get(i));
                    String[] fileList = file.list();

                    // =====CHECKPOINTS=====
                    System.out.println(Arrays.toString(fileList));
                    // =====CHECKPOINTS=====

                    // if exists, print out result and proceed
                    for (int j = 0; j < fileList.length; j++) {
                        String resCompare = resource.replace("/", "");
                        System.out.println(fileList[j]);
                        if (resCompare.equals(fileList[j])) {
                            System.out.println("Resource exists");
                            resourceExists = true;
                            resourcePath = directory.get(i) + resource;
                            System.out.println("RESOURCE PATH: " + resourcePath);
                            break;
                        }
                    }
                }
                // if does not exists, send the response back to client by os.write()
                if (resourceExists == false) {
                    System.out.println("resource doesn't exists");
                    os.write("HTTP/1.1 404 Not Found\r\n".getBytes());
                    os.write("\r\n".getBytes());
                    os.write((resource + " not found\r\n").getBytes());
                    break;
                }

                // Action 3 & 4
                String[] resourcePathArr = resourcePath.replace(".", " ")
                        .split(" ");

                // =====CHECKPOINTS=====
                // System.out.println(resourcePath);
                // System.out.println(Arrays.toString(resourcePathArr));
                // =====CHECKPOINTS=====

                // check for resource extension
                String ext = resourcePathArr[resourcePathArr.length - 1];

                // =====CHECKPOINTS=====
                // System.out.println("EXT: " + ext);
                // =====CHECKPOINTS=====

                if (ext.equals("html") || ext.equals("css")) {
                    File file = new File(resourcePath);
                    Scanner scanner = new Scanner(file);
                    String content = scanner.useDelimiter("\\Z").next();
                    scanner.close();
                    // System.out.println(content);
                    os.write("HTTP/1.1 200 OK\r\n".getBytes());
                    os.write("\r\n".getBytes());
                    os.write(content.getBytes());
                    os.flush();
                    break;
                } else if (ext.equals("png")) {
                    FileInputStream img = new FileInputStream(resourcePath);
                    os.write("HTTP/1.1 200 OK\r\n".getBytes());
                    os.write("Content-Type: image/png\r\n".getBytes());
                    os.write("\r\n".getBytes());
                    os.write(img.readAllBytes());
                    os.flush();
                    break;
                }
            }
            is.close();
            os.close();
            socket.close();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
