package com.mygdx.game.android;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ram52.princess.R;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public LinearLayout layoutBox;
    private Display display;

    public AndroidLauncher getLauncher() {
        return launcher;
    }

    public void setLauncher(AndroidLauncher launcher) {
        this.launcher = launcher;
    }

    private AndroidLauncher launcher;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                launcher.created = false;
                c.finish();
                break;
            case R.id.btn_no:
                dismiss();
                launcher.created = false;
                break;
            default:
                break;
        }
        dismiss();
    }
}