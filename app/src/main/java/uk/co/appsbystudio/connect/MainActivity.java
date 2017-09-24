package uk.co.appsbystudio.connect;

import android.app.Activity;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import uk.co.appsbystudio.connect.server.ClientThread;
import uk.co.appsbystudio.connect.server.ServerConnect;

public class MainActivity extends Activity {

    ClientThread clientThread;
    Thread thread;
    ServerConnect serverConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        final EditText editTextPort = (EditText) findViewById(R.id.editTextPort);

        findViewById(R.id.connectButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //serverConnect = new ServerConnect(editTextAddress.getText().toString(), Integer.valueOf(String.valueOf(editTextPort.getText())));
                //serverConnect.execute();
                if (thread == null || !thread.isAlive()) {
                    clientThread = new ClientThread(editTextAddress.getText().toString(), Integer.valueOf(String.valueOf(editTextPort.getText())));
                    thread = new Thread(clientThread);
                    thread.start();
                } else System.out.println("Already connected!");
            }
        });

        findViewById(R.id.messageSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        clientThread.sendMessage("This should work!");
                    }
                });
                thread.start();
            }
        });

        findViewById(R.id.disconnectButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientThread.closeConnection();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (thread != null) {
            clientThread.closeConnection();
        }
    }
}
