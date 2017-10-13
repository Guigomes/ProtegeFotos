package ggsoftware.com.br.protegefotos;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import ggsoftware.com.br.protegefotos.dao.PastaDAO;
import ggsoftware.com.br.protegefotos.dao.PastaVO;

public class EscolherPastaActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_pasta);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        PastaDAO pastaDAO = new PastaDAO(EscolherPastaActivity.this);

        List<PastaVO> pastas = pastaDAO.listarPastas();

        List<String> nomePastas = new ArrayList();

        for (PastaVO pasta : pastas) {
            nomePastas.add(pasta.getNomePasta());
        }

        nomePastas.add("Safari");
        nomePastas.add("Camera");
        nomePastas.add("FireFox");
        nomePastas.add("Android");


        setListAdapter(new ArrayAdapter<String>(
                this, R.layout.item_list,
                R.id.Itemname, nomePastas));
    }

}
