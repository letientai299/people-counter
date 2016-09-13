package com.bosch.peoplecounter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import com.bosch.peoplecounter.data.Person;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import rx.Observable;

public class Utils {
  static final int CODE_FILE_PICKER = 1;
  public static final String[] EXCEL_MIME_TYPES = new String[] {
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      "application/vnd.ms-excel", "application/octet-stream"
  };

  public static void askForDoSomething(Context context, String message,
      Runnable action) {

    new AlertDialog.Builder(context).setTitle("Pay attention")
        .setMessage(message)
        .setPositiveButton("Yes", (v, vw) -> action.run())
        .setNegativeButton("Not really", null)
        .show();
  }

  /**
   * Start intent to pick a file from file system, given MIME types.
   */
  public static void startFilePickerIntent(Activity activity, String[] types) {
    Intent intent = new Intent();
    intent.setType("*/*");
    intent.putExtra(Intent.EXTRA_MIME_TYPES, types);
    intent.setAction(Intent.ACTION_GET_CONTENT);
    activity.startActivityForResult(Intent.createChooser(intent, "Choose file"),
        CODE_FILE_PICKER);
  }

  static Observable<Person> parseExcel(Uri uri) {
    return Observable.create((subscriber) -> {
      Workbook wb = null;
      try {
        wb = createWorkbook(uri);
        Iterator<Row> iterator = wb.getSheetAt(0).iterator();

        // ignore header row
        iterator.next();

        // Assume that the format is ok
        while (iterator.hasNext()) {
          Row row = iterator.next();
          Person p = parseRow(row);
          subscriber.onNext(p);
        }

        subscriber.onCompleted();
      } catch (IOException ioe) {
        subscriber.onError(ioe);
      } finally {
        if (wb != null) {
          try {
            wb.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    });
  }

  private static Person parseRow(final Row row) {
    String name = row.getCell(2).getStringCellValue();
    return new Person(null, false, name, "");
  }

  private static Workbook createWorkbook(final Uri uri) throws IOException {
    String path = uri.getPath();
    InputStream inputStream = new FileInputStream(path);
    if (path.endsWith("xlsx")) {
      return new XSSFWorkbook(inputStream);
    } else {
      return new HSSFWorkbook(inputStream);
    }
  }

  public static void recreateFragment(Fragment fragment) {
    FragmentActivity activity = fragment.getActivity();
    activity.getSupportFragmentManager()
        .beginTransaction()
        .detach(fragment)
        .attach(fragment)
        .commitAllowingStateLoss();
  }
}
