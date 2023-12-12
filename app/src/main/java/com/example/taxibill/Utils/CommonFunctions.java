package com.example.taxibill.Utils;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.taxibill.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CommonFunctions {

    public static Intent createActivityIntent(Activity activity, Class<? extends Activity> aClass, Integer flags, Bundle bundle) {
        Intent intent = new Intent(activity, aClass);
        if (flags != null) {
            intent.setFlags(flags);
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        return intent;
    }
    public static boolean CameracheckPermissionsGranted(String[] permissions) {
        boolean granted = true;
        for (String permission : permissions) {
            if (isPermissionDenied(App.getInstance(), permission)) {
                granted = false;
            }
        }
        return granted;
    }



    public static void showDatePickerMinMaxDateRent(Activity activity, String defaultDate, String minDate, String maxDate, final String tag, final MyDateSelectedListener myDateSelectedListener) {


        Calendar calendar = Calendar.getInstance();
        /*try {*/
        if (!defaultDate.equals("") && !defaultDate.equalsIgnoreCase("0000-00-00")) {
            String[] date = defaultDate.split("-");

           /* calendar.set(Calendar.YEAR, Integer.parseInt(date[0]));
            calendar.set(Calendar.MONTH, Integer.parseInt(date[1]));
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));*/
            try {
                calendar.setTime(DataFormats.dateFormatDbDate.parse(defaultDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);
                myDateSelectedListener.onDateSet(tag, calendar, calendar.getTime());
            }
        };
        DatePickerDialog aDatePickerDialog = new DatePickerDialog(
                activity, onDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDateandTime = sdf.format(new Date());

            //minDate =currentDateandTime;
            if (!minDate.equals("") && !minDate.equalsIgnoreCase("0000-00-00")) {
                Log.e("minDatee",minDate);


                long dateMin = DataFormats.dateFormatDbDate.parse(currentDateandTime).getTime();
                aDatePickerDialog.getDatePicker().setMinDate(dateMin);

            }
           /* if (!maxDate.equals("") && !maxDate.equalsIgnoreCase("0000-00-00")) {
                long dateMax = DataFormats.dateFormatDbDate.parse(maxDate).getTime();
                aDatePickerDialog.getDatePicker().setMaxDate(dateMax);
            }*/
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DialogUtils.setButtonTextColor(activity, aDatePickerDialog);
        ArrayList<View> views = aDatePickerDialog.getDatePicker().getTouchables();
        if (views != null && views.size() > 0)
            aDatePickerDialog.getDatePicker().getTouchables().get(0).performClick();
        aDatePickerDialog.show();
        /*} catch (ParseException e) {
            Log.i("catchcc", e.getMessage());
            e.printStackTrace();
        }*/

    }



    public static void hideKeyboardFrom(Context context, View view) {
        view.clearFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static <T> void setJsonArrayGson(JSONArray array, List<T> list, Class<T> tClass, RecyclerView.Adapter<?> adapter) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            T t = App.gson.fromJson(array.getJSONObject(i).toString(), tClass);
            list.add(t);
        }
        adapter.notifyDataSetChanged();
    }

    public static boolean checkAutoTimeEnabled() {
        return Settings.Global.getInt(App.getInstance().getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;

    }

    public static void hideKeyboardFrom(View view) {
        view.clearFocus();
        InputMethodManager imm = (InputMethodManager) App.getInstance().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isLastItemDisplayingLinear(RecyclerView recyclerView) {
        if (recyclerView.getAdapter() != null && recyclerView.getAdapter().getItemCount() != 0) {

            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            return lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1 && recyclerView.getLayoutManager().canScrollVertically();
        }
        return false;
    }

    public static boolean is1stItemDisplayingLinear(RecyclerView recyclerView) {
        if (recyclerView.getAdapter() != null && recyclerView.getAdapter().getItemCount() != 0) {

            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            return lastVisibleItemPosition == 0 /*&& recyclerView.getLayoutManager().canScrollVertically()*/;
        }
        return false;
    }

    public static int get1stItemPosition(RecyclerView recyclerView) {
        if (recyclerView.getAdapter() != null && recyclerView.getAdapter().getItemCount() != 0) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (linearLayoutManager != null)
                return ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        }
        return -1;
    }

    public static void openDialPad(Activity activity, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        activity.startActivity(intent);
    }

    public static boolean isLastItemDisplayingGrid(RecyclerView recyclerView) {
        if (recyclerView.getAdapter() != null && recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            return lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1;
        }
        return false;
    }

    public static String getDeviceId(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
            return UUID.randomUUID().toString();
        }
    }


    public static boolean checkPermissionsGranted(Context context, String[] permissions) {
        boolean granted = true;
        for (String permission : permissions) {
            if (isPermissionDenied(context, permission)) {
                granted = false;
            }
        }
        return granted;
    }

    public static boolean checkPermissionsGranted(String[] permissions) {
        boolean granted = true;
        for (String permission : permissions) {
            if (isPermissionDenied(App.getInstance(), permission)) {
                granted = false;
            }
        }
        return granted;
    }


    public static boolean isPermissionDenied(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkPermissionsGrantedRes(int[] permissions) {
        boolean granted = true;
        for (int permission : permissions) {
            if (permission != PackageManager.PERMISSION_GRANTED) {
                granted = false;
                break;
            }
        }
        return granted;
    }

    public static boolean checkPermissionsRationale(Activity activity, Fragment fragment, String[] permissions) {
        boolean granted = true;
        for (String permission : permissions) {
            if (isPermissionDenied(activity, permission)) {
                if (fragment != null) {
                    Log.i("PermissionRationale", String.valueOf(fragment.shouldShowRequestPermissionRationale(permission)));
                    if (!fragment.shouldShowRequestPermissionRationale(permission)) {
                        granted = false;
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!activity.shouldShowRequestPermissionRationale(permission)) {
                            granted = false;
                        }
                    }
                }
            }
        }
        Log.i("PermissionRationale", String.valueOf(granted));
        return granted;
    }

    public static boolean getMessage(Activity activity, Fragment fragment, String[] permissions) {
        boolean granted = true;
        for (String permission : permissions) {
            if (isPermissionDenied(activity, permission)) {
                if (fragment != null) {
                    Log.i("PermissionRationale", String.valueOf(fragment.shouldShowRequestPermissionRationale(permission)));
                    if (!fragment.shouldShowRequestPermissionRationale(permission)) {
                        granted = false;
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!activity.shouldShowRequestPermissionRationale(permission)) {
                            granted = false;
                        }
                    }
                }
            }
        }
        Log.i("PermissionRationale", String.valueOf(granted));
        return granted;
    }

    public static void askPermission(Activity activity, Fragment fragment, String[] permissions, int code) {

        if (!checkPermissionsRationale(activity, fragment, permissions)) {
            gotoPermission(activity, fragment, code);
        } else {
            if (fragment != null) {
                fragment.requestPermissions(permissions, code);
            } else {
                ActivityCompat.requestPermissions(activity, permissions, code);
            }
        }

    }

    public static void askPermission1st(Activity activity, Fragment fragment, String[] permissions, int code) {
        if (fragment != null) {
            fragment.requestPermissions(permissions, code);
        } else {
            ActivityCompat.requestPermissions(activity, permissions, code);
        }
    }

    public static void setSuccessResult(Activity activity, Intent intent) {
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    public static Intent getActivityResultIntent(Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        return intent;
    }

    public static List<JSONObject> getJSONObjectsList(JSONArray array) {

        List<JSONObject> list = new ArrayList<>();
        list.clear();
        try {
            for (int i = 0; i < array.length(); i++) {

                JSONObject object = array.getJSONObject(i);

                list.add(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void sendMail(Activity activity, String[] email, String subject, String body) {
        Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
        selectorIntent.setData(Uri.parse("mailto:"));
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setData(Uri.parse("mailto:")); // only email apps should h&&le this
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.setSelector(selectorIntent);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, "No apps available", Toast.LENGTH_SHORT).show();
        }
    }


    public static View getItemView(ViewGroup viewGroup, int resource) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);
    }

    public static void setJSONValue(TextView textView, JSONObject object, String key) throws JSONException {

        textView.setText(object.getString(key));
    }

    public static void setDrawable(Context context, TextView textView, int icon, String direction) {
        if (direction != null && !direction.equalsIgnoreCase("")) {
            Drawable drawable = ContextCompat.getDrawable(context, icon);

            switch (direction) {
                case "r":
                    textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                    break;
                case "l":
                    textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    break;
                case "t":
                    textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                    break;
                case "b":
                    textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
                    break;
                default:
                    textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    break;
            }
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    public static void setDrawable(TextView textView, int icon, String direction) {
        if (direction != null && !direction.equalsIgnoreCase("")) {
            Drawable drawable = ContextCompat.getDrawable(App.getInstance(), icon);

            switch (direction) {
                case "r":
                    textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                    break;
                case "l":
                    textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    break;
                case "t":
                    textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                    break;
                case "b":
                    textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
                    break;
                default:
                    textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    break;
            }
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    public static void loadImage(Context context, ImageView imageView, Object src, int res) {
        if (src instanceof Integer) {
            if (res == 0) {
                Glide.with(context).load((int) src).into(imageView);
            } else {
                Glide.with(context).load((int) src).override(res, res).into(imageView);
            }
        } else if (src instanceof String) {
            if (res == 0) {
                Glide.with(context).load(src).into(imageView);
            } else {
                Glide.with(context).load(src).override(res, res).into(imageView);
            }
        }
    }

    public static File getOutputMediaFile(String type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        /*File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");*/
        /*String extr = Environment.getExternalStoragePublicDirectory(type).toString();
        File mediaStorageDir = new File(extr + FOLDER_NAME);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }*/
        File mediaStorageDir = App.getInstance().getExternalFilesDir(type);

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type.equalsIgnoreCase(Environment.DIRECTORY_PICTURES)) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type.equalsIgnoreCase(Environment.DIRECTORY_MOVIES)) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static void hideSystemUI(Activity activity, boolean hasFocus) {

        if (hasFocus) {
            // Enables regular immersive mode.
            // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
            // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            // Set the content to appear under the system bars so that the
                            // content doesn't resize when the system bars hide and show.
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    public static void showSystemUI(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public static void openActivity(Activity activity, Bundle bundle, Class c) {
        Intent intent = new Intent(activity, c);
        activity.startActivity(intent, bundle);
    }



    public void setBackButton(final Activity activity, ImageView imageViewBack) {
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity, "Back Pressed", Toast.LENGTH_SHORT).show();
                activity.onBackPressed();
            }
        });
    }

  /*  public static void storeLoginPhone(Activity activity, String phone) {
        MyPrefs myPrefs = ((App) activity.getApplication()).myPrefs;
        myPrefs.putBoolean(UserData.KEY_LOGGED_IN, true);

    }*/

    public static void clearError(Editable editable, EditText editText) {
        if (editable.equals(editText.getText())) {
            editText.setError(null);
        }
    }

    public static void setJSONArray(JSONObject object, String arrayName, List<JSONObject> list, RecyclerView.Adapter adapter) throws JSONException {
        JSONArray arrayExp = object.getJSONArray(arrayName);
        list.addAll(getJSONObjectsList(arrayExp));
        adapter.notifyDataSetChanged();
    }

    public static String get1stNChars(String text, int n) {
        if (text.length() > n) {
            return text.substring(0, n);
        }
        return text;
    }

    public static String getConcatenatedValue(int i, int size, String valueToConcat, String seperator) {
        if (i == size - 1) {
            return valueToConcat;

        } else {
            return valueToConcat + seperator;
        }
    }

    public static String[] getFormattedCardNumSplit(String cardNum) {

        String cardNum1 = "", cardNum2 = "", cardNum3 = "", cardNum4 = "";

        if (cardNum.length() == 13) {
            for (int i = 0; i < 4; i++) {
                cardNum1 += cardNum.charAt(i);
            }


            for (int i = 4; i < 9; i++) {
                cardNum2 += cardNum.charAt(i);

            }

            for (int i = 9; i < cardNum.length(); i++) {
                cardNum3 += cardNum.charAt(i);
            }
        }

        if (cardNum.length() == 14) {
            for (int i = 0; i < 4; i++) {
                cardNum1 += cardNum.charAt(i);
            }


            for (int i = 4; i < 10; i++) {
                cardNum2 += cardNum.charAt(i);

            }

            for (int i = 10; i < cardNum.length(); i++) {
                cardNum3 += cardNum.charAt(i);
            }
        }
        if (cardNum.length() == 15) {
            for (int i = 0; i < 5; i++) {
                cardNum1 += cardNum.charAt(i);
            }


            for (int i = 5; i < 10; i++) {
                cardNum2 += cardNum.charAt(i);

            }

            for (int i = 10; i < cardNum.length(); i++) {
                cardNum3 += cardNum.charAt(i);
            }
        }
        if (cardNum.length() == 16) {
            for (int i = 0; i < 4; i++) {
                cardNum1 += cardNum.charAt(i);
            }


            for (int i = 4; i < 8; i++) {
                cardNum2 += cardNum.charAt(i);

            }

            for (int i = 8; i < 12; i++) {
                cardNum3 += cardNum.charAt(i);
            }

            for (int i = 12; i < 16; i++) {
                cardNum4 += cardNum.charAt(i);
            }
        }
        return new String[]{cardNum1, cardNum2, cardNum3, cardNum4};
    }


    public static String getFormattedCardNumX(String cardNum) {

        String formattedCardNum = "";

        if (cardNum.length() == 13) {

            for (int i = 0; i < 4; i++) {
                formattedCardNum += cardNum.charAt(i);
            }
            formattedCardNum += " ";

            formattedCardNum += "XXXXX";
            formattedCardNum += " ";

            for (int i = 9; i < cardNum.length(); i++) {
                formattedCardNum += cardNum.charAt(i);
            }

        }

        if (cardNum.length() == 14) {
            formattedCardNum = "";

            for (int i = 0; i < 4; i++) {
                formattedCardNum += cardNum.charAt(i);
            }
            formattedCardNum += " ";

            formattedCardNum += "XXXXXX";
            formattedCardNum += " ";

            for (int i = 10; i < cardNum.length(); i++) {
                formattedCardNum += cardNum.charAt(i);
            }

            return formattedCardNum;
        }
        if (cardNum.length() == 15) {
            formattedCardNum = "";

            for (int i = 0; i < 5; i++) {
                formattedCardNum += cardNum.charAt(i);
            }
            formattedCardNum += " ";


            formattedCardNum += "XXXXX";
            formattedCardNum += " ";

            for (int i = 10; i < cardNum.length(); i++) {
                formattedCardNum += cardNum.charAt(i);
            }
            return formattedCardNum;
        }

        if (cardNum.length() == 16) {
            formattedCardNum = "";

            for (int i = 0; i < 4; i++) {
                formattedCardNum += cardNum.charAt(i);
            }
            formattedCardNum += " ";

            formattedCardNum += "XXXX";
            formattedCardNum += " ";

            formattedCardNum += "XXXX";
            formattedCardNum += " ";

            for (int i = 12; i < cardNum.length(); i++) {
                formattedCardNum += cardNum.charAt(i);
            }
            return formattedCardNum;
        }
        return cardNum;
    }

    public static void gotoPermission(Activity activity, Fragment fragment, int code) {

        Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + activity.getPackageName()));
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        //i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        if (fragment != null) {
            fragment.startActivityForResult(i, code);
        } else {
            activity.startActivityForResult(i, code);
        }
    }


    public static void gotoPermissionNew(Activity activity, ActivityResultLauncher<Intent> launcher) {

        Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + activity.getPackageName()));
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        //i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        launcher.launch(i);
    }

    public static void EsplitOpenFile(Activity activity, String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Log.i("File", path);

        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        String extension = path.substring(path.lastIndexOf(".") + 1);
        String mimeType = myMime.getMimeTypeFromExtension(extension);

        Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", new File(path));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, mimeType);
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

/*    public static String getDownloadDir() {
        String extr = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        File coasFolder = new File(extr + FOLDER_NAME);

        if (!coasFolder.exists()) {

            coasFolder.mkdir();
        }
        return coasFolder.toString();
    }*/


    public static void openFile(Activity activity, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String path = file.getPath();
        Log.i("File", path);

        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        String extension = path.substring(path.lastIndexOf(".") + 1);
        String mimeType = myMime.getMimeTypeFromExtension(extension);

        Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, mimeType);
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static LayoutInflater getAdapterInflater(ViewGroup viewGroup) {
        return LayoutInflater.from(viewGroup.getContext());
    }


    public static String getUrlDecodedString(String text) {
        try {
            return URLDecoder.decode(text, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            // not going to happen - value came from JDK's own StandardCharsets
            return text;
        }
    }

    public static Map<String, Integer> getScreenSize(Activity activity) {
        int columnWidth;
        WindowManager wm = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();

        point.x = display.getWidth();
        point.y = display.getHeight();

        Map<String, Integer> mapScreenSize = new HashMap<>();
        mapScreenSize.put("width", point.x);
        mapScreenSize.put("height", point.y);
        Log.i("ScreenRes", mapScreenSize.toString());
        return mapScreenSize;
    }



    public static long getCurrentTimeStampChat() {
        return System.currentTimeMillis() / 1000L;
    }

    public static String getCurrentTimeFormat(DateFormat dateFormat) {
        return dateFormat.format(new Date());
    }

    public static void openWeb(Activity activity, String docUrl) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(activity, R.color.black));
        builder.setShowTitle(true);

        CustomTabsIntent customTabsIntent = builder.build();
        try {
            customTabsIntent.intent.setPackage("com.android.chrome");


        } catch (Exception e) {

            e.printStackTrace();
        }
        customTabsIntent.launchUrl(activity, Uri.parse(docUrl));

    }


    public static void writeAPIResponse(String data, Context context) {
        //  if (BuildConfig.DEBUG) {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
        String time = format.format(calendar.getTime());

        File path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS + "/MitezyAPI/");
        // Make sure the path directory exists.
        if (!path.exists()) {
            // Make it, if it doesn't exit
            path.mkdirs();
        }


        final File file = new File(path, "response_" + time + ".txt");

        // Save your stream, don't forget to flush() it before closing it.


        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            fOut.write(data.getBytes());
                /*OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(data);

                myOutWriter.close();*/

            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //  }
    }


    public static String getCheckboxVal(boolean value) {
        if (value) {
            return "1";
        } else {
            return "0";
        }

    }

    public static int pxToDp(int px) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        float dp = px / density;

        return (int) dp;
    }

    public static Point getDispRes(Activity activity, boolean realSize) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = activity.getWindowManager().getDefaultDisplay();
        display.getMetrics(displayMetrics);
        Point size = new Point();
        if (realSize) {
            display.getRealSize(size);
        } else {
            display.getSize(size);
        }
        Log.i("DispRes", App.gson.toJson(size));
        Log.i("DispRes", String.valueOf(size.x / 3));
        return size;
    }

    public static int dpToPx(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        float px = (dp * density);

        return (int) px;
    }

    public static String get0or1Check(CompoundButton checkBox) {
        if (checkBox.isChecked()) {
            return "1";
        } else {
            return "0";
        }
    }

    public static void copyText(String tag, String text) {
        ClipboardManager clipboard = (ClipboardManager) App.getInstance().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(tag, text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(App.getInstance(), "Copied", Toast.LENGTH_SHORT).show();
        }
    }

    public static void shareText(Activity activity, Fragment fragment, String shareBody) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public static Bundle getBundle(Activity activity) {
        return activity.getIntent().getExtras() != null ? activity.getIntent().getExtras() : new Bundle();
    }

    public static Bundle getBundle(Intent intent) {
        return intent.getExtras() != null ? intent.getExtras() : new Bundle();
    }

    public static Bundle createBundle(Activity activity) {
        Bundle bundle = new Bundle();
        bundle.putAll(CommonFunctions.getBundle(activity));
        return bundle;
    }

    public static Bundle createBundle(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putAll(CommonFunctions.getBundle(fragment));
        return bundle;
    }

    public static Bundle getBundle(Fragment fragment) {
        return fragment.getArguments() != null ? fragment.getArguments() : new Bundle();
    }

    public static double getNumberFromInput(TextView textView) {
        String num = textView.getText().toString().trim();
        if (!InputValidator.isValidNum(num)) {
            return 0;
        } else {
            return Double.parseDouble(num);
        }
    }

    public static void setTextColor(int color, TextView textView) {

        textView.setTextColor(color);

    }

    public static void setColorFromResTextView(int color, TextView textView) {

        try {
            textView.setTextColor(ContextCompat.getColor(App.getInstance(), color));
        } catch (Resources.NotFoundException e) {
            textView.setTextColor(color);
            e.printStackTrace();
        }

    }

    public static void showKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) App.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }
}
