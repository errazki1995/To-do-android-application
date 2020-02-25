package upem.tasksAnd.start.Services;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Json {
    Context context;

    public Json(Context ctx){
        context=ctx;
    }

    public String filename="tasks.json";

    public  String getTheJson(){
        try {
            InputStream stream = context.getAssets().open(filename);
            int size =stream.available();
            byte[] tampon = new byte[size];
            stream.read(tampon);
            stream.close();
            return new String(tampon,"UTF-8");
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public String PutTotheJson(String data){
        File f = context.getFilesDir();
        OutputStreamWriter streamWriter;
        try {
            File file =new File(f.getPath()+"/tasks.json");
            streamWriter= new OutputStreamWriter(context.openFileOutput(file.getPath(),Context.MODE_PRIVATE));
            streamWriter.write(data);
            return "Json OK!";
        }catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }

}
