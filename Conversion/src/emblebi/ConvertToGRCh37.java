package emblebi;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;


public class ConvertToGRCh37 {

 public static void main(String[] args) throws Exception {

  System.out.println("Enter chromosome Region");

  Scanner scanner = new Scanner(System.in);

  String chr = scanner.next();

  System.out.println("Enter chromosome Start");

  Scanner scanner1 = new Scanner(System.in);

  String start = scanner1.next();


  System.out.println("Enter chromosome Stop");

  Scanner scanner2 = new Scanner(System.in);

  String stop = scanner2.next();
  String server = "http://rest.ensembl.org";
  String ext = "/map/human/GRCh38/" + chr + ":" + start + ".." + stop + ":1/GRCh37?";
  URL url = new URL(server + ext);

  URLConnection connection = url.openConnection();
  HttpURLConnection httpConnection = (HttpURLConnection) connection;

  httpConnection.setRequestProperty("Content-Type", "application/json");


  InputStream response = connection.getInputStream();
  int responseCode = httpConnection.getResponseCode();

  if (responseCode != 200) {
   throw new RuntimeException("Response code was not 200. Detected response was " + responseCode);
  }

  String output;
  Reader reader = null;
  try {
   reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
   StringBuilder builder = new StringBuilder();
   char[] buffer = new char[8192];
   int read;
   while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
    builder.append(buffer, 0, read);
   }
   output = builder.toString();
  } finally {
   if (reader != null) try {
    reader.close();
   } catch (IOException logOrIgnore) {
    logOrIgnore.printStackTrace();
   }
  }


  System.out.println(output);
 }
}