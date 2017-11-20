package ggsoftware.com.br.protegefotos;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ggsoftware.com.br.protegefotos.dao.PastaDAO;
import ggsoftware.com.br.protegefotos.dao.PastaVO;
import me.zhanghai.android.patternlock.ConfirmPatternActivity;

public class EscolherPastaActivity extends AppCompatActivity {

    private String m_Text;

    PastaDAO pastaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_pasta);

        final List<String> nomePastas;


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EscolherPastaActivity.this);
                builder.setTitle(getString(R.string.txt_informe_nome_pasta));


                final EditText input = new EditText(EscolherPastaActivity.this);

                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setTextColor(Color.BLACK);
                builder.setView(input);

                builder.setPositiveButton(getString(R.string.btn_criar_pasta), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();

                        Intent it = new Intent(EscolherPastaActivity.this, SampleSetPatternActivity.class);

                        it.putExtra("idPasta", -1);
                        it.putExtra("nomePasta", m_Text);
                        startActivityForResult(it, MainActivity.CRIAR_NOVA_SENHA);
                    }
                });

                builder.setNegativeButton(getString(R.string.btn_cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

         pastaDAO = new PastaDAO(EscolherPastaActivity.this);

        List<PastaVO> pastas = pastaDAO.listarPastas(false);

        ListView listaPastas = (ListView) findViewById(R.id.lista_pastas);

        nomePastas = new ArrayList();

        for (PastaVO pasta : pastas) {
            nomePastas.add(pasta.getNomePasta());
        }



        listaPastas.setAdapter(new ArrayAdapter<String>(
                this, R.layout.item_list,
                R.id.Itemname, nomePastas));

        listaPastas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nomePastaEscolhida = nomePastas.get(position);

                Intent it = new Intent(EscolherPastaActivity.this, SampleConfirmPatternActivity.class);
                it.putExtra("nomePasta", nomePastaEscolhida);
                startActivityForResult(it, MainActivity.CONFERIR_SENHA);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == MainActivity.CRIAR_NOVA_SENHA) {

            String nomePasta = (String) data.getExtras().get("nomePasta");
            String pattern = (String) data.getExtras().get("pattern");


            PastaDAO pastaDAO = new PastaDAO(EscolherPastaActivity.this);
            PastaVO pastaVO = pastaDAO.buscarPorNome(nomePasta);
            if (pastaVO == null) {
                boolean sucesso = pastaDAO.salvarPasta(nomePasta, pattern);


                if (sucesso) {
                    Toast.makeText(EscolherPastaActivity.this, getString(R.string.msg_sucesso_criar_pasta), Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(EscolherPastaActivity.this, GlideActivity.class);
                    it.putExtra("nomePasta", nomePasta);
                    finish();

                    startActivity(it);


                } else {
                    Toast.makeText(EscolherPastaActivity.this, getString(R.string.msg_erro_criar_pasta), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(EscolherPastaActivity.this, getString(R.string.msg_pasta_repetida), Toast.LENGTH_SHORT).show();

            }


        } else if (resultCode == RESULT_OK && requestCode == MainActivity.CONFERIR_SENHA) {



            Intent it = new Intent(EscolherPastaActivity.this, GlideActivity.class);
            finish();


            startActivity(it);


        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(!MainActivity.isModoMisto()) {
            getMenuInflater().inflate(R.menu.menu_pasta, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_ativar_modo_invisivel:
                ativarModoInvisivel();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }


    public void ativarModoInvisivel(){
        MainActivity.setModoInvisivel(true);

        List<PastaVO> pastas = pastaDAO.listarPastas(false);

        for(PastaVO pasta : pastas){
            pasta.setInvisivel(1);
            pastaDAO.updatePasta(pasta);
        }
        
        Intent it = new Intent(EscolherPastaActivity.this, SampleConfirmPatternActivity.class);
        it.putExtra("isModoInvisivel", true);
        finish();
        startActivity(it);
    }


}
