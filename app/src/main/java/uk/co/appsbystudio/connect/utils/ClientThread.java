package uk.co.appsbystudio.connect.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import uk.co.appsbystudio.connect.data.models.ContactsModel;

public class ClientThread implements Runnable {

    private Context context;
    private String address;
    private int port;

    public boolean isConnected = false;

    private ArrayList<String> message = new ArrayList<>();
    private ArrayList<ContactsModel> contactsArray = new ArrayList<>();

    private Socket socket;
    private DataOutputStream outputStream;
    private ObjectOutputStream objectOutputStream;

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
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            while ((byteRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, byteRead);
                String result = byteArrayOutputStream.toString("UTF-8");

                sendMessage("Hello, Server!");
                new ContactsSync().execute();
                //new LastMessageSync().execute();

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

    private void sendMessage(String message) {
        try {
            byte[] data = message.getBytes("UTF-8");
            outputStream.writeInt(data.length);
            outputStream.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendArray(ArrayList object) throws IOException {
        objectOutputStream.writeObject(object);
    }

    public void closeConnection() {
        try {
            if (socket != null) {
                socket.close();
                outputStream.close();
                objectOutputStream.close();

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

    private class ContactsSync extends AsyncTask<Void, Void, Void> {

        private Cursor cursor;
        private Cursor cursorDetails;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int indexId = 0;
            int indexName = 0;

            if (cursor != null) {
                indexId = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                indexName = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            }

            if (indexId < 0 || cursor != null && !cursor.moveToNext()) return null;

            System.out.println("Contacts Sync");

            do {
                String id = cursor.getString(indexId);
                String name = cursor.getString(indexName);

                cursorDetails = context.getContentResolver()
                        .query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{cursor.getString(indexId)},
                                null);

                int indexNumber = 0;

                if (cursorDetails != null) {
                    indexNumber = cursorDetails.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                }

                if (indexNumber < 0 || cursorDetails != null && !cursorDetails.moveToNext()) return null;

                do {
                    assert cursorDetails != null;
                    String number = cursorDetails.getString(indexNumber);

                    ContactsModel contactsModel = new ContactsModel(
                            id,
                            name,
                            number
                    );

                    contactsArray.add(contactsModel);

                } while (cursorDetails.moveToNext());
            } while (cursor.moveToNext());

            try {
                sendArray(contactsArray);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            cursor.close();
            cursorDetails.close();
        }
    }

    private class LastMessageSync extends AsyncTask<Void, Void, Void> {

        private Cursor cursor;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Uri uri = Uri.parse("content://sms");
            cursor = context.getContentResolver().query(uri, null, null, null, null);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int indexAddress = 0;
            int indexBody = 0;
            int indexDate = 0;
            int indexRead = 0;
            int indexPerson = 0;

            if (cursor != null) {
                indexAddress = cursor.getColumnIndex("address");
                indexBody = cursor.getColumnIndex("body");
                indexDate = cursor.getColumnIndex("date");
                indexRead = cursor.getColumnIndex("read");
                indexPerson = cursor.getColumnIndex("type");
            }

            if (indexBody < 0 || cursor != null && !cursor.moveToFirst()) return null;

            do {
                assert cursor != null;

                String number = cursor.getString(indexAddress);

                message.add(number);
            } while (cursor.moveToNext());

            try {
                sendArray(message);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            cursor.close();
        }
    }
}
