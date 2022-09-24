package com.example.ToFix;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.StringTokenizer;

public class ReceiveSms extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "sms received", Toast.LENGTH_SHORT).show();
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg_from;
            if(bundle != null){
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0 ; i< msgs.length ; i++)
                    {
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        StringTokenizer st = new StringTokenizer(msgBody," ");
                        //Toast.makeText(context, msgBody, Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(context,LocationScreen.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.putExtra("lat",Double.parseDouble(st.nextToken()));
                        intent1.putExtra("long",Double.parseDouble(st.nextToken()));
                        context.startActivity(intent1);

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
