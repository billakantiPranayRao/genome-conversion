package emblebi;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/*This class is meant for conversion of GRCh38 to GRCh37 from a .bed file*/
public class ConversionToGRCh37 {


 public static void main(String[] args) throws IOException {

	 ConversionToGRCh37 cn = new ConversionToGRCh37();


     cn.convertGRCh38ToGRCh37();


 }
/*Reading all the GRCh38 data from .bed file */
 
 private ArrayList < String > readData() throws FileNotFoundException {

  Scanner s = new Scanner(new FileReader("C:/Users/Z0045MZT/Desktop/EBI/GRCH38Test.bed"));
  ArrayList < String > list = new ArrayList < String > ();
  while (s.hasNext()) {
   list.add(s.next());
  }
  s.close();
  chromosomeRegion(list);
  chromosomeStart(list);
  chromosomeStop(list);
  return list;
 }

 //collecting only Chromosome Region data of GRCh38

 private LinkedList < String > chromosomeRegion(ArrayList < String > chrData) {

  LinkedList < String > chr = new LinkedList < > ();

  int i = 0;

  while (i < chrData.size()) {
   chr.add(chrData.get(i));

   i = i + 3;

  }

  return chr;
 }

 /*collecting only Chromosome Start data of GRCh38*/
 
 private LinkedList < String > chromosomeStart(ArrayList < String > chrStartData) {

  LinkedList < String > chr_start = new LinkedList < > ();

  int j = 1;

  while (j < chrStartData.size()) {

   chr_start.add(chrStartData.get(j));
   j = j + 3;

  }

  return chr_start;
 }
 
 /*collecting only Chromosome Stop data of GRCh38*/

 private LinkedList < String > chromosomeStop(ArrayList < String > chrStopData) {


  LinkedList < String > chr_stop = new LinkedList < > ();


  int k = 2;

  while (k < chrStopData.size()) {

   chr_stop.add(chrStopData.get(k));

   k = k + 3;
  }

  return chr_stop;

 }

 /*Converting GRCh38 data to GRCh37 data using ensembl rest API*/
 
 private String convertGRCh38ToGRCh37() throws IOException {
  String GRCh37Data = "";

  ConversionToGRCh37 cn = new ConversionToGRCh37();

  System.out.println("FileContents :" + cn.readData());

  Iterator ch = cn.chromosomeRegion(cn.readData()).iterator();
  Iterator c_start = cn.chromosomeStart(cn.readData()).iterator();
  Iterator c_stop = cn.chromosomeStop(cn.readData()).iterator();

  String server = "http://rest.ensembl.org";

  while (ch.hasNext() && c_start.hasNext() && c_stop.hasNext()) {
   String ext = "/map/human/GRCh38/" + ch.next() + ":" + c_start.next() + ".." + c_stop.next() + ":1/GRCh37?";
   URL url = new URL(server + ext);
   URLConnection connection = url.openConnection();
   HttpURLConnection httpConnection = (HttpURLConnection) connection;
   httpConnection.setRequestProperty("Content-Type", "application/json");


   InputStream response = connection.getInputStream();
   int responseCode = httpConnection.getResponseCode();
   if (responseCode != 200) {
    throw new RuntimeException("Response code was not 200. Detected response was " + responseCode);
   }

   Reader reader = null;
   try {
    reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
    StringBuilder builder = new StringBuilder();
    char[] buffer = new char[8192];
    int read;
    while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
     builder.append(buffer, 0, read);
    }
    GRCh37Data = builder.toString();
   } finally {
    if (reader != null) try {
     reader.close();
    } catch (IOException logOrIgnore) {
     logOrIgnore.printStackTrace();
    }
    System.out.println(GRCh37Data);

   }


  }

  return GRCh37Data;
 }

}