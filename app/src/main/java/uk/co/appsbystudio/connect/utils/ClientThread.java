package uk.co.appsbystudio.connect.utils;

import android.content.Context;
import android.content.Intent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientThread implements Runnable {

    private Context context;
    private String address;
    private int port;

    public boolean isConnected = false;

    private Socket socket;
    private DataOutputStream outputStream;

    public ClientThread(Context context, String address, int port) {
        this.context = context;
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
                String result = byteArrayOutputStream.toString("UTF-8");

                if (result.equals("You have connected to the server!")) {
                    isConnected = true;
                    Intent intent = new Intent();
                    intent.setAction("socket.state");
                    intent.putExtra("connection", true);
                    context.sendBroadcast(intent);
                }
                System.out.println(result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            byte[] data = message.getBytes("UTF-8");
            outputStream.writeInt(data.length);
            outputStream.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openConnection(String address, int port) {
        this.address = address;
        this.port = port;

        this.run();
    }

    public void closeConnection() {
        try {
            if (socket != null) {
                socket.close();
                outputStream.close();

                isConnected = false;

                Intent intent = new Intent();
                intent.setAction("socket.state");
                intent.putExtra("connection", false);
                context.sendBroadcast(intent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
