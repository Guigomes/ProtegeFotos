package ggsoftware.com.br.protegefotos.dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

import ggsoftware.com.br.protegefotos.GaleriaActivity;
import ggsoftware.com.br.protegefotos.ImageSaver;
import ggsoftware.com.br.protegefotos.R;

/**
 * Created by f3861879 on 16/08/17.
 */

public class ImageAdapter extends BaseAdapter {

    private ImageSaver imageSaver;
    //  imageView = (ImageView) findViewById(R.id.imagem);
    private ImageDAO imageDAO;

    private Context mContext;
    List<ImagemVO> imagens;
    public ImageAdapter(Context c) {
        mContext = c;
        imageSaver = new ImageSaver(c);
        //  imageView = (ImageView) findViewById(R.id.imagem);
        imageDAO = new ImageDAO(c);
        imagens = imageDAO.carregaDados();



    }

    public int getCount() {
        return imagens.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }




            Bitmap bitmap = new ImageSaver(mContext).
                    setFileName(imagens.get(position).getNome()).
                    setDirectoryName(imagens.get(position).getDiretorio()).
                    load();
        imageView.setImageBitmap(bitmap);


        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,
    };
}