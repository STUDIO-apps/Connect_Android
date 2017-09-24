package uk.co.appsbystudio.connect.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class ClientThread implements Runnable {

    private String address;
    private int port;
    private String result;

    private Socket socket;
    DataOutputStream outputStream;

    private byte[] buffer = new byte[65000];

    public ClientThread(String address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {

        try {
            socket = new Socket(address, port);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];
            int byteRead;
            InputStream inputStream = socket.getInputStream();
            outputStream = new DataOutputStream(socket.getOutputStream());

            while ((byteRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, byteRead);
                result = byteArrayOutputStream.toString("UTF-8");
                System.out.println(result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            //OutputStream outputStream = socket.getOutputStream();
            //PrintStream printStream = new PrintStream(outputStream);
            //printStream.print(message);
            //outputStream.writeChars(message);
            //outputStream.writeUTF(message);
            //outputStream.flush();
            byte[] data = message.getBytes("UTF-8");
            outputStream.writeInt(data.length);
            outputStream.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
