package pradd.com.notes;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class MainActivity extends ActionBarActivity {

    private ListView list;
    private ArrayAdapter<String> adapter;
    public static String[] items ={"History", "Settings", "About"};
    private ActionBarDrawerToggle toggle;
    private DrawerLayout layout;
    private String title;
    private List<Notes> notes;
    public ListView ls;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ls = (ListView) findViewById(R.id.main_list_view);
        list = (ListView) findViewById(R.id.navList);
        layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        title = getTitle().toString();
        setListItems();
        setDrawer();
        setMainList();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


    }
    public void setMainList(){
        notes = Notes.listAll(Notes.class);
        final ArrayAdapter<Notes> adapter = new CustomListViewAdapter(this, notes);
        ls.setAdapter(adapter);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notes note = notes.get(position);
                Intent callViewNote = new Intent(MainActivity.this, ViewNote.class);
                Bundle b = new Bundle();
                b.putStringArray("Array", new String[]{note.title, note.content,Long.toString(note.getId())});
                callViewNote.putExtras(b);
                startActivity(callViewNote);
            }
        });
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        ls,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    adapter.getItem(position).delete();
                                    adapter.remove(adapter.getItem(position));

                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
        ls.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        ls.setOnScrollListener(touchListener.makeScrollListener());
    }
    //Override the activity post create to sync the menu toggle
    //This syncs the toggle onclick (for animation)
    @Override
    protected void onPostCreate(Bundle savedStateInstance){
        super.onPostCreate(savedStateInstance);
        toggle.syncState();
    }
    //Modify the toggle on config changes (like orientation changes)
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    public void setDrawer(){
        toggle = new ActionBarDrawerToggle(this, layout,R.string.drawer_open, R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu();//Reset the List (so that it can be changed)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(title);
                invalidateOptionsMenu();
            }
        };
        toggle.setDrawerIndicatorEnabled(true); //Set the toggle to use an icon
        layout.setDrawerListener(toggle);

    }
    public void setListItems(){
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this , "You clicked " + MainActivity.items[position],Toast.LENGTH_SHORT ).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id==R.id.new_note){
            Intent intent = new Intent(this, NewNote.class);
            final int result = 1;
            startActivityForResult(intent, result);
            return true;
        }
        if (id == R.id.delete_notes){
            Notes.deleteAll(Notes.class);
            setMainList();
        }
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                String[] array = b.getStringArray("Array");
                String NoteContent = array[1];
//                List<String> tags = new ArrayList<String>();
//                for (String x: NoteContent.split("\\s+")){
//                    if (x.startsWith("#")){
//                        tags.add(x);
//                    }
//                }

                Notes n = new Notes(array[0], array[1]);
                n.save();
                setMainList();
            }
        }
    }
}
