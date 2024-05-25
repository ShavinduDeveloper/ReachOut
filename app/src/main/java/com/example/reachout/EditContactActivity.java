package com.example.reachout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditContactActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editName;
    private EditText editPhone;
    private ImageView editImage;
    private Button btnSelectImage;
    private Button btnSave;

    private Uri imageUri;
    private Bitmap selectedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        editName = findViewById(R.id.edit_name);
        editPhone = findViewById(R.id.edit_phone);
        editImage = findViewById(R.id.edit_image);
        btnSelectImage = findViewById(R.id.btn_select_image);
        btnSave = findViewById(R.id.btn_save);

        // Get data from the intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        int imageResource = intent.getIntExtra("image", R.drawable.ic_launcher_background);

        // Set data to views
        editName.setText(name);
        editPhone.setText(phone);
        editImage.setImageResource(imageResource);

        btnSelectImage.setOnClickListener(v -> openImageSelector());

        btnSave.setOnClickListener(v -> {
            // Get edited data
            String newName = editName.getText().toString().trim();
            String newPhone = editPhone.getText().toString().trim();

            if (newName.isEmpty() || newPhone.isEmpty()) {
                Toast.makeText(EditContactActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the image from ImageView
            Bitmap bitmap = ((BitmapDrawable) editImage.getDrawable()).getBitmap();

            // Convert bitmap to byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            // Return edited data to ContactDetailActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("new_name", newName);
            resultIntent.putExtra("new_phone", newPhone);
            resultIntent.putExtra("new_image", byteArray);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void openImageSelector() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                editImage.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
