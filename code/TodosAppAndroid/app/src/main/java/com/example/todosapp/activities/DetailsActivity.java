package com.example.todosapp.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.todosapp.R;
import com.example.todosapp.datamodel.Item;
import com.example.todosapp.properties.ApplicationProperties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class DetailsActivity extends AppCompatActivity {

    private final static int ADD_FILE_REQ_CODE = 10;
    private final static int PERMISSION_CODE_STORAGE = 20;

    private EditText m_title;
    private EditText m_description;
    private Switch m_statusSwitch;
    private Button m_image;
    private ImageView m_imageView;
    private Uri imageUri = Uri.parse("");
    ApplicationProperties m_properties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        m_properties = ApplicationProperties.getINSTANCE();
        if (m_properties.getMode() == 0) {
            getSupportActionBar().setTitle("Add item");       // Checking if the item is new item or editing the existing item
            invalidateOptionsMenu();
        }
        else if (m_properties.getMode() == 1) {
            getSupportActionBar().setTitle("Edit item");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        m_title = (EditText) findViewById(R.id.editText_title);
        m_description = (EditText) findViewById(R.id.editText_description);
        m_statusSwitch = (Switch) findViewById(R.id.statusSwitch);
        m_image = (Button) findViewById(R.id.image_button);
        m_imageView = (ImageView) findViewById(R.id.image_view);
        if (m_properties.getMode() == 1) {
            Item item = m_properties.getCategoryList().get(m_properties.getSelectedCategory()).getItems().get(m_properties.getSelectedItem());
            m_title.setText(item.getTitle());
            m_description.setText(item.getDescription());
            m_statusSwitch.setChecked(item.isDone());
            if (!item.getAttachment().isEmpty() || !item.getAttachment().equals(null)) {
                Bitmap bitmap = null;
                try {
                    imageUri = Uri.parse(item.getAttachment());
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(item.getAttachment()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                m_imageView.setImageBitmap(bitmap);
            }
        }

        m_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(DetailsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetailsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE_STORAGE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(intent, ADD_FILE_REQ_CODE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        if (m_properties.getMode() == 0) {
            MenuItem deleItem = menu.findItem(R.id.action_delete);
            deleItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            if (m_title.getText().toString().isEmpty() || m_description.getText().toString().isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                builder.setMessage("Title and Description are mandatory");
                builder.setCancelable(true);
                builder.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
            } else {
                if (m_properties.getMode() == 0) {
                    Item newItem = new Item(m_title.getText().toString(), m_description.getText().toString(), m_statusSwitch.isChecked(), imageUri.toString());
                    m_properties.getCategoryList().get(m_properties.getSelectedCategory()).getItems().add(newItem);
                    m_title.setText("");
                    m_description.setText("");
                    m_statusSwitch.setChecked(false);
                    m_imageView.setImageBitmap(null);
                    Toast.makeText(this, getString(R.string.successSave), Toast.LENGTH_SHORT).show();
                } else if (m_properties.getMode() == 1) {
                        Item edititem = m_properties.getCategoryList().get(m_properties.getSelectedCategory()).getItems().get(m_properties.getSelectedItem());
                        edititem.setTitle(m_title.getText().toString());
                        edititem.setDescription(m_description.getText().toString());
                        edititem.setStatus(m_statusSwitch.isChecked());
                        edititem.setAttachment(imageUri.toString());
                    Toast.makeText(this,getString(R.string.successUpdate), Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (item.getItemId() == R.id.action_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
            builder.setMessage(getString(R.string.delete));
            builder.setCancelable(true);                    //  Deleting the existing item.
            builder.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            m_properties.getCategoryList().get(m_properties.getSelectedCategory()).getItems().remove(m_properties.getSelectedItem());
                            dialog.cancel();
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alert11 = builder.create();
            alert11.show();
        }

        if(item.getItemId() == android.R.id.home){
            setResult(RESULT_OK);     // Home button to move back.
            finish();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE_STORAGE:
                if (permissions.length == 1) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivityForResult(intent, ADD_FILE_REQ_CODE);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                        builder.setMessage(R.string.please_grant_necessary_permission);
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case ADD_FILE_REQ_CODE:
                if (resultCode == RESULT_OK) {

                    if (data.getData() != null) {
                        Uri uri = data.getData();
                        imageUri = uri;
                        File newFile = new File(uri.getPath());
                        try {
                            ContentResolver contentResolver = getContentResolver();
                            InputStream ips = contentResolver.openInputStream(uri);

                            String mimeType = getFileType(contentResolver.getType(uri));
                            if (TextUtils.isEmpty(mimeType)) {
                                Toast.makeText(this, R.string.invalidFile, Toast.LENGTH_LONG).show();
                            } else {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
                                m_imageView.setImageBitmap(bitmap);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;

        }
    }

    public static String getFileType(String fileName) {
        if (fileName.endsWith(".txt") || fileName.endsWith(".docx") || fileName.contains("docx") || fileName.contains("txt")) {
            return "text/plain";
        } else if (fileName.endsWith(".pdf") || fileName.contains("pdf")) {
            return "application/pdf";
        } else if (fileName.endsWith(".png") || fileName.contains("png")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith("jpeg") || fileName.contains("jpeg") || fileName.contains("jpg")) {
            return "image/jpeg";
        }
        return "";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }
}
