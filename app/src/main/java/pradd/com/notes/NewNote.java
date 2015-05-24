package pradd.com.notes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import RichTextEditor.CustomEditText.CustomEditText;
import yuku.ambilwarna.AmbilWarnaDialog;

import static RichTextEditor.CustomEditText.CustomEditText.EventBack;

/**
 * Created by Pradipta on 18-04-2015.
 */
public class NewNote extends ActionBarActivity implements AmbilWarnaDialog.OnAmbilWarnaListener {
    private LinearLayout lnl;
    private CustomEditText customEditor;
    private AmbilWarnaDialog colorPickerDialog;
    private ImageView imgChangeColor;

    private int selectionStart;
    private int selectionEnd;

    private EventBack eventBack = new EventBack() {

        @Override
        public void close() {
            lnl.setVisibility(View.GONE);
        }

        @Override
        public void show() {
            lnl.setVisibility(View.VISIBLE);
        }
    };
    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (customEditor.isFocused()) {
                lnl.setVisibility(View.VISIBLE);
            }
        }
    };
    private EditText ext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note);
        colorPickerDialog = new AmbilWarnaDialog(this, Color.BLACK, this);
        ToggleButton boldToggle = (ToggleButton) findViewById(R.id.btnBold);
        ToggleButton italicsToggle = (ToggleButton) findViewById(R.id.btnItalics);
        ToggleButton underlinedToggle = (ToggleButton) findViewById(R.id.btnUnderline);
        imgChangeColor = (ImageView) findViewById(R.id.btnChangeTextColor);
        lnl = (LinearLayout) findViewById(R.id.lnlAction);
        lnl.setVisibility(View.VISIBLE);

        customEditor = (CustomEditText) findViewById(R.id.editor);
        ext = (EditText) findViewById(R.id.editText);
        customEditor.setHint("");
        customEditor.setSingleLine(false);
        customEditor.setMinLines(10);
        customEditor.setBoldToggleButton(boldToggle);
        customEditor.setItalicsToggleButton(italicsToggle);
        customEditor.setUnderlineToggleButton(underlinedToggle);
        customEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    lnl.setVisibility(View.VISIBLE);
                }
                else
                {
                    lnl.setVisibility(View.GONE);
                }
            }
        });
        customEditor.setEventBack(eventBack);
        customEditor.setOnClickListener(clickListener);
        imgChangeColor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectionStart = customEditor.getSelectionStart();
                selectionEnd = customEditor.getSelectionEnd();
                colorPickerDialog.show();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done){
            Log.i("Submission", "Done");
            submitNote();
            return true;
        }
        Log.i("Submission","Nope");
        return super.onOptionsItemSelected(item);
    }
    public void submitNote(){
        Log.i("Success?","Called");
        ext = (EditText) findViewById(R.id.editText);
        String text = customEditor.getTextHTML();
        String title = String.valueOf(ext.getText());
        Intent back = new Intent();
        Bundle b=new Bundle();
        b.putStringArray("Array", new String[]{title, text});
        back.putExtras(b);
        setResult(RESULT_OK,back);
        finish();
    }


    @Override
    public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {

    }

    @Override
    public void onOk(AmbilWarnaDialog ambilWarnaDialog, int i) {
        customEditor.setColor(i, selectionStart, selectionEnd);
        imgChangeColor.setBackgroundColor(i);
    }
}
