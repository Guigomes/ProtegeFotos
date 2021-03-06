package ggsoftware.com.br.protegefotos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;

import ggsoftware.com.br.protegefotos.dao.ImageDAO;
import ggsoftware.com.br.protegefotos.dao.ImagemVO;
import ggsoftware.com.br.protegefotos.dao.PastaDAO;
import ggsoftware.com.br.protegefotos.dao.PastaVO;


public class MainActivity extends AppCompatActivity {

    public static int CRIAR_NOVA_SENHA = 200;

    public static int ALTERAR_SENHA = 300;

    public static int CONFERIR_SENHA = 100;

    static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PastaDAO pastaDAO = new PastaDAO(MainActivity.this);

        List<PastaVO> listaPastas = pastaDAO.listarPastas(false);

        ImageDAO imagemDAO = new ImageDAO(MainActivity.this);


//        setContentView(R.layout.activity_main);

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        if (MainActivity.isModoMisto()) {
            startActivity(new Intent(MainActivity.this, EscolherPastaActivity.class));
            finish();

        } else if (MainActivity.isModoInvisivel()) {

            Intent it = new Intent(MainActivity.this, SampleConfirmPatternActivity.class);
            startActivityForResult(it, CONFERIR_SENHA);

        } else if (listaPastas.size() == 0) {
            Intent it = new Intent(MainActivity.this,
                    SampleSetPatternActivity.class);

            int idPasta = 0;

            it.putExtra("idPasta", idPasta);
            startActivityForResult(it, CRIAR_NOVA_SENHA);
        } else {

            startActivity(new Intent(MainActivity.this, EscolherPastaActivity.class));
            finish();

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == CRIAR_NOVA_SENHA) {

            int idPasta = (int) data.getExtras().get("idPasta");
            String pattern = (String) data.getExtras().get("pattern");

            if (idPasta == 0) {
                String nomePasta = getString(R.string.txt_pasta_principal);

                PastaDAO pastaDAO = new PastaDAO(MainActivity.this);


                boolean sucesso = pastaDAO.salvarPasta(nomePasta, pattern);

                if (sucesso) {
                    Toast.makeText(MainActivity.this, getString(R.string.msg_sucesso_criar_pasta), Toast.LENGTH_SHORT).show();

                    Intent it = new Intent(MainActivity.this, GlideActivity.class);
                    it.putExtra("nomePasta", nomePasta);
                    finish();
                    startActivity(it);

                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.msg_erro_criar_pasta), Toast.LENGTH_SHORT).show();
                }


            }


        } else if (resultCode == RESULT_OK && requestCode == CONFERIR_SENHA) {


            Intent it = new Intent(MainActivity.this, GlideActivity.class);
            startActivity(it);


        } else if (resultCode == RESULT_CANCELED) {
            finish();
        }
    }


    public static boolean isModoInvisivel() {

        return sharedPreferences.getBoolean("isModoInvisivel", false);

    }

    public static void setModoInvisivel(boolean isModoInvisivel) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isModoInvisivel", isModoInvisivel);
        editor.commit();

    }

    public static boolean isModoMisto() {
if(sharedPreferences != null) {
    return sharedPreferences.getBoolean("isModoMisto", false);
}else{
    return  false;
}
    }

    public static void setModoMisto(boolean isModoMisto) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isModoMisto", isModoMisto);
        editor.commit();

    }

}
