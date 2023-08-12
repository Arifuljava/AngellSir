package com.grozziie.grozziie_pdf.BlueTooth;;

import android.bluetooth.BluetoothSocket;

public class FirstEvent {
    private BluetoothSocket socket_M;
    public FirstEvent(BluetoothSocket socket1){
        socket_M=socket1;
    }
    public BluetoothSocket getSocket_M(){
        return  socket_M;
    }
}
