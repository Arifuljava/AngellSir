package com.grozziie.grozziie_pdf;;

import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;

public class BitmapEventBus {
    private Bitmap[] IntentBitmap;
    public BitmapEventBus(Bitmap[] bitmaps){
        IntentBitmap=bitmaps;
    }
    public Bitmap[] getIntentBitmap(){
        return  IntentBitmap;
    }
}
