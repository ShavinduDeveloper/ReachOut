package com.example.reachout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class CreateContactActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText etName, etPhone;
    private ImageView ivBack;
    private Button btnSave;
    private Bitmap selectedBitmap;
    private Uri imageUri;
    private Button btnSelectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        etName = findViewById(R.id.edit_name);
        etPhone = findViewById(R.id.edit_phone);
        ivBack = findViewById(R.id.image_view);
        btnSelectImage = findViewById(R.id.btn_select_image);
        btnSave = findViewById(R.id.btn_save_contact);

        ivBack.setImageResource(R.drawable.ic_launcher_foreground);

        btnSelectImage.setOnClickListener(v -> openImageSelector());

        btnSave.setOnClickListener(v -> {
            // Get the entered name and phone number
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            // Validate input
            if (name.isEmpty() || phone.isEmpty() || selectedBitmap == null) {
                Toast.makeText(this, "Please enter name, phone number, and select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create an intent to pass back the new contact data to ContactActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("name", name);
            resultIntent.putExtra("phone", phone);
            resultIntent.putExtra("imageUri", imageUri.toString());
            setResult(RESULT_OK, resultIntent);
            finish(); // Close this activity and return to ContactActivity
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
                ivBack.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
