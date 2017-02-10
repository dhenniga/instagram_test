package com.tag.instagramtest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	public JSONParser() {}

	public JSONObject getJSONFromUrlByGet(String urlString) {

        try {

			URL url = new URL(urlString);
			URLConnection urlConnection = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line;

            while ((line = in.readLine()) != null) {

				builder.append(line).append("\n");

			}

			json = builder.toString();
			in.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

		try {

			jObj = new JSONObject(json);

		} catch (JSONException e) {

		}

		return jObj;
	}
}
