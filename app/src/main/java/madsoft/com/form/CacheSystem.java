package madsoft.com.form;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Даниил on 30.06.2018.
 */

public class CacheSystem {

    private Activity activity;
    public static String file_extension = ".thm";

    public CacheSystem(Activity activity) {

        this.activity = activity;

    }

    private void prepare(String s)
    { s += file_extension;}

    public boolean checkFile(String filename){

        prepare(filename);


        try {


            BufferedReader br = new BufferedReader(new InputStreamReader(
                    activity.openFileInput(filename)));

        }catch (IOException e){return false;}
        catch (Exception e){e.printStackTrace(); return false;}

        return true;
    }

    public void save(byte[] file){

    }

    public void write(ArrayList<String> list, String filename){//спорный метод

        BufferedWriter bw;
        prepare(filename);

        try{
            bw = new BufferedWriter(new OutputStreamWriter(
                    activity.openFileOutput(filename, MODE_PRIVATE)));

            for(String s : list)
            bw.write(s);

            bw.close();

        }catch (Exception ex){ex.printStackTrace();}

    }

    public void write(String str, String filename){

        BufferedWriter bw;
        prepare(filename);

        try{
            bw = new BufferedWriter(new OutputStreamWriter(
                    activity.openFileOutput(filename, MODE_PRIVATE)));

            bw.write(str);

            bw.close();

        }catch (Exception ex){ex.printStackTrace();}


    }

    public void delete(String filename){

    try {

        activity.deleteFile(filename);

    }catch (Exception e){e.printStackTrace();}

    }

    public String load(String filename){

        String str = "";
        String str2 = " ";
        prepare(filename);

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

    public ArrayList<String> loadArrayList(String filename){

        String str = "";
        ArrayList<String> list = new ArrayList<>();

        prepare(filename);

        try {


            BufferedReader br = new BufferedReader(new InputStreamReader(
                    activity.openFileInput(filename)));



            while ((str = br.readLine()) != null) {
                list.add(str);
            }
            br.close();

        }catch (Exception e){e.printStackTrace();}

        return list;

    }

}
