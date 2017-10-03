package ggsoftware.com.br.protegefotos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;



import java.util.List;



public class MainActivity extends AppCompatActivity   {

    static SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        String padrao = getPadrao();

        if (padrao == null) {

            Intent it = new Intent(MainActivity.this,
                    SampleSetPatternActivity.class);
            startActivityForResult(it, 200);

        } else {

            Intent it = new Intent(MainActivity.this,
                    SampleConfirmPatternActivity.class);

            startActivityForResult(it, 100);

        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK
                && (requestCode == 100 || requestCode == 200)) {

            Intent it = new Intent(MainActivity.this, GlideActivity.class);
            startActivity(it);
            finish();

        }else if(resultCode == RESULT_CANCELED){
            finish();
        }
    }

    public static String getPadrao() {

        return sharedPreferences.getString("padrao", null);

    }

    public static void setPadrao(String padrao) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("padrao", padrao);
        editor.commit();

    }


}
