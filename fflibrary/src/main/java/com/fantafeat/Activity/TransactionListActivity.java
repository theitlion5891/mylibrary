package com.fantafeat.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantafeat.Adapter.TransactionListAdapter;
import com.fantafeat.Model.TransactionModel;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.Session.MyApp;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TransactionListActivity extends BaseActivity {
    RecyclerView transaction_full_list;
    TransactionListAdapter transactionListAdapter;
    List<TransactionModel> transactionModelList = new ArrayList<>();

    int offset, limit;
    boolean isApiCall, isGetData;
    private SwipeRefreshLayout pull_transaction;
    LinearLayout scroll_loading;
    LinearLayoutManager linearLayout;
    ImageView toolbar_back;
    /*Button btnDown;
    EditText edtTo,edtFrom;*/
    TextView toolbar_title,btnWithdrawHistory;
   /* DatePickerDialog fromDate,toDate;
    Calendar fromCalendar,toCalendar;*/

    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        setContentView(R.layout.fragment_transaction_list);

        initData();
        initClik();
        getData();
    }

    private void initClik() {
        pull_transaction.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                transactionModelList = new ArrayList<>();
                offset = 0;
                isGetData = true;
                getData();
            }
        });

        btnWithdrawHistory.setOnClickListener(v -> {
            startActivity(new Intent(mContext,WithdrawListActivity.class));
        });

        toolbar_back.setOnClickListener(v->{
            finish();
        });

        transaction_full_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayout != null &&
                        linearLayout.findLastCompletelyVisibleItemPosition() == transactionModelList.size() - 1
                        && isGetData && isApiCall) {

                    scroll_loading.setVisibility(View.VISIBLE);
                    getData();
                }
            }
        });

        /*btnDown.setOnClickListener(v ->{
            LogUtil.e("flpath","  Click  ");
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                if (isValidDate()){
                                    getTransactionData();
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

        edtTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtFrom.getText().toString().trim())){
                    CustomUtil.showTopSneakError(getApplicationContext(),"Please Enter From Date");
                    return ;
                }
               toDate.show();
            }
        });

        edtFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               fromDate.show();
            }
        });*/
    }

    private void initData() {
        offset = 0;
        limit = 25;
        isApiCall = true;
        isGetData = true;

        linearLayout = new LinearLayoutManager(mContext);
        transaction_full_list = findViewById(R.id.transaction_full_list);
        scroll_loading = findViewById(R.id.scroll_loading);
        pull_transaction = findViewById(R.id.pull_transaction);
        toolbar_back = findViewById(R.id.toolbar_back);
        btnWithdrawHistory = findViewById(R.id.btnWithdrawHistory);
        toolbar_title = findViewById(R.id.toolbar_title);
//        edtTo = findViewById(R.id.edtTo);
//        edtFrom = findViewById(R.id.edtFrom);
        toolbar_title.setText("Transactions");

       /* btnDown = findViewById(R.id.btnDown);

        fromCalendar = Calendar.getInstance();
        toCalendar = Calendar.getInstance();

        fromDate = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                fromCalendar.set(Calendar.YEAR, year);
                fromCalendar.set(Calendar.MONTH, monthOfYear);
                fromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                edtTo.setText("");
                updateLabel(edtFrom,fromCalendar);
            }
        }, fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), fromCalendar.get(Calendar.DAY_OF_MONTH));
        fromDate.getDatePicker().setMinDate(System.currentTimeMillis() - 568025136000L);

        toDate = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                toCalendar.set(Calendar.YEAR, year);
                toCalendar.set(Calendar.MONTH, monthOfYear);
                toCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(edtTo,toCalendar);
            }
        }, toCalendar.get(Calendar.YEAR), toCalendar.get(Calendar.MONTH), toCalendar.get(Calendar.DAY_OF_MONTH));
        toDate.getDatePicker().setMaxDate(fromCalendar.getTimeInMillis());*/
    }

    private void updateLabel(EditText edt,Calendar calendar) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = MyApp.changedFormat(myFormat);
        edt.setText(sdf.format(calendar.getTime()));
    }

    /*private boolean isValidDate(){
        if (TextUtils.isEmpty(edtFrom.getText().toString().trim())){
            CustomUtil.showTopSneakError(this,"Please Enter From Date");
            return false;
        }
        if (TextUtils.isEmpty(edtTo.getText().toString().trim())){
            CustomUtil.showTopSneakError(this,"Please Enter To Date");
            return false;
        }
        return true;
    }

    private void createPDF(){
        Document doc = new Document();
        PdfWriter docWriter = null;

        DecimalFormat df = new DecimalFormat("0.00");

        String pdfFilename="Transaction_"+edtFrom.getText().toString().trim()+" to "+edtTo.getText().toString().trim()+"_"+( System.currentTimeMillis()/1000) + ".pdf";

        try {

            //special font sizes
            Font bfBold12 = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(FontFamily.TIMES_ROMAN, 12);

            //file path
            String path = Environment.getExternalStorageDirectory() + File.separator+ getResources().getString(R.string.app_name) + File.separator+ pdfFilename;
            File f=new File(Environment.getExternalStorageDirectory() + File.separator+ getResources().getString(R.string.app_name));
            if (!f.mkdir()){
                f.mkdir();
            }
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

            Font font = new Font(FontFamily.TIMES_ROMAN, 20, Font.NORMAL, BaseColor.BLACK);
            doc.add(new Phrase("Fantafeat",font));


            //create a paragraph
            Paragraph paragraph = new Paragraph("Fantafeat Transactions\nFrom: "+edtFrom.getText().toString().trim()+" to "+edtTo.getText().toString().trim());

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

            SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            SimpleDateFormat TransactionFormat = MyApp.changedFormat("dd MMM, yyyy hh:mma",Locale.US);

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
        catch (DocumentException dex)
        {
            dex.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (doc != null){
                //close the document
                doc.close();
            }
            if (docWriter != null){
                //close the writer
                docWriter.close();
            }

            CustomUtil.showTopSneakSuccess(mContext,"Download success.");
        }
    }

    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font){

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
      //  cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }

    private void exportCSVfile() throws IOException {
        LogUtil.e("flpath","Call");
        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName ="Transaction_"+( System.currentTimeMillis()/1000) + ".csv";
        String csv =Environment.getExternalStorageDirectory().getAbsolutePath(); //getExternalFilesDir(DIRECTORY_DOWNLOADS).getAbsolutePath();

        String filePath = csv + File.separator +getResources().getString(R.string.app_name)+File.separator+ fileName;
        //File f = new File(filePath);

        CSVWriter writer;

        File fn = new File(Environment.getExternalStorageDirectory() + File.separator+ getResources().getString(R.string.app_name));
        LogUtil.e("flpath",fn.getAbsolutePath());
        if (!fn.mkdirs()) {
            fn.mkdirs();
        }

        File f=new File(fn.getAbsoluteFile(),File.separator+ fileName);

        FileWriter mFileWriter = new FileWriter(f);
        writer = new CSVWriter(mFileWriter);


        String[] data = {"No", "Title", "Transaction ID","Transaction Amount","Transaction Type","Description","Date"};
        writer.writeNext(data);

        DecimalFormat decimalFormat=CustomUtil.getFormater("00.00");

        SimpleDateFormat format = MyApp.changedFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat TransactionFormat = MyApp.changedFormat("dd MMM, yyyy hh:mma",Locale.US);

        try {

            for (int i=0;i<transactionModelList.size();i++){
                TransactionModel transactionModel=transactionModelList.get(i);
                Date date = format.parse(transactionModel.getCreateAt());

                String transactionDate = TransactionFormat.format(date);
                String[] data1 = {(i+1)+"",
                        transactionModel.getTransName(),
                        transactionModel.getTransId(),
                        mContext.getResources().getString(R.string.rs) + decimalFormat.format(CustomUtil.convertFloat(transactionModel.getAmount())),
                        transactionModel.getTransType(),
                        transactionModel.getTransDesc(),
                        transactionDate};
                writer.writeNext(data1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        writer.close();

    }

    private void getTransactionData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("from_date", edtFrom.getText().toString().trim());
            jsonObject.put("to_date", edtTo.getText().toString().trim());
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
                    createPDF();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }*/

    private void getData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", preferences.getUserModel().getId());
            jsonObject.put("offset", offset);
            jsonObject.put("limit", limit);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean isProgress=true;
        if (pull_transaction != null && pull_transaction.isRefreshing()) {
            isProgress=false;
        }

        isApiCall = false;
        HttpRestClient.postJSON(mContext,isProgress , ApiManager.TRANSACTION_LIST, jsonObject, new GetApiResult() {
            @Override
            public void onSuccessResult(JSONObject responseBody, int code) {
                LogUtil.e("TAG", "onSuccessResult: " + responseBody.toString());
                if (responseBody.optBoolean("status")) {
                    checkPull();
                    JSONArray array = responseBody.optJSONArray("data");
                    int i = 0;
                    if (limit > array.length()) {
                        isGetData = false;
                    }
                    for (i = 0; i < array.length(); i++) {
                        JSONObject object = array.optJSONObject(i);
                        TransactionModel transactionModel = gson.fromJson(object.toString(), TransactionModel.class);
                        transactionModelList.add(transactionModel);
                    }

                    /*if(array.length()<6){
                        transaction_view_more.setVisibility(View.GONE);
                    }*/

                    if (offset == 0) {
                        transactionListAdapter = new TransactionListAdapter(mContext, transactionModelList);
                        transaction_full_list.setLayoutManager(linearLayout);
                        //transaction_full_list.addItemDecoration(new DividerItemDecoration(transaction_full_list.getContext(), DividerItemDecoration.VERTICAL));
                        transaction_full_list.setAdapter(transactionListAdapter);

                    } else {
                        if (transactionListAdapter != null) {
                            transactionListAdapter.updateData(transactionModelList);
                        }else{
                            transactionListAdapter = new TransactionListAdapter(mContext, transactionModelList);
                            transaction_full_list.setLayoutManager(linearLayout);
                           // transaction_full_list.addItemDecoration(new DividerItemDecoration(transaction_full_list.getContext(), DividerItemDecoration.VERTICAL));
                            transaction_full_list.setAdapter(transactionListAdapter);
                        }
                    }
                    offset += array.length();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void checkPull() {
        if (pull_transaction != null && pull_transaction.isRefreshing()) {
            pull_transaction.setRefreshing(false);
        }
        isApiCall = true;
        scroll_loading.setVisibility(View.GONE);
    }
}