package ggsoftware.com.br.protegefotos.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by f3861879 on 15/08/17.
 */

public class CriaBanco  extends SQLiteOpenHelper {

    public static final String NOME_BANCO = "protegefotos.db";
    public static final String TABELA = "imagens";
    public static final String ID = "id";
    public static final String NOME = "nome";
    public static final String DIRETORIO = "diretorio";

    public static final int VERSAO = 1;


    public CriaBanco(Context context) {
        super(context, NOME_BANCO,null,VERSAO);    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABELA+"("
                + ID + " integer primary key autoincrement,"
                + NOME + " text,"
                + DIRETORIO + " text"
                +")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABELA);
        onCreate(db);

    }
}
