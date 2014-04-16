package com.jaymcd.leap_calc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Server {
	JSONArray jArray = null;
	String result = null;
	StringBuilder sb = null;
	InputStream is = null;
	Context c;
	static public String URL, email, pass, bal;
	static ArrayList<NameValuePair> data;
	protected String getUserURL = "http://www.jaymcd.com/leapUsr.php";
	protected String addBalURL = "http://www.jaymcd.com/upDBal.php";
	protected String addUserURL = "http://www.jaymcd.com/upDUser.php";
	protected String getBalURL = "http://www.jaymcd.com/leapBal.php";

	public Server(Context x) {
		c = x;
	}
	


	public void addUser(ArrayList<NameValuePair> d) {
		data = null;
		data = d;
		URL = addUserURL;
		writeServer ws = new writeServer();
		ws.execute("");

	}// end dbConnect

	public void addBal(ArrayList<NameValuePair> d) {
		data = null;
		data=d;
		URL = addBalURL;
		writeServer ws = new writeServer();
		ws.execute("");

	}// end dbConnect
	
	public Boolean getUser(String a, String b) {
		email = a;
		URL = getUserURL;
		pass = "";
		getUser u = new getUser();
		try {
			pass = u.execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		if (pass.equals(b)) {
			return true;
		}

		return false;
	}
	
	public Boolean getUserN(String a){
		email = a;
		URL = getUserURL;
		String b=null;
		getUserN u = new getUserN();
		
		try {
			b = u.execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		if (email.equals(b)){ //if email already exists
		return false;}
		
		return true;
	}

	public int getBal(String a) {
		
		int rtnVal = 0;
		email = a;
		URL = getBalURL;
		
		getBal b = new getBal();
		try {
			bal = b.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rtnVal = Integer.parseInt(bal);
		return rtnVal;

	}

	private class writeServer extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(URL);
				httppost.setEntity(new UrlEncodedFormEntity(data));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();

			} catch (Exception e) {
				Toast.makeText(c, "Unable to connect to server", Toast.LENGTH_SHORT).show();
				Log.e("log_tag", "Error in http connection " + e.toString());
				return null;
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(String... text) {
		}	

	}
	
	
	private class getUser extends AsyncTask<String, String, String> {

		ArrayList<HashMap<String, String>> resultL = new ArrayList<HashMap<String, String>>();
		ArrayList<NameValuePair> List = new ArrayList<NameValuePair>();
		@Override
		protected String doInBackground(String... params) {
			

			try {
				HttpClient httpclient = new DefaultHttpClient();

				HttpPost httppost = new HttpPost(URL);
				httppost.setEntity(new UrlEncodedFormEntity(List));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			} catch (Exception e) {
				Toast.makeText(c, "Unable to connect to server", Toast.LENGTH_SHORT).show();
				Log.e("log_tag", "Error in http connection " + e.toString());
				return null;
			}
			// convert response to string
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						is, "iso-8859-1"), 8);
				sb = new StringBuilder();
				sb.append(reader.readLine() + "\n");

				String line = "0";
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}

				is.close();

				result = sb.toString();

			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());

			}
			
			
			
			
			try {
				jArray = new JSONArray(result);
				JSONObject json_data = null;
				for (int i = 0; i < jArray.length(); i++) {
					json_data = jArray.getJSONObject(i);

					HashMap<String, String> map = new HashMap<String, String>();
					map.put("user", json_data.getString("user"));
					map.put("password", json_data.getString("password"));
					resultL.add(map);
				}

			} catch (JSONException e1) {
				Toast.makeText(c, "No Sync Found", Toast.LENGTH_LONG).show();

			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			pass = "";
			for (int i = 0; i < resultL.size(); i++) {
				String x;
				x = resultL.get(i).get("user");
				if (x.equals(email)) {
					pass = resultL.get(i).get("password");

				}
				
			}
			
			
			return pass;
		}
		@Override
		protected void onPostExecute(String result) {
			
		}

		@Override
		protected void onPreExecute() {
			
		}

		@Override
		protected void onProgressUpdate(String... text) {
		}	

	}
	
	private class getUserN extends AsyncTask<String, String, String> {

		ArrayList<HashMap<String, String>> resultL = new ArrayList<HashMap<String, String>>();
		ArrayList<NameValuePair> List = new ArrayList<NameValuePair>();
		@Override
		protected String doInBackground(String... params) {
			

			try {
				HttpClient httpclient = new DefaultHttpClient();

				HttpPost httppost = new HttpPost(URL);
				httppost.setEntity(new UrlEncodedFormEntity(List));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			} catch (Exception e) {
				Toast.makeText(c, "Unable to connect to server", Toast.LENGTH_SHORT).show();
				Log.e("log_tag", "Error in http connection " + e.toString());
				return null;
			}
			// convert response to string
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						is, "iso-8859-1"), 8);
				sb = new StringBuilder();
				sb.append(reader.readLine() + "\n");

				String line = "0";
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}

				is.close();

				result = sb.toString();

			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());

			}
			
			
			
			
			try {
				jArray = new JSONArray(result);
				JSONObject json_data = null;
				for (int i = 0; i < jArray.length(); i++) {
					json_data = jArray.getJSONObject(i);

					HashMap<String, String> map = new HashMap<String, String>();
					map.put("user", json_data.getString("user"));
					map.put("password", json_data.getString("password"));
					resultL.add(map);
				}

			} catch (JSONException e1) {
				Toast.makeText(c, "No Sync Found", Toast.LENGTH_LONG).show();

			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			String userN;
			userN = "";
			for (int i = 0; i < resultL.size(); i++) {
				String x;
				x = resultL.get(i).get("user");
				if (x.equals(email)) {
					userN = resultL.get(i).get("user");

				}
				
			}
			
			
			return userN;
		}
		@Override
		protected void onPostExecute(String result) {
			
		}

		@Override
		protected void onPreExecute() {
			
		}

		@Override
		protected void onProgressUpdate(String... text) {
		}	

	}
	
	
	
	private class getBal extends AsyncTask<String, String, String> {
		ArrayList<NameValuePair> List = new ArrayList<NameValuePair>();
		ArrayList<HashMap<String, String>> resultL = new ArrayList<HashMap<String, String>>();

		@Override
		protected String doInBackground(String... params) {
			
			try {
				HttpClient httpclient = new DefaultHttpClient();

				HttpPost httppost = new HttpPost(URL);
				httppost.setEntity(new UrlEncodedFormEntity(List));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			} catch (Exception e) {
				Toast.makeText(c, "Unable to connect to server", Toast.LENGTH_SHORT).show();
				Log.e("log_tag", "Error in http connection " + e.toString());
				return null;

			}
			// convert response to string
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						is, "iso-8859-1"), 8);
				sb = new StringBuilder();
				sb.append(reader.readLine() + "\n");

				String line = "0";
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}

				is.close();

				result = sb.toString();

			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());

			}
			
			
			try {
				jArray = new JSONArray(result);
				JSONObject json_data = null;
				for (int i = 0; i < jArray.length(); i++) {
					json_data = jArray.getJSONObject(i);

					HashMap<String, String> map = new HashMap<String, String>();
					map.put("user", json_data.getString("user"));
					map.put("balance", json_data.getString("balance"));
					resultL.add(map);
				}

			} catch (JSONException e1) {
				Toast.makeText(c, "No Sync Found", Toast.LENGTH_LONG).show();

			} catch (ParseException e1) {
				e1.printStackTrace();
			}


			for (int i = 0; i < resultL.size(); i++) {
				String x;
				x = resultL.get(i).get("user");
				if (x.equals(email)) {
					bal = resultL.get(i).get("balance");

				}
			}
			if (bal == null){ bal ="0";}
			return bal;
		}
		@Override
		protected void onPostExecute(String result) {
			
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(String... text) {
		}	

	}
	
	
	
		


}
