package ggsoftware.com.br.protegefotos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;

import ggsoftware.com.br.protegefotos.dao.PastaDAO;
import ggsoftware.com.br.protegefotos.dao.PastaVO;


public class MainActivity extends AppCompatActivity {

    public static int CRIAR_NOVA_SENHA = 200;
    public static int CONFERIR_SENHA = 100;

    //static SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PastaDAO pastaDAO = new PastaDAO(MainActivity.this);

        List<PastaVO> listaPastas = pastaDAO.listarPastas();

//        setContentView(R.layout.activity_main);
/*
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        String padrao = getPadrao();
  */

        if (listaPastas.size() == 0) {
            Intent it = new Intent(MainActivity.this,
                    SampleSetPatternActivity.class);

            int idPasta = 0;

            it.putExtra("idPasta", idPasta);
            startActivityForResult(it, CRIAR_NOVA_SENHA);
        } else if (listaPastas.size() == 1) {
            PastaVO pasta = listaPastas.get(0);

            int idPasta = pasta.getId();

            Intent it = new Intent(MainActivity.this,
                    SampleConfirmPatternActivity.class);

            it.putExtra("idPasta", idPasta);

            startActivityForResult(it, CONFERIR_SENHA);
        } else {

            startActivity(new Intent(MainActivity.this, EscolherPastaActivity.class));

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
                    startActivity(it);
                    finish();

                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.msg_erro_criar_pasta), Toast.LENGTH_SHORT).show();
                }


            }


        } else if (resultCode == RESULT_OK && requestCode == CONFERIR_SENHA) {


            Intent it = new Intent(MainActivity.this, GlideActivity.class);
            startActivity(it);
            finish();

        } else if (resultCode == RESULT_CANCELED) {
            finish();
        }
    }

    /*
    public static String getPadrao() {

        return sharedPreferences.getString("padrao", null);

    }

    public static void setPadrao(String padrao) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("padrao", padrao);
        editor.commit();

    }
*/

}