package ggsoftware.com.br.protegefotos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by f3861879 on 15/08/17.
 */

public class ImageDAO {

    private SQLiteDatabase db;
    private CriaBanco banco;

    public ImageDAO(Context context){
        banco = new CriaBanco(context);
    }

    public String insereDado(String nome, String diretorio){
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(CriaBanco.NOME, nome);
        valores.put(CriaBanco.DIRETORIO, diretorio);


        resultado = db.insert(CriaBanco.TABELA, null, valores);
        db.close();

        if (resultado ==-1)
            return "Erro ao inserir registro";
        else
            return "Registro Inserido com sucesso";

    }
    public  List<ImagemVO> carregaDados(){
        List<ImagemVO> listaImagens = new ArrayList<>();
        Cursor rs;
        String[] campos =  {banco.ID,banco.NOME, banco.DIRETORIO};
        db = banco.getReadableDatabase();
        rs = db.query(banco.TABELA, campos, null, null, null, null, null, null);

        while (rs.moveToNext()) {
            ImagemVO imagemVO = new ImagemVO();
            imagemVO.setId(rs.getInt(rs.getColumnIndex(CriaBanco.ID)));
            imagemVO.setNome(rs.getString(rs.getColumnIndex(CriaBanco.NOME)));
            imagemVO.setDiretorio(rs.getString(rs.getColumnIndex(CriaBanco.DIRETORIO)));


            listaImagens.add(imagemVO);
        }
        rs.close();
        db.close();

        return listaImagens;
    }
}
