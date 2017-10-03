package ggsoftware.com.br.protegefotos;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.OpenableColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import ggsoftware.com.br.protegefotos.dao.ImageAdapter;
import ggsoftware.com.br.protegefotos.dao.ImageDAO;
import ggsoftware.com.br.protegefotos.dao.ImagemVO;

public class GaleriaActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    ImageSaver imageSaver;
    ImageView imageView;
    ImageDAO imageDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        imageSaver = new ImageSaver(GaleriaActivity.this);
       //  imageView = (ImageView) findViewById(R.id.imagem);
        imageDAO = new ImageDAO(GaleriaActivity.this);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(GaleriaActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void abrirPicker(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {


            case PICK_IMAGE:
                Uri selectedImage = imageReturnedIntent.getData();

                String filename = queryName(getContentResolver(), selectedImage);
                Toast.makeText(getApplicationContext(), filename, Toast.LENGTH_SHORT).show();


/*
                String FILENAME = "hello_file";

                FileOutputStream fos = null;
                try {
                    fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                    fos.write(uriToByteArray(selectedImage.toString()));
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

*/

                try {
                    Bitmap bitmap = Images.Media.getBitmap(this.getContentResolver(), selectedImage);
//                    imageView.setImageBitmap(bitmap);

                    imageSaver.save(bitmap,  filename);
                    imageDAO.insereDado(filename, "images");
                    Toast.makeText(getApplicationContext(), "UHUU", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }


    }



    public void load(View v){

/*
        List<ImagemVO> imagens = imageDAO.carregaDados();
        LinearLayout container = (LinearLayout) findViewById(R.id.container);

        for(ImagemVO imagem : imagens){

            ImageView imagemView = new ImageView(GaleriaActivity.this);

            Bitmap bitmap = new ImageSaver(GaleriaActivity.this).
                    setFileName(imagem.getNome()).
                    setDirectoryName(imagem.getDiretorio()).
                    load();
            imagemView.setImageBitmap(bitmap);
            container.addView(imagemView);

        }
        Toast.makeText(getApplicationContext(), "CARREGOU", Toast.LENGTH_SHORT).show();

*/

    }

    private String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }


}
