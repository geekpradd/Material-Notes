package pradd.com.notes;

import com.orm.SugarRecord;

import java.util.ArrayList;


/**
 * Created by Pradipta on 18-04-2015.
 */
public class Notes extends SugarRecord<Notes>{
    String title; //For Code Maintaince, keep this
    String content;

    public Notes(){
    }

    public Notes(String title, String content){
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString(){
        return this.title;
    }

}
