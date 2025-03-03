package com.fantafeat.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.GetApiResult;
import com.fantafeat.util.HttpRestClient;
import com.fantafeat.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class RequestStatementFragment extends BaseFragment {

    TextView from_date,to_date,submit;//email,
    DatePickerDialog datePickerDialog;

    private String fromDate;
    private DatePickerDialog fromDatePicker;
    private DatePickerDialog.OnDateSetListener fromDatePickerListener;
    private Calendar fromDateCalendar;
    private String toDate;
    private DatePickerDialog toDatePicker;
    private DatePickerDialog.OnDateSetListener toDatePickerListener;
    private Calendar toDateCalendar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_statement, container, false);
        initFragment(view);
        initToolBar(view,"Request Statement",true);
        return view;
    }

    @Override
    public void initControl(View view) {
        from_date = view.findViewById(R.id.from_date);
        to_date = view.findViewById(R.id.to_date);
        //email = view.findViewById(R.id.email);
        submit = view.findViewById(R.id.submit);

        //email.setText(preferences.getUserModel().getEmailId());
        from_date.setText("Select");
        to_date.setText("Select");


        fromDateCalendar = Calendar.getInstance();
        fromDatePickerListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                fromDateCalendar.set(Calendar.YEAR, year);
                fromDateCalendar.set(Calendar.MONTH, monthOfYear);
                fromDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                fromDate = fromDateCalendar.get(Calendar.DAY_OF_MONTH) + "-" + (fromDateCalendar.get(Calendar.MONTH)+1) + "-" + fromDateCalendar.get(Calendar.YEAR);
                from_date.setText(fromDate);
                to_date.setText("");
                toDatePicker.getDatePicker().setMinDate(fromDateCalendar.getTimeInMillis());
            }

        };

        toDateCalendar = Calendar.getInstance();
        toDatePickerListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                toDateCalendar.set(Calendar.YEAR, year);
                toDateCalendar.set(Calendar.MONTH, monthOfYear);
                toDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                toDate = toDateCalendar.get(Calendar.DAY_OF_MONTH) + "-" + (toDateCalendar.get(Calendar.MONTH)+1) + "-" + toDateCalendar.get(Calendar.YEAR) ;
                to_date.setText(toDate);
            }

        };

        toDatePicker = new DatePickerDialog(getContext(),toDatePickerListener, toDateCalendar.get(Calendar.YEAR), toDateCalendar.get(Calendar.MONTH), toDateCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePicker = new DatePickerDialog(getContext(),fromDatePickerListener, fromDateCalendar.get(Calendar.YEAR), fromDateCalendar.get(Calendar.MONTH), fromDateCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void initClick() {

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Dexter.withActivity((Activity)mContext)
                        .withPermissions(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    if(validate()){
                                        callAPI();
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
                        .check();*/
                if(validate()){
                    callAPI();
                }

            }
        });

        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                fromDatePicker.show();
            }
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
    }

    /*private void getTransactionData() {
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
                    createPDF();
                }
            }

            @Override
            public void onFailureResult(String responseBody, int code) {

            }
        });
    }

    private void createPDF(){
        Document doc = new Document();
        PdfWriter docWriter = null;

        DecimalFormat df = new DecimalFormat("0.00");

        String pdfFilename="Transaction_"+from_date.getText().toString().trim()+" to "+to_date.getText().toString().trim()+"_"+( System.currentTimeMillis()/1000) + ".pdf";

        try {

            //special font sizes
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

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

    }*/

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
                    RemoveFragment();
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
        if(from_date.getText().toString().equals("Select") || from_date.getText().toString().equals("")){
            CustomUtil.showTopSneakError(mContext,"Please select From date");
            return false;
        }else if(to_date.getText().toString().equals("Select") || to_date.getText().toString().equals("")){
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