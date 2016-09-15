package com.bosch.peoplecounter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import com.bosch.peoplecounter.data.Person;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import rx.Observable;
import rx.schedulers.Schedulers;

public class Utils {
  public static final String[] EXCEL_MIME_TYPES = new String[] {
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      "application/vnd.ms-excel", "application/octet-stream"
  };
  static final int CODE_FILE_PICKER = 1;

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
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      intent.setType("*/*");
      intent.putExtra(Intent.EXTRA_MIME_TYPES, types);
    } else {
      StringBuilder sb = new StringBuilder();
      for (final String type : types) {
        sb.append(type);
        sb.append(",");
      }
      sb.deleteCharAt(sb.length() - 1);
      intent.setType(sb.toString());
    }
    intent.setAction(Intent.ACTION_GET_CONTENT);
    activity.startActivityForResult(Intent.createChooser(intent, "Choose file"),
        CODE_FILE_PICKER);
  }

  static Observable<Person> parseExcel(Uri uri) {
    Observable<Person> objectObservable = Observable.create((subscriber) -> {
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
      }
    });

    objectObservable.subscribeOn(Schedulers.newThread());

    return objectObservable;
  }

  private static Person parseRow(final Row row) {
    Person person = new Person();

    try {
      Iterator<Cell> iterator = row.iterator();
      iterator.next(); // ignore No.
      iterator.next(); // ignore Employee code

      // name
      String name = convertAndGetStringCellValue(iterator.next());
      person.setName(name);

      iterator.next(); // who cares about department

      String group = convertAndGetStringCellValue(iterator.next());
      person.setGroup(group);

      String genderString = convertAndGetStringCellValue(iterator.next());
      person.setIsMale(genderString.equalsIgnoreCase("M"));

      iterator.next();
      iterator.next();
      iterator.next(); // ignore direct and indirect manager, and also the T-Shirt size

      String room = convertAndGetStringCellValue(iterator.next());
      person.setRoom(room);

      String hotel = convertAndGetStringCellValue(iterator.next());
      person.setHotel(hotel);

      // Now, skip bus and the coordinator
      iterator.next();
      iterator.next();

      // This is last column, and maybe they didn't update the phone number for me to parse
      String phone = convertAndGetStringCellValue(iterator.next());
      person.setPhoneNumber(phone);
    } catch (Exception ex) {
      // ignore, we try to get at most data as possible,
      // at least, we have the person name. That is enough to show on the GUI.
    }

    return person;
  }

  private static String convertAndGetStringCellValue(final Cell cell) {
    if (cell == null) {
      return "";
    }

    cell.setCellType(Cell.CELL_TYPE_STRING);
    String stringCellValue = cell.getStringCellValue();
    if (stringCellValue == null) return "";

    return stringCellValue.trim();
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
