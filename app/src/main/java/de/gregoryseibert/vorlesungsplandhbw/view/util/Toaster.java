package de.gregoryseibert.vorlesungsplandhbw.view.util;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Gregory Seibert on 23.01.2018.
 */

public abstract class Toaster {
    public static void toast(final Activity activity, final String toast) {
        toast(activity, toast, false);
    }

    public static void toast(final Activity activity, final String toast, boolean longMessage) {
        activity.runOnUiThread(() -> Toast.makeText(activity.getApplicationContext(), toast, (longMessage ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)).show());
    }
}
