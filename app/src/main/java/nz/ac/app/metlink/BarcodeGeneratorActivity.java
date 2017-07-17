package nz.ac.app.metlink;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class BarcodeGeneratorActivity extends AppCompatActivity {
    public String Ticket_Num, Email_string;
    ImageView image;
    TextView txtbarcode;
    Button scan_barcode;

// https://www.youtube.com/watch?v=e5pI6CSxAX8 (Barcode gnerator)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_generator);
        Bundle extras=getIntent().getExtras();
        if(extras!=null)
        {
            Ticket_Num=extras.getString("TicketNum");
            Email_string = extras.getString("Email");
        }
        image=(ImageView)findViewById(R.id.image);
        txtbarcode=(TextView)findViewById(R.id.barcodeNumber);
        scan_barcode=(Button)findViewById(R.id.button_scan);
        scan_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BarcodeGeneratorActivity.this,ScanBarcode.class);
                startActivity(intent);
            }
        });
        GenerateBarcode(Ticket_Num);

    }
    public void GenerateBarcode(String TicketNum)
    {
        MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
        try
        {
            BitMatrix bitMatrix=multiFormatWriter.encode(TicketNum, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
            Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
            image.setImageBitmap(bitmap);
           // txtbarcode.setText(TicketNum);
        }
        catch (WriterException e)
        {
            e.printStackTrace();

        }
    }




    public void backClicked(View view)
    {
        Intent intent=new Intent(BarcodeGeneratorActivity.this, ViewTicket.class);
        Bundle extras=new Bundle();
        extras.putString("Email",Email_string);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }
}
