package br.com.fiomaravilhabarbearia.fio_maravilha;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.github.pinball83.maskededittext.MaskedEditText;

/**
 * Created by fraps on 03/02/17.
 */

public class MyEditText extends MaskedEditText {

    public MyEditText(Context context) {
        super(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
     }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
     }

}
