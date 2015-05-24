package pradd.com.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Pradipta on 23-05-2015.
 */
public class ViewNote extends ActionBarActivity {
    private TextView heading;
    private TextView content;
    private String[] data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_note);
        Intent callingIntent = getIntent();
        Bundle received = callingIntent.getExtras();
        data = received.getStringArray("Array");
        heading = (TextView) findViewById(R.id.heading);
        heading.setText(data[0]);
        content = (TextView) findViewById(R.id.textView);
        Spanned htm = Html.fromHtml(data[1]);
        content.setText(htm);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit_note){
            Intent action = new Intent(this, EditNote.class);
            Bundle b = new Bundle();
            b.putStringArray("Array", data);
            action.putExtras(b);
            startActivityForResult(action, 2);

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent d) {
        super.onActivityResult(requestCode, resultCode, d);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                String[] array = d.getExtras().getStringArray("Array");
                heading.setText(array[0]);
                content.setText(Html.fromHtml(array[1]));
                data = array;
            }
        }
    }
}
