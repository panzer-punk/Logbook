package madsoft.com.form;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;



import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Даниил on 30.06.2018.
 */

public class CacheSystem {

    private Activity activity;

    public CacheSystem(Activity activity) {

        this.activity = activity;

    }

    public void save(byte[] file){

    }

    public void write(String str, String filename){

        BufferedWriter bw;

        try{
            bw = new BufferedWriter(new OutputStreamWriter(
                    activity.openFileOutput(filename, MODE_PRIVATE)));

            bw.write(str);

            bw.close();

        }catch (Exception ex){ex.printStackTrace();}


    }

    public void delete(String path){

        

    }

    public String load(String filename){

        String str = "";
        String str2 = " ";

        try {


            BufferedReader br = new BufferedReader(new InputStreamReader(
                    activity.openFileInput(filename)));



            while ((str = br.readLine()) != null) {
                str2 += str;
            }
            br.close();

        }catch (Exception e){e.printStackTrace();}

        return str2;

    }

}
