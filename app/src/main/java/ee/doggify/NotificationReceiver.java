package ee.doggify;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Date;

import ee.doggify.Models.Receipt;

/**
 * Created by Risto on 2/22/2016.
 */
public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Get the extras
        Bundle bundle = intent.getExtras();
        int id = bundle.getInt("notificationId");
        String companyName = bundle.getString("company");
        double price = bundle.getDouble("price");
        Date date = (Date) intent.getSerializableExtra("date");

        //Create the receipt
        Receipt receipt = new Receipt(companyName, price, date);

        //Set the receipt's usefulness according to notification's button pressed
        if (intent.getAction().equals("ee.doggify.decline")) {
            receipt.setUseful(false);
            Log.d("Decline","Decline event");
        } else if (intent.getAction().equals("ee.doggify.accept")) {
            receipt.setUseful(true);
            Log.d("Decline", "Accept event");
        }

        //Insert the receipt into DB
        DBHelper dbHelper = new DBHelper(context);
        dbHelper.insertIntoDB(receipt);

        //Close the notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(id);

        MainActivity.getInstace().showReceipts();
    }
}
