package uk.co.appsbystudio.connect.server;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerConnect extends AsyncTask<Void, Void, Void>{

    private String address;
    private int port;
    private String result;

    private Socket socket;
    private OutputStream outputStream;
    private PrintStream printStream;

    private byte[] buffer = new byte[65000];

    public ServerConnect(String address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            socket = new Socket(address, port);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];
            int byteRead;
            InputStream inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            printStream = new PrintStream(outputStream);

            while ((byteRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, byteRead);
                result = byteArrayOutputStream.toString("UTF-8");
                System.out.println(result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void sendMessage(String message) {
        try {
            //buffer = message.getBytes();
            printStream.print(message);
            printStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        System.out.println(result);
        super.onPostExecute(aVoid);
    }
}
