package com.example.reachout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ContactDetailActivity extends AppCompatActivity {

    private String name;
    private String phone;

    private int image;
    private static final int REQUEST_EDIT_CONTACT = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_CONTACT && resultCode == RESULT_OK) {
            // Get edited data
            name = data.getStringExtra("new_name");
            phone = data.getStringExtra("new_phone");
            byte[] byteArray = data.getByteArrayExtra("new_image");

            // Convert byte array to Bitmap
            Bitmap newImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            // Update views
            TextView nameView = findViewById(R.id.detail_name);
            TextView phoneView = findViewById(R.id.detail_phone);
            ImageView imageView = findViewById(R.id.detail_image);
            nameView.setText(name);
            phoneView.setText(phone);
            imageView.setImageBitmap(newImage);

            Toast.makeText(this, "Contact updated", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        ImageView imageView = findViewById(R.id.detail_image);
        TextView nameView = findViewById(R.id.detail_name);
        TextView phoneView = findViewById(R.id.detail_phone);
        ImageButton btnCall = findViewById(R.id.btn_call);
        ImageButton btnMessage = findViewById(R.id.btn_message);
        ImageButton btnEdit = findViewById(R.id.btn_edit);
        ImageButton btnShare = findViewById(R.id.btn_share);

        // Get data from the intent
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        image = getIntent().getIntExtra("image", R.drawable.ic_launcher_background);

        // Set data to views
        nameView.setText(name);
        phoneView.setText(phone);
        imageView.setImageResource(image);

        // Set click listeners
        btnCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
        });

        btnMessage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("sms:" + phone));
            startActivity(intent);
        });

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(ContactDetailActivity.this, EditContactActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("phone", phone);
            intent.putExtra("image", image);
            startActivityForResult(intent, REQUEST_EDIT_CONTACT);
        });





        btnShare.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Contact: " + name + "\nPhone: " + phone);
            startActivity(Intent.createChooser(intent, "Share via"));
        });

        ImageView menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(this::showPopupMenu);
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.contact_menu); // Your menu XML file
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_back) {
                backContact();
                return true;
            } else if (item.getItemId() == R.id.action_generate_qr) {
                viewQRCode();
                return true;
            } else if (item.getItemId() == R.id.action_print) {
                try {
                    printContact();
                } catch (IOException e) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

    private void backContact() {
        Toast.makeText(this, "Contact List", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ContactDetailActivity.this, ContactActivity.class);
        startActivity(intent);
    }

    private void viewQRCode() {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(writer.encode("Contact: " + name + "\nPhone: " + phone, BarcodeFormat.QR_CODE, 400, 400));

            ImageView qrImageView = new ImageView(this);
            qrImageView.setImageBitmap(bitmap);

            new AlertDialog.Builder(this)
                    .setTitle("QR Code")
                    .setView(qrImageView)
                    .setPositiveButton("OK", null)
                    .show();
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to generate QR code", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("ResourceType")
    private void printContact() throws IOException {
        Toast.makeText(this, "Preparing contact for printing", Toast.LENGTH_SHORT).show();
        View printView = LayoutInflater.from(this).inflate(R.layout.activity_contact_detail, null);
        TextView nameView = printView.findViewById(R.id.detail_name);
        TextView phoneView = printView.findViewById(R.id.detail_phone);
        ImageView imageView = printView.findViewById(R.id.detail_image);
        nameView.setText(name);
        phoneView.setText(phone);
        imageView.setImageResource(image);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.getDisplay().getRealMetrics(displayMetrics);
        } else this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        printView.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY));
        printView.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(printView.getMeasuredWidth(), printView.getMeasuredHeight(), 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        printView.draw(canvas);
        pdfDocument.finishPage(page);

        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        String filename = name + "'s contact.pdf";
        File file = new File(downloadsDir, filename);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            pdfDocument.writeTo(fos);
            fos.close();
            Toast.makeText(this, "Contact printed successfully", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
           Log.d("ContactDetailActivity", "File not found");
           throw new RuntimeException(e);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
