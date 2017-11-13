package ggsoftware.com.br.protegefotos;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.List;

import ggsoftware.com.br.protegefotos.dao.PastaDAO;
import ggsoftware.com.br.protegefotos.dao.PastaVO;
import me.zhanghai.android.patternlock.ConfirmPatternActivity;
import me.zhanghai.android.patternlock.PatternUtils;
import me.zhanghai.android.patternlock.PatternView;

/**
 * Created by gomes on 31/07/17.
 */

public class SampleConfirmPatternActivity extends ConfirmPatternActivity {

    PastaDAO pastaDAO;

    @Override
    protected boolean isStealthModeEnabled() {
        // TODO: Return the value from SharedPreferences.
        return false;
    }

    @Override
    protected boolean isPatternCorrect(List<PatternView.Cell> pattern) {
        String padrao = PatternUtils.patternToSha1String(pattern);

        pastaDAO = new PastaDAO(SampleConfirmPatternActivity.this);
        if (MainActivity.isModoInvisivel()) {
            List<PastaVO> pastas = pastaDAO.listarPastas(true);
            for (PastaVO pasta :
                    pastas) {

                if (padrao.equals(pasta.getSenhaPasta())) {
                    SampleConfirmPatternActivity.pastaVO = pasta;
                    Intent it = new Intent(SampleConfirmPatternActivity.this, GlideActivity.class);
                    startActivity(it);
                    return true;
                }

            }

        } else {
            String patternSha1 = ConfirmPatternActivity.pastaVO.getSenhaPasta();
            if (TextUtils.equals(PatternUtils.patternToSha1String(pattern), patternSha1)) {
                Intent it = new Intent(SampleConfirmPatternActivity.this, GlideActivity.class);

                    finish();


                startActivity(it);
            } else {
                return false;
            }

        }
        return false;

    }


    @Override
    protected void onForgotPassword() {

        //startActivity(new Intent(this, YourResetPatternActivity.class));

        // Finish with RESULT_FORGOT_PASSWORD.
        super.onForgotPassword();
    }
}