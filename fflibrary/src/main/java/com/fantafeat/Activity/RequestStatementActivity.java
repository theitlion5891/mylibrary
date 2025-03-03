package com.fantafeat.Activity;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantafeat.BuildConfig;
import com.fantafeat.Model.TransactionModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;
import com.fantafeat.util.WatermarkPageEvent;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class RequestStatementActivity extends BaseActivity {

    TextView from_date,to_date,submit,toolbar_title,share;
    DatePickerDialog datePickerDialog;

    private String fromDate;
    private DatePickerDialog fromDatePicker;
    private DatePickerDialog.OnDateSetListener fromDatePickerListener;
    private Calendar fromDateCalendar;
    private String toDate;
    private ImageView toolbar_back;
    private DatePickerDialog toDatePicker;
    private DatePickerDialog.OnDateSetListener toDatePickerListener;
    private Calendar toDateCalendar;
    List<TransactionModel> transactionDownList = new ArrayList<>();

    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    String[] p;

    @Override
    public void initClick() {}

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.fragment_request_statement);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }

        initData();
        initClic();
    }

    private void initClic() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(RequestStatementActivity.this)
                        .withPermissions(p)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    if(validate()){
                                        getTransactionData(false);
                                    }
                                }

                                // check for permanent denial of any permission
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    // permission is denied permenantly, navigate user to app settings
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        })
                        .onSameThread()
                        .check();

            }
        });

        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                fromDatePicker.show();
            }
        });

        toolbar_back.setOnClickListener(v->{
            finish();
        });

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(from_date.getText().toString().toLowerCase().equals("select")){
                    CustomUtil.showTopSneakError(mContext,"Please select From Date");
                }else {
                    toDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                    toDatePicker.show();
                }
            }
        });

        share.setOnClickListener(view -> {
            Dexter.withActivity(RequestStatementActivity.this)
                    .withPermissions(p)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                if(validate()){
                                    getTransactionData(true);
                                }
                            }

                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // permission is denied permenantly, navigate user to app settings
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .onSameThread()
                    .check();
        });
    }

    private void getTransactionData(boolean isShare) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("to_date",to_date.getText().toString().trim());
            jsonObject.put("from_date",from_date.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext,true , ApiManager.requestStatementnew, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("TAG", "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    JSONArray array = responseBody.optJSONArray("data");
                    int i = 0;

                    for (i = 0; i < array.length(); i++) {
                        JSONObject object = array.optJSONObject(i);
                        TransactionModel transactionModel = gson.fromJson(object.toString(), TransactionModel.class);
                        transactionDownList.add(transactionModel);
                    }
                    createPDF(isShare);
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void createPDF(boolean isShare){
        // Start a the operation in a background thread
        DownloadWebPageTask task = new DownloadWebPageTask(isShare);
        task.execute(new String[] { "" });
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        boolean isShare=false;

        public DownloadWebPageTask(boolean isShare) {
            this.isShare = isShare;
        }

        @Override
        protected String doInBackground(String... urls) {

            Document doc = new Document();
            PdfWriter docWriter = null;

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            DecimalFormat df = new DecimalFormat("0.00");

            String pdfFilename="Transaction_"+from_date.getText().toString().trim()+"_to_"+to_date.getText().toString().trim()+"_"+( System.currentTimeMillis()/1000) + ".pdf";

            String path =    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ File.separator+ "Fantafeat" + File.separator+pdfFilename;//Environment.DIRECTORY_DOWNLOADS+ "/Fantafeat/"
            File f=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +File.separator+ "Fantafeat");//Environment.DIRECTORY_DOWNLOADS+ "/Fantafeat"
            if (!f.mkdir()){
                f.mkdir();
            }

            try {

                //special font sizes
                Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
                Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

                //file path

                docWriter = PdfWriter.getInstance(doc , new FileOutputStream(path));

                docWriter.setPageEvent(new WatermarkPageEvent());

                //document header attributes
                doc.addAuthor("fantafeat");
                doc.addCreationDate();
                doc.addProducer();
                doc.addCreator("fantafeat.com");
                doc.addTitle("Fantafeat");
                doc.setPageSize(PageSize.LETTER);

                //open document
                doc.open();

                Font font = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.NORMAL, BaseColor.BLACK);
                doc.add(new Phrase("Fantafeat",font));

                //create a paragraph
                Paragraph paragraph = new Paragraph("Fantafeat Transactions\nFrom: "+from_date.getText().toString().trim()+" to "+to_date.getText().toString().trim());

                //specify column widths
                float[] columnWidths = {1f, 2f, 2f, 2f, 2f, 2f, 2f};
                //create PDF table with the given widths
                PdfPTable table = new PdfPTable(columnWidths);
                // set table width a percentage of the page width
                table.setWidthPercentage(100f);

                //insert column headings  "Title", "Transaction ID","Transaction Amount","Transaction Type","Description","Date"
                insertCell(table, "No", Element.ALIGN_MIDDLE, 1, bfBold12);
                insertCell(table, "Title", Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, "Transaction ID", Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, "Transaction Amount", Element.ALIGN_LEFT, 2, bfBold12);
                insertCell(table, "Transaction Type", Element.ALIGN_LEFT, 2, bfBold12);
                insertCell(table, "Description", Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, "Date", Element.ALIGN_LEFT, 1, bfBold12);
                table.setHeaderRows(1);

                DecimalFormat decimalFormat=CustomUtil.getFormater("00.00");

                SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat TransactionFormat = MyApp.changedFormat("dd MMM, yyyy hh:mma");

                try {

                    for (int i=0;i<transactionDownList.size();i++){
                        TransactionModel transactionModel=transactionDownList.get(i);
                        Date date = format.parse(transactionModel.getCreateAt());

                        String transactionDate = TransactionFormat.format(date);

                        insertCell(table, (i+1)+"", Element.ALIGN_MIDDLE, 1, bf12);
                        insertCell(table,  transactionModel.getTransName(), Element.ALIGN_LEFT, 1, bf12);
                        insertCell(table,  transactionModel.getTransId(), Element.ALIGN_LEFT, 1, bf12);
                        insertCell(table,  "₹"+ decimalFormat.format(CustomUtil.convertFloat(transactionModel.getAmount())), Element.ALIGN_LEFT, 1, bf12);
                        insertCell(table,  transactionModel.getTransType(), Element.ALIGN_LEFT, 1, bf12);
                        insertCell(table,  transactionModel.getTransDesc(), Element.ALIGN_LEFT, 1, bf12);
                        insertCell(table,  transactionDate, Element.ALIGN_LEFT, 1, bf12);

                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //add the PDF table to the paragraph
                paragraph.add(table);
                // add the paragraph to the document
                doc.add(paragraph);

            }
            catch (Exception dex) {
                dex.printStackTrace();
            } finally {
                if (doc != null){
                    //close the document
                    doc.close();
                }
                if (docWriter != null){
                    //close the writer
                    docWriter.close();
                }
                // CustomUtil.showTopSneakSuccess(mContext,"Download success.");
            }

            try {
                if (isShare){
                    sahareFile(path);
                }
               /* else {
                    sendNotification("Statement Download","Download Completed",pdfFilename);
                }*/

            } catch (Exception e) {
                e.printStackTrace();
            }
            return pdfFilename;
        }

        @Override
        protected void onPostExecute(String result) {
            /*try {
                    *//*File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),File.separator+ getResources().getString(R.string.app_name) + File.separator+pdfFilename);
                    Uri apkURI = FileProvider.getUriForFile(
                            getApplicationContext(),
                            getApplicationContext()
                                    .getPackageName() + ".provider", file);*//*

            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }

    }
    private void sahareFile(String path) {

        File pdfFile=new File(path);
        Uri uri ;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", pdfFile);
        } else {
            uri = Uri.fromFile(pdfFile);
        }

        LogUtil.e("resp","Share File Path: "+uri);

        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        //share.setPackage("com.whatsapp");

        startActivity(share);
    }

    private Uri getUriFromFile(File file){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Uri.fromFile(file);
        }else {
            return FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", file);
        }
    }
    /*private void sendNotification(String title, String message, String path) throws JSONException {

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+
                File.separator+ "Fantafeat" + File.separator,path);

        LogUtil.e("pdfpth",Uri.fromFile(file)+" ");


        Intent intent = new Intent(Intent.ACTION_VIEW);

        //intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setDataAndType(getUriFromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|
                Intent.FLAG_ACTIVITY_NO_HISTORY);

        int pendingFlags;
        if (Build.VERSION.SDK_INT >= 23) {
            pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        } else {
            pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT;
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,pendingFlags );//last PendingIntent.FLAG_ONE_SHOT
        //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.plucky);
        String Channel = getString(R.string.default_notification_channel_id);
        Random random = new Random();
        int Notification_ID = random.nextInt(9999 - 1000) + 1000;

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = null;

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.setSummaryText(message);

        notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_push_notification_icon)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(bigTextStyle)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setChannelId(Channel)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = getString(R.string.default_notification_channel_id);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            NotificationChannel channel = new NotificationChannel(Channel, name, importance);
            channel.setDescription(title);
            channel.enableVibration(true);
            channel.setSound(defaultSoundUri, att);
            channel.setVibrationPattern(new long[]{1000});
            // Do not show notification badge, as we use sticky notification, android 8+ devices will
            // always show a badge over app icon, which should be disabled.
            channel.setShowBadge(false);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }

        }
        assert notificationManager != null;
        notificationManager.notify(Notification_ID, notificationBuilder.build());
    }*/

    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font){

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
       // cell.setBackgroundColor(new BaseColor(R.color.appBackGround));
        //set the cell column span in case you want to merge two or more cells
        //  cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }

    private void initData() {
        from_date = findViewById(R.id.from_date);
        to_date = findViewById(R.id.to_date);
        share = findViewById(R.id.share);

        submit = findViewById(R.id.submit);
        toolbar_back = findViewById(R.id.toolbar_back);
        toolbar_title = findViewById(R.id.toolbar_title);

        toolbar_title.setText("Request Statement");


        from_date.setText("Select");
        to_date.setText("Select");


        fromDateCalendar = Calendar.getInstance();
        fromDatePickerListener = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            fromDateCalendar.set(Calendar.YEAR, year);
            fromDateCalendar.set(Calendar.MONTH, monthOfYear);
            fromDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            fromDate = fromDateCalendar.get(Calendar.DAY_OF_MONTH) + "-" + (fromDateCalendar.get(Calendar.MONTH)+1) + "-" + fromDateCalendar.get(Calendar.YEAR);
            from_date.setText(fromDate);
            toDatePicker.getDatePicker().setMinDate(fromDateCalendar.getTimeInMillis());
        };

        toDateCalendar = Calendar.getInstance();
        toDatePickerListener = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            toDateCalendar.set(Calendar.YEAR, year);
            toDateCalendar.set(Calendar.MONTH, monthOfYear);
            toDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            toDate = toDateCalendar.get(Calendar.DAY_OF_MONTH) + "-" + (toDateCalendar.get(Calendar.MONTH)+1) + "-" + toDateCalendar.get(Calendar.YEAR) ;
            to_date.setText(toDate);
        };

        toDatePicker = new DatePickerDialog(this,toDatePickerListener, toDateCalendar.get(Calendar.YEAR), toDateCalendar.get(Calendar.MONTH), toDateCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePicker = new DatePickerDialog(this,fromDatePickerListener, fromDateCalendar.get(Calendar.YEAR), fromDateCalendar.get(Calendar.MONTH), fromDateCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void callAPI() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",preferences.getUserModel().getId());
            jsonObject.put("to_date",to_date.getText().toString().trim());
            jsonObject.put("from_date",from_date.getText().toString().trim());
           // jsonObject.put("email_id",email.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRestClient.postJSON(mContext, true, ApiManager.REQUEST_STATEMENT, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                if (responseBody.optBoolean("status")) {
                    CustomUtil.showTopSneakSuccess(mContext, responseBody.optString("msg"));

                    LogUtil.e(TAG, "onSuccessResult: "+responseBody.toString() );
                    finish();
                } else {
                    CustomUtil.showTopSneakError(mContext, responseBody.optString("msg"));
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });

    }

    private boolean validate() {
        if(from_date.getText().toString().equals("Select") ||
                from_date.getText().toString().equals("")){
            CustomUtil.showTopSneakError(mContext,"Please select From date");
            return false;
        }else if(to_date.getText().toString().equals("Select") ||
                to_date.getText().toString().equals("")){
            CustomUtil.showTopSneakError(mContext,"Please select To date");
            return false;
        }
        /*else if (email.getText().toString().equals("")){
            CustomUtil.showTopSneakError(mContext,"Please enter email");
            return false;
        }else if(!isValidEmail(email.getText().toString().trim())){
            CustomUtil.showTopSneakError(mContext,"Please enter valid email");
            return false;
        }*/
        return true;
    }

}

