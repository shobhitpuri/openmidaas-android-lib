package org.openmidaas.library.test;


import java.io.InputStream;
import org.json.JSONObject;
import android.content.Context;

public class Utils {
	
	public static JSONObject readFileAsJSON(Context context, String filename) {
		try {
			InputStream is = context.getAssets().open(filename);
			int size = is.available();
			byte[] buffer = new byte[size];  
			is.read(buffer);
			is.close();
			String bufferString = new String(buffer);
			return (new JSONObject(bufferString));
	}
	catch(Exception e) {
		return null;
	}
	}
}
