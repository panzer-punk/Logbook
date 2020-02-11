package madsoft.com.form.FileSystem;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.STORAGE_SERVICE;

/**
 * Created by Даниил on 30.06.2018.
 */

public class CacheSystem {

    private Activity activity;
    public static String file_extension = ".thm";
    public static String shorts_prefix = "s_";

    public CacheSystem(Activity activity) {

        this.activity = activity;

    }

    private String prepare(String s)
    { return s + file_extension;}

    public boolean checkFile(String filename){

        filename = prepare(filename);

        Log.d("Check", filename);


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
        filename = prepare(filename);

        Log.d("WriteArrayList", filename);

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
        filename = prepare(filename);

        Log.d("Write", filename);

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
        filename = prepare(filename);

        Log.d("Load", filename);

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

        filename = prepare(filename);

        Log.d("LoadArrayList", filename);

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

    public ArrayList<String> loadListCachedFiles(){

        File directory = activity.getApplicationContext().getFilesDir();
        ArrayList<String> filenames = new ArrayList<>();

       File[] files =  directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {

                if(file.getName().contains(file_extension) && !file.getName().contains(shorts_prefix))
                return true;
                else
                return false;

            }
        });

        for( File f : files){

            filenames.add(f.getName().replace(file_extension,""));

            Log.d("LoadList", f.getName());

        }

        return filenames;

    }

}
