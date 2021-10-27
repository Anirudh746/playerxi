package com.team.fetchdata;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class FetchData {
  
  private static final String       API_TOKEN = "api_token=z2HrVDP07JS26WPjou6DXaoAy0NjRgpXvIoyhTbSsTP80GiSspLx4VleEewe";
  private static final List<String> BATS      = Arrays.asList("innings", "balls_faced");
  private static final List<String> BOWLS     = Arrays.asList("innings", "wickets");
  private static final List<String> WKS       = Arrays.asList("innings", "balls_faced");
  private static final List<String> ALLRS     = Arrays.asList("innings", "balls_faced", "wickets");
  private static final List<String> FORMATS   = Arrays.asList("ODI", "T20I");
  
  public static List<Integer> getAllCountriesData() throws IOException
  {
    HttpURLConnection connection = (HttpURLConnection) new URL(
        "https://cricket.sportmonks.com/api/v2.0/countries?fields[countries]=id&" + API_TOKEN).openConnection();
    
    connection.setRequestMethod("GET");
    
    int responseCode = connection.getResponseCode();
    if (responseCode == 200) {
      String response = "";
      Scanner scanner = new Scanner(connection.getInputStream());
      while (scanner.hasNextLine()) {
        response += scanner.nextLine();
        response += "\n";
      }
      scanner.close();
      
      JSONObject jsonObject = new JSONObject(response);
      List<Integer> countryIds = new ArrayList<Integer>();
      JSONArray jsonArray = jsonObject.getJSONArray("data");
      for (int i = 0, size = jsonArray.length(); i < size; i++) {
        JSONObject objectInArray = jsonArray.getJSONObject(i);
        countryIds.add((Integer) objectInArray.get("id"));
      }
      return countryIds;
    }
    
    // an error happened
    return null;
  }
  
  public static void getPlayersData(Map<Integer, Map<Integer, Integer>> batsManMap,
      Map<Integer, Map<Integer, Integer>> wkMap,
      Map<Integer, Map<Integer, Integer>> allrMap,
      Map<Integer, Map<Integer, Integer>> bowlerMap, Map<Integer, String> profile) throws IOException
  {
    HttpURLConnection connection = (HttpURLConnection) new URL(
        "https://cricket.sportmonks.com/api/v2.0/players?fields[players]=id,fullname,position&"
            + API_TOKEN).openConnection();
    
    fetchInfo(batsManMap, wkMap, allrMap, bowlerMap, profile, connection);
  }
  
  public static void getPlayersDataByCountry(Map<Integer, Map<Integer, Integer>> batsManMap,
      Map<Integer, Map<Integer, Integer>> wkMap,
      Map<Integer, Map<Integer, Integer>> allrMap,
      Map<Integer, Map<Integer, Integer>> bowlerMap, Map<Integer, String> profile, Integer iCountry) throws IOException
  {
    HttpURLConnection connection = (HttpURLConnection) new URL(
        "https://cricket.sportmonks.com/api/v2.0/players?&filter[country_id]=" + iCountry + ",fields[players]=id,fullname,position&"
            + API_TOKEN).openConnection();
    
    fetchInfo(batsManMap, wkMap, allrMap, bowlerMap, profile, connection);
  }

  private static void fetchInfo(Map<Integer, Map<Integer, Integer>> batsManMap,
      Map<Integer, Map<Integer, Integer>> wkMap,
      Map<Integer, Map<Integer, Integer>> allrMap,
      Map<Integer, Map<Integer, Integer>> bowlerMap, Map<Integer, String> profile,
      HttpURLConnection connection) throws ProtocolException, IOException
  {
    connection.setRequestMethod("GET");
    
    int responseCode = connection.getResponseCode();
    if (responseCode == 200) {
      String response = "";
      Scanner scanner = new Scanner(connection.getInputStream());
      while (scanner.hasNextLine()) {
        response += scanner.nextLine();
        response += "\n";
      }
      scanner.close();
      
      JSONObject jsonObject = new JSONObject(response);
      JSONArray jsonArray = jsonObject.getJSONArray("data");
      for (int loopCounter = 0, size = jsonArray.length(); loopCounter < size; loopCounter++) {
        System.out.print(".");
        JSONObject objectInArray = jsonArray.getJSONObject(loopCounter);
        Integer playerId = (Integer) objectInArray.get("id");
        profile.put(playerId, (String) objectInArray.get("fullname"));
        JSONObject position = objectInArray.getJSONObject("position");
        Integer iPosition = (Integer) position.get("id");
        String role = (String) position.get("name");
        switch (role) {
          case "Batsman":
            getPlayerData(playerId, batsManMap, iPosition, BATS);
            break;
          case "Bowler":
            getPlayerData(playerId, bowlerMap, iPosition, BOWLS);
            break;
          case "Wicketkeeper":
            getPlayerData(playerId, wkMap, iPosition, WKS);
            break;
          case "Allrounder":
            getPlayerData(playerId, allrMap, iPosition, ALLRS);
            break;
          default:
            break;
        }
      }
    }
    else {
      System.out.println("Too many attempts.");
    }
  }
  
  public static void getPlayerData(Integer playerId,
      Map<Integer, Map<Integer, Integer>> rolesMap, Integer iPosition, List<String> fields)
      throws IOException
  {
    Integer finalScore = 0;
    HttpURLConnection connection = (HttpURLConnection) new URL(
        "https://cricket.sportmonks.com/api/v2.0/players/" + playerId
            + "?fields[players]=id,career&include=career&" + API_TOKEN).openConnection();
    
    connection.setRequestMethod("GET");
    
    int responseCode = connection.getResponseCode();
    if (responseCode == 200) {
      String response = "";
      Scanner scanner = new Scanner(connection.getInputStream());
      while (scanner.hasNextLine()) {
        response += scanner.nextLine();
        response += "\n";
      }
      scanner.close();
      
      JSONObject jsonObject = new JSONObject(response);
      JSONObject dataObject = jsonObject.getJSONObject("data");
      JSONArray jsonArray = dataObject.getJSONArray("career");
      for (int i = 0, size = jsonArray.length(); i < size; i++) {
        System.out.print(".");
        JSONObject objectInArray = jsonArray.getJSONObject(i);
        if (FORMATS.contains((String) objectInArray.get("type"))) {
          if (!objectInArray.isNull("batting")) {
            JSONObject batting = objectInArray.getJSONObject("batting");
            for (String key : BATS) {
              finalScore += batting.getInt(key);
            }
          }
          if (!objectInArray.isNull("bowling")) {
            JSONObject bowling = objectInArray.getJSONObject("bowling");
            for (String key : BOWLS) {
              finalScore += bowling.getInt(key);
            }
          }
        }
      }
    }
    else {
      System.out.println("Too many attempts. Use new API Token.");
    }
    
   if (rolesMap.get(iPosition) == null) {
     Map<Integer, Integer> scoreMap = new HashMap<Integer, Integer>();
     scoreMap.put(finalScore, playerId);
     rolesMap.put(iPosition, scoreMap);
   }
   else {
     Map<Integer, Integer> scoreMap = rolesMap.get(iPosition);
     scoreMap.put(finalScore, playerId);
   }
  }
}
