package com.example.easyshipping;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.VerticalText;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.security.Permission;

public class PrintActivity extends AppCompatActivity {

    Button btn_create_pdf;
    //CreateLabelActivity Label = new CreateLabelActivity();
    private String email,SenAL1,SenAL2,SenTn,SenCounty,SenCountry,SenPC,SenPh;

    /**
     * Get the sender details printed out at the bottom, access these from the
     * Menu and pass them through create label and then to here
     *
     * Also, add a barcode
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        btn_create_pdf = (Button) findViewById(R.id.btn_create_pdf);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        email = extras.getString("email");
        getSenAddr(email);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("EasyShipping");


        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                btn_create_pdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createPDFFile(Common.getAppPath(PrintActivity.this) + "test_pdf.pdf");
                    }
                });
            }
            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check();
    }

    public void createPDFFile(String path) {
        if(new File(path).exists())
            new File(path).delete();
        try{
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            String str_conNo = extras.getString("str_conNo");
            String str_name = extras.getString("str_name");
            String str_addressline1 = extras.getString("str_addressline1");
            String str_addressline2 = extras.getString("str_addressline2");
            String str_towncity = extras.getString("str_towncity");
            String str_countystate = extras.getString("str_countystate");
            String str_country = extras.getString("str_country");
            String str_postcode = extras.getString("str_postcode");
            String str_noofparcels = extras.getString("str_noofparcels");
            String str_totalweight = extras.getString("str_totalweight");
            String str_phoneno = extras.getString("str_phoneno");
            String str_additionalnote = extras.getString("str_additionalnote");
            Bitmap bitmap = (Bitmap) extras.getParcelable("qrcode");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image maimg = Image.getInstance(stream.toByteArray());


            Document document = new Document();
            //Save
            PdfWriter.getInstance(document,new FileOutputStream(path));
            //Open to write
            document.open();

            //Setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("A00240194");
            document.addCreator("Ross Hayden");

            //Font Setting
            BaseColor colorAccent = new BaseColor(0,153,204,255);
            float fontSize=15.0f;
            float valueFontSize=21.0f;

            //Custom font
            BaseFont fontName = BaseFont.createFont("res/font/brandon_medium.otf","UTF-8",BaseFont.EMBEDDED);

            //Create Document Title
            //Start Border
            Font titleFont = new Font(fontName,31.0f,Font.NORMAL,BaseColor.BLACK);
            Font conNumberFont = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            Font orderNumberValueFont = new Font(fontName,valueFontSize,Font.NORMAL,BaseColor.BLACK);
            Font addressFont = new Font(fontName,21.0f,Font.NORMAL,BaseColor.BLACK);

            addNewItem(document,"Consignee Details", Element.ALIGN_CENTER,titleFont);
//            addNewItem(document,"Name: ",Element.ALIGN_LEFT,conNumberFont);
//
//            addNewItem(document,"Consignment No: "+ str_name,Element.ALIGN_LEFT,conNumberFont);
//            addNewItem(document,str_conNo,Element.ALIGN_RIGHT,conNumberFont);
//
//            addLineSeperator(document);
//
//            addNewItem(document,"Address:",Element.ALIGN_LEFT,conNumberFont);
//            addNewItem(document,str_addressline1+", "+str_addressline2+", "+str_towncity+", "+str_countystate,Element.ALIGN_LEFT,orderNumberValueFont);
//
//            addLineSeperator(document);
//
//            addNewItem(document,"Country:",Element.ALIGN_LEFT,conNumberFont);
//            addNewItem(document,str_country,Element.ALIGN_LEFT,orderNumberValueFont);
//            addLineSpace(document);
//
//            addNewItemWithLeftAndRight(document,str_postcode,"Parcels: "+str_noofparcels,titleFont,orderNumberValueFont);
//            addNewItemWithLeftAndRight(document,str_phoneno,"Weight: "+str_totalweight,titleFont,orderNumberValueFont);
//
//            addLineSeperator(document);
//
//            addNewItemWithLeftAndRight(document,"Additional Note:",str_additionalnote,titleFont,orderNumberValueFont);
//            //End Border

            PdfPTable table = new PdfPTable(2);
            table.setWidths(new float[] { 3, 1 });
            PdfPCell cell1 = new PdfPCell(new Phrase("Delivery Address:\n"+str_addressline1+"\n"+str_addressline2
                    +"\n"+str_towncity+"\n"+str_countystate+"\n"+str_country+"\n"+str_postcode,titleFont));
            table.addCell(cell1);

            PdfPCell cell3 = new PdfPCell(new Phrase("Sender:\n"+SenAL1+"\n"+SenAL2
                    +"\n"+SenTn+"\n"+SenCounty+"\n"+SenCountry+"\n"+"Phone: "+SenPh+"\n"+email,conNumberFont));
            cell3.setRotation(270);
            table.addCell(cell3);
            table.completeRow();

            table.setWidthPercentage(100);
            document.add(table);
            addNewItem(document,"Name: "+ str_name,Element.ALIGN_LEFT,conNumberFont);
            addNewItem(document,"Consignment No: "+ str_conNo,Element.ALIGN_LEFT,conNumberFont);
            addNewItemWithLeftAndRight(document,"Phone: "+str_phoneno,"Parcels: "+str_noofparcels,conNumberFont,conNumberFont);
            addNewItemWithLeftAndRight(document,"Note: "+str_additionalnote,"Weight: "+str_totalweight,conNumberFont,conNumberFont);

            addLineSeperator(document);

            maimg.setAlignment(Image.RIGHT);
            document.add(maimg);

            addLineSeperator(document);
            document.close();

            Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show();

            printPDF();


        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printPDF() {
        PrintManager printManager = (PrintManager)getSystemService(Context.PRINT_SERVICE);
        try{
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(PrintActivity.this,Common.getAppPath(PrintActivity.this)+"test_pdf.pdf");
            printManager.print("Document",printDocumentAdapter,new PrintAttributes.Builder().build());
        } catch (Exception ex)
        {
            Log.e("A00240194", ""+ex.getMessage());
        }
    }

    private void addNewItemWithLeftAndRight(Document document,String textLeft, String textRight, Font textLeftFont, Font textRightFont) throws DocumentException
    {
        Chunk chunkTextLeft = new Chunk(textLeft,textLeftFont);
        Chunk chunkTextRight = new Chunk(textRight,textRightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);
    }

    private void addLineSeperator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text,font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);
    }

    private void getSenAddr(String SenEmail) {
        String url= "https://easyshippingrh.000webhostapp.com/SelectCurrentSenUser.php?email="+SenEmail;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for(int i=0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                SenAL1 = jsonobject.getString("addressline1");
                                SenAL2 = jsonobject.getString("addressline2");
                                SenTn = jsonobject.getString("towncity");
                                SenCounty = jsonobject.getString("countystate");
                                SenCountry = jsonobject.getString("country");
                                SenPC = jsonobject.getString("postcode");
                                SenPh = jsonobject.getString("phoneno");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Wrong email provided, please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){
                            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
