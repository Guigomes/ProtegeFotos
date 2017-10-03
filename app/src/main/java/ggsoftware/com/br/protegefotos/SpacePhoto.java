package ggsoftware.com.br.protegefotos;

/**
 * Created by f3861879 on 02/10/2017.
 */

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ggsoftware.com.br.protegefotos.dao.ImageDAO;
import ggsoftware.com.br.protegefotos.dao.ImagemVO;

public class SpacePhoto implements Parcelable {

    private String mUrl;
    private String mTitle;
    private int id;

    private static SpacePhoto[] fotos;

    public static SpacePhoto[] getFotos(){

        return fotos;
    }

    public  int getId(){

        return id;
    }


    public SpacePhoto(String url, String title, int id) {
        mUrl = url;
        this.id = id;
        mTitle = title;
    }

    protected SpacePhoto(Parcel in) {
        mUrl = in.readString();
        mTitle = in.readString();
        id  = in.readInt();
    }

    public static final Creator<SpacePhoto> CREATOR = new Creator<SpacePhoto>() {
        @Override
        public SpacePhoto createFromParcel(Parcel in) {
            return new SpacePhoto(in);
        }

        @Override
        public SpacePhoto[] newArray(int size) {
            return new SpacePhoto[size];
        }
    };

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public static  SpacePhoto[] getSpacePhotos(Context context) {

        ImageDAO imageDAO = new ImageDAO(context);
        List<ImagemVO> imagens = imageDAO.carregaDados();

        SpacePhoto[] photos = new SpacePhoto[imagens.size()];
        int i = 0;
        for(ImagemVO imagemVO : imagens){
            photos[i] = new SpacePhoto(imagemVO.getDiretorio(), imagemVO.getNome(), imagemVO.getId());
            i++;

        }
        fotos = photos;
        return fotos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mUrl);
        parcel.writeString(mTitle);
        parcel.writeInt(id);

    }
}