package ggsoftware.com.br.protegefotos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by f3861879 on 15/08/17.
 */

public class PastaDAO {

    private SQLiteDatabase db;
    private CriaBanco banco;

    public PastaDAO(Context context){
        banco = new CriaBanco(context);
    }

    public boolean salvarPasta(String nomePasta, String senhaPasta){
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(CriaBanco.NOME_PASTA, nomePasta);
        valores.put(CriaBanco.TIMESTAMP_CRIACAO_PASTA, String.valueOf(Calendar.getInstance().getTimeInMillis()));
        valores.put(CriaBanco.SENHA_PASTA, senhaPasta);


        resultado = db.insert(CriaBanco.TABELA_PASTA, null, valores);
        db.close();

        if (resultado ==-1)
            return false;
        else
            return true;

    }
    public  List<PastaVO> listarPastas(){
        List<PastaVO> listaPastas = new ArrayList<>();
        Cursor rs;
        String[] campos =  {banco.ID,banco.NOME_PASTA, banco.TIMESTAMP_CRIACAO_PASTA, banco.SENHA_PASTA};

        db = banco.getReadableDatabase();
        rs = db.query(banco.TABELA_PASTA, campos, null, null, null, null, null, null);

        while (rs.moveToNext()) {
            PastaVO pastaVO = new PastaVO();
            pastaVO.setId(rs.getInt(rs.getColumnIndex(CriaBanco.ID)));
            pastaVO.setNomePasta(rs.getString(rs.getColumnIndex(CriaBanco.NOME_PASTA)));
            pastaVO.setTimestampCriacaoPasta(rs.getString(rs.getColumnIndex(CriaBanco.TIMESTAMP_CRIACAO_PASTA)));
            pastaVO.setSenhaPasta(rs.getString(rs.getColumnIndex(CriaBanco.SENHA_PASTA)));

            listaPastas.add(pastaVO);
        }
        rs.close();
        db.close();

        return listaPastas;
    }

    public PastaVO buscarPorId(int idPasta) {

        PastaVO pastaVO = new PastaVO();
        Cursor rs;
        String[] campos = {banco.ID, banco.NOME_PASTA, banco.TIMESTAMP_CRIACAO_PASTA, banco.SENHA_PASTA};
        String where = banco.ID + " = ?";
        String[] argumentos = {String.valueOf(idPasta)};
        db = banco.getReadableDatabase();
        rs = db.query(banco.TABELA_PASTA, campos, where, argumentos, null, null, null, null);

        while (rs.moveToNext()) {

            pastaVO.setId(rs.getInt(rs.getColumnIndex(CriaBanco.ID)));
            pastaVO.setNomePasta(rs.getString(rs.getColumnIndex(CriaBanco.NOME_PASTA)));
            pastaVO.setTimestampCriacaoPasta(rs.getString(rs.getColumnIndex(CriaBanco.TIMESTAMP_CRIACAO_PASTA)));
            pastaVO.setSenhaPasta(rs.getString(rs.getColumnIndex(CriaBanco.SENHA_PASTA)));


        }
        rs.close();
        db.close();

        return pastaVO;
    }

    public PastaVO buscarPorNome(String nomePasta) {

        PastaVO pastaVO = null;
        Cursor rs;
        String[] campos = {banco.ID, banco.NOME_PASTA, banco.TIMESTAMP_CRIACAO_PASTA, banco.SENHA_PASTA};
        String where = banco.NOME_PASTA + " = ?";
        String[] argumentos = {nomePasta};
        db = banco.getReadableDatabase();
        rs = db.query(banco.TABELA_PASTA, campos, where, argumentos, null, null, null, null);

        while (rs.moveToNext()) {
            pastaVO = new PastaVO();
            pastaVO.setId(rs.getInt(rs.getColumnIndex(CriaBanco.ID)));
            pastaVO.setNomePasta(rs.getString(rs.getColumnIndex(CriaBanco.NOME_PASTA)));
            pastaVO.setTimestampCriacaoPasta(rs.getString(rs.getColumnIndex(CriaBanco.TIMESTAMP_CRIACAO_PASTA)));
            pastaVO.setSenhaPasta(rs.getString(rs.getColumnIndex(CriaBanco.SENHA_PASTA)));


        }
        rs.close();
        db.close();

        return pastaVO;
    }
}

