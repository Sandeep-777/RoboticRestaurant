package com.example.sandeep.parsingtest;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by sandeep on 7/1/16.
 */
public class SendData {

    String txt_file_name = "data.txt";
    File sensor_file;
    FileOutputStream f_out_data;


    public void commitToFile(String data) throws IOException {

        try {
            sensor_file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), txt_file_name);
            sensor_file.createNewFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

        final String entryString = new String(data + "\n");
        f_out_data = new FileOutputStream(sensor_file, true);
        OutputStreamWriter osw = new OutputStreamWriter(f_out_data);
        osw.write(entryString);
        osw.flush();
        osw.close();
    }

    public void deletefile(){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), txt_file_name);
        boolean deleted = file.delete();
    }
}
