package pradd.com.notes;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Pradipta on 23-05-2015.
 */
public class CustomListViewAdapter extends ArrayAdapter<Notes> {

    private List<Notes> notes;

    public CustomListViewAdapter(Context context, List<Notes> ns) {
        super(context,R.layout.row_layout, ns);
        notes = ns;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //LayoutInflater will take the layout and create a view out of it
        // which will be used in our ListView
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View theView = inflater.inflate(R.layout.row_layout, parent, false); //False means
        //not to inflate into the parent

        Spanned note = Html.fromHtml(getItem(position).content);
        TextView tx = (TextView) theView.findViewById(R.id.textView1);
        tx.setText(note);
        ImageView iv = (ImageView) theView.findViewById(R.id.clickable);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notes n = notes.get(position);
                n.delete();
                notes.remove(position);
                notifyDataSetChanged();
            }
        });
        return theView;
    }

}
