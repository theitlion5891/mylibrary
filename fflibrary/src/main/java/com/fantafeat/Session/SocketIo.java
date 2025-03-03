package com.fantafeat.Session;

import android.content.Context;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class SocketIo {

    Context mContext;
    Socket mSocket;
    private static final String TAG = "SocketIo";


    public void connect(Context mContext){
        this.mContext = mContext;
        try {
            IO.Options options = new IO.Options();

            options.timeout = 60 * 1000;
            options.reconnection = true;
            options.forceNew = true;

            //mSocket = IO.socket("http://192.168.0.109:8080/",options);
            //mSocket = IO.socket("http://192.168.0.109:8080/",options);
            mSocket = IO.socket("http://13.232.101.168:8080/",options);
            mSocket = getSoket();

            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }



    }

    public Socket getSoket() {
        if(mSocket !=null && mSocket.connected()) {
            return mSocket;
        }else{
            return mSocket.connect();
        }
    }



    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "call: on Connect " );
        }
    };


    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "call: onDisconnect " + args[0].toString() );
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "call: ConnectionError"  + args[0].toString());
        }
    };
}
