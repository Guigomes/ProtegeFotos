package ggsoftware.com.br.protegefotos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;

public class SpacePhotoActivity extends AppCompatActivity {

    public static final String EXTRA_SPACE_PHOTO = "SpacePhotoActivity.SPACE_PHOTO";
    private ImageView mImageView;

    SpacePhoto spacePhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_photo);

        mImageView = (ImageView) findViewById(R.id.image);
         spacePhoto = getIntent().getParcelableExtra(EXTRA_SPACE_PHOTO);

        ImageSaver imageSaver = new ImageSaver(SpacePhotoActivity.this);
        File file = imageSaver.loadFile(spacePhoto.getTitle());


        Glide.with(this)
                .load(file.getAbsolutePath())
                .asBitmap()
                .error(android.R.drawable.ic_delete)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mImageView);
    }

    public void nextFoto(View v){
        SpacePhoto[] fotos = SpacePhoto.getFotos();
for(int i = 0;i< fotos.length; i++){
    if(fotos[i].getId() == spacePhoto.getId()){
        if(i < fotos.length -1){
            spacePhoto = fotos[i+1];
        }else{
            spacePhoto = fotos[0];
        }
        ImageSaver imageSaver = new ImageSaver(SpacePhotoActivity.this);
        File file = imageSaver.loadFile(spacePhoto.getTitle());


        Glide.with(this)
                .load(file.getAbsolutePath())
                .asBitmap()
                .error(android.R.drawable.ic_delete)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mImageView);

        break;
    }
}
        Log.i("TESTE","TESTE");

    }
}