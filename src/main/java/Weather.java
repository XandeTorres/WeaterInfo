import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class Weather {
   private JSONObject jData;

   public String request()  {
      try {
         URL url = new URL("https://api.openweathermap.org/data/2.5/forecast?q=Saint%20Petersburg&units=metric&appid=93b4ceb459cf6f5195575187e4f8e0db");
         Scanner in = new Scanner((InputStream) url.getContent());
         StringBuilder response = new StringBuilder();
         while (in.hasNext()) {
            response.append(in.nextLine());
         }
         in.close();
         return response.toString();

      } catch (IOException e){
         System.out.println("Request failed. Try again");
         return null;
      }
   }
   public void getData(){
      try {
         jData = (JSONObject) new JSONParser().parse(request());
      } catch (ParseException e) {
         System.out.println("Parse failed. Try again");
      }
   }

   public String getPressure()  {
      JSONObject jsonObject = jData;

      if (jsonObject!= null) {
         JSONArray weatherList = (JSONArray) jsonObject.get("list");
         double maxPressure = 0.0D;

         for (Object it : weatherList) {
            JSONObject currinfo = (JSONObject) it;
            currinfo = (JSONObject) currinfo.get("main");
            long pressure = (long) currinfo.get("pressure");
            if (pressure > maxPressure)
               maxPressure = (double) pressure;
         }
         maxPressure = maxPressure * 0.750062;
         return "Max pressure is "+ maxPressure + " mmHg";
      }
      return "Something goes wrong. Try again";
   }

   public String getMinTempDiffDay() {
      Map <String, Double> dayTemp = new HashMap<>();

      String currDate;
      String currDateTime;
      JSONObject jsonObject = jData;
      if (jsonObject!= null) {
         JSONArray jsonArray = (JSONArray) jData.get("list");
         JSONObject first = (JSONObject) jsonArray.get(0);
         String initialDate = (String) first.get("dt_txt");
         initialDate = initialDate.substring(0, 10);

         for (Object it : jsonArray) {
            JSONObject currInfo = (JSONObject) it;
            JSONObject currMain = (JSONObject) currInfo.get("main");
            currDate = (String) currInfo.get("dt_txt");
            currDate = currDate.substring(0, 10);
            currDateTime = (String) currInfo.get("dt_txt");

            if (!initialDate.equals(currDate)) {
               if (currDateTime.contains("00:00:00")) {
                  dayTemp.put(currDate, Double.parseDouble(currMain.get("temp").toString()));
               }
               if (currDateTime.contains("09:00:00")) {
                  dayTemp.put(currDate, Math.abs(dayTemp.get(currDate) - Double.parseDouble(currMain.get("temp").toString())));
               }
            }
         }
         //System.out.println(dayTemp);
         Map.Entry<String, Double> min = Collections.min(dayTemp.entrySet(), Map.Entry.comparingByValue());
         //System.out.println(min);
         return "Day with min night and morning temperature difference in " + Math.round(min.getValue()) + "C is " + min.getKey();
      }
      return "Something goes wrong. Try again";
   }

}
