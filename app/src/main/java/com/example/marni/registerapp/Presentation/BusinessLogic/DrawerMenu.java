package com.example.marni.registerapp.Presentation.BusinessLogic;

/**
 * Created by Wallaard on 30-5-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.auth0.android.jwt.JWT;
import com.example.marni.registerapp.Presentation.Presentation.Activities.AssortmentActivity;
import com.example.marni.registerapp.Presentation.Presentation.Activities.LogInActivity;
import com.example.marni.registerapp.Presentation.Presentation.Activities.RegisterHistoryActivity;
import com.example.marni.registerapp.R;

public class DrawerMenu {

    private final String TAG = getClass().getSimpleName();

    private Context context;
    private Intent intent;

    public DrawerMenu(Context context, int id) {
        this.context = context;

        switch (id) {
            case R.id.nav_order_history:
                intent = new Intent(context, RegisterHistoryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;

            case R.id.nav_assortment:
                intent = new Intent(context, AssortmentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;

            case R.id.nav_logout:
                intent = new Intent(context, LogInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
        }
    }
}
