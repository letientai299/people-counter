package com.bosch.peoplecounter;

import android.content.Context;
import android.support.v7.app.AlertDialog;

public class Utils {
  public static void askForDoSomething(Context context, String message,
      Runnable action) {

    new AlertDialog.Builder(context).setTitle("Pay attention")
        .setMessage(message)
        .setPositiveButton("Yes", (v, vw) -> {
          action.run();
        })
        .setNegativeButton("Not really", null)
        .show();
  }
}
