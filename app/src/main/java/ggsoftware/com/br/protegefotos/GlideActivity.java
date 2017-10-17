package ggsoftware.com.br.protegefotos;


import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ggsoftware.com.br.protegefotos.dao.ImageDAO;
import ggsoftware.com.br.protegefotos.dao.ImagemVO;
import ggsoftware.com.br.protegefotos.dao.PastaDAO;
import ggsoftware.com.br.protegefotos.dao.PastaVO;
import me.zhanghai.android.patternlock.ConfirmPatternActivity;

public class GlideActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private static final int PICK_IMAGE = 1;
    PastaVO pastaSelecionada;
    List<ImagemVO> listaImagens;
    ImageDAO imagemDAO;
    private ProgressBar spinner;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_galeria, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_imagem:
                addImagem();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }



}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);

        pastaSelecionada = ConfirmPatternActivity.pastaVO;

        if (pastaSelecionada == null) {
            PastaDAO pastaDAO = new PastaDAO(GlideActivity.this);
            String nomePasta = (String) getIntent().getExtras().get("nomePasta");

            pastaSelecionada = pastaDAO.buscarPorNome(nomePasta);
        }

        myToolbar.setTitle(pastaSelecionada.getNomePasta());
        setSupportActionBar(myToolbar);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4);
        recyclerView = (RecyclerView) findViewById(R.id.rv_images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

         imagemDAO = new ImageDAO(GlideActivity.this);


        listaImagens = imagemDAO.listarPorPasta(pastaSelecionada.getNomePasta());
        if (listaImagens.size() == 0) {
            ((TextView) findViewById(R.id.txt_pasta_vazia)).setVisibility(View.VISIBLE);
        } else {
            ((TextView) findViewById(R.id.txt_pasta_vazia)).setVisibility(View.GONE);
        }
        ImageGalleryAdapter adapter = new ImageGalleryAdapter(this, SpacePhoto.getSpacePhotos(listaImagens));
        recyclerView.setAdapter(adapter);


    }

    public void addImagem() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {


            case PICK_IMAGE:

                ClipData clipData = imageReturnedIntent.getClipData();
                if (clipData != null) {
                    spinner.setVisibility(View.VISIBLE);

                    new SalvarImagens().execute(clipData);
                } else {
                    Uri selectedImage = imageReturnedIntent.getData();
                    spinner.setVisibility(View.VISIBLE);

                    new SalvarImagem().execute(selectedImage);
                }


        }

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


    private class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder> {

        @Override
        public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View photoView = inflater.inflate(R.layout.custom_item, parent, false);
            ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(photoView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {

            SpacePhoto spacePhoto = mSpacePhotos[position];
            ImageView imageView = holder.mPhotoImageView;

            ImageSaver imageSaver = new ImageSaver(GlideActivity.this);
            File file = imageSaver.loadFile(spacePhoto.getTitle());


            Glide.with(mContext)
                    .load(file.getAbsolutePath())
                    .asBitmap()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);
        }

        @Override
        public int getItemCount() {
            return (mSpacePhotos.length);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView mPhotoImageView;

            public MyViewHolder(View itemView) {

                super(itemView);
                mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    SpacePhoto spacePhoto = mSpacePhotos[position];
                    Intent intent = new Intent(mContext, SpacePhotoActivity.class);
                    intent.putExtra(SpacePhotoActivity.EXTRA_SPACE_PHOTO, spacePhoto);
                    startActivity(intent);
                }
            }
        }

        private SpacePhoto[] mSpacePhotos;
        private Context mContext;

        public ImageGalleryAdapter(Context context, SpacePhoto[] spacePhotos) {
            mContext = context;
            mSpacePhotos = spacePhotos;

        }
    }

    private class SalvarImagem extends AsyncTask<Uri, Void, Boolean> {
        protected Boolean doInBackground(Uri... uri) {

            Uri selectedImage = uri[0];

            try {
                String filename = queryName(getContentResolver(), selectedImage);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(GlideActivity.this.getContentResolver(), selectedImage);
                ImageSaver imageSaver = new ImageSaver(GlideActivity.this);
                ImageDAO imageDAO = new ImageDAO(GlideActivity.this);
                imageSaver.save(bitmap, filename);
                imageDAO.insereDado(filename, pastaSelecionada.getNomePasta());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                listaImagens = imagemDAO.listarPorPasta(pastaSelecionada.getNomePasta());
                ImageGalleryAdapter adapter = new ImageGalleryAdapter(GlideActivity.this, SpacePhoto.getSpacePhotos(listaImagens));
                recyclerView.setAdapter(adapter);

            } else {
                Toast.makeText(getApplicationContext(), "Falha ao salvar imagem", Toast.LENGTH_SHORT).show();
            }
            spinner.setVisibility(View.GONE);

        }
    }

    private class SalvarImagens extends AsyncTask<ClipData, Void, Boolean> {
        protected Boolean doInBackground(ClipData... clipdatas) {
            ClipData clipData = clipdatas[0];

            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item item = clipData.getItemAt(i);
                Uri uri = item.getUri();

                try {
                    String filename = queryName(getContentResolver(), uri);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(GlideActivity.this.getContentResolver(), uri);


                    ImageSaver imageSaver = new ImageSaver(GlideActivity.this);
                    ImageDAO imageDAO = new ImageDAO(GlideActivity.this);
                    imageSaver.save(bitmap, filename);
                    imageDAO.insereDado(filename, pastaSelecionada.getNomePasta());

                } catch (IOException e) {
                    e.printStackTrace();

                    return false;
                }


            }
            return true;
        }


        protected void onPostExecute(Boolean result) {

            listaImagens = imagemDAO.listarPorPasta(pastaSelecionada.getNomePasta());
            ImageGalleryAdapter adapter = new ImageGalleryAdapter(GlideActivity.this, SpacePhoto.getSpacePhotos(listaImagens));
            recyclerView.setAdapter(adapter);

            spinner.setVisibility(View.GONE);


        }
    }
}