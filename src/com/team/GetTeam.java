package com.team;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;

import com.team.fetchdata.FetchData;
import com.team.validate.Validations;

public class GetTeam {
  
  public static void main(String[] args) throws IOException
  {
    readInputAndSelectTeam();
  }

  private static void readInputAndSelectTeam() throws IOException
  {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Welcome to the Best Cricket team XI selector APP.");
    System.out.println("Restrictions:\n" +
      "1. Team size should not exceed more than 11\n" +
      "2. You should have atleast one wicket keeper in the team\n" +
      "3. You should have atleast 2 bowlers in the team\n" +
      "4. You should have atleast 1 allrounder in the team");
    Integer iBats = Validations.getAndValidateBatsInput(scanner);
    Integer iBowls = Validations.getAndValidateBowlsInput(scanner);
    Integer iAllrs = Validations.getAndValidateAllRsInput(scanner);
    int iTotal = iBats + iBowls + iAllrs;
    if (iTotal != 11) {
      System.out.println("Team must have 11 players.");
      scanner.close();
      readInputAndSelectTeam();
    }
    Integer iCountry = Validations.getAndValidateCountryInput(scanner);
    scanner.close();
    selectTeam(iBats, iBowls, iAllrs, iCountry);
  }

  private static void selectTeam(Integer iBats, Integer iBowls, Integer iAllrs, Integer iCountry) throws IOException
  {
    Map<Integer, Map<Integer, Integer>> batsManMap = new HashMap<Integer, Map<Integer,Integer>>();
    Map<Integer, Map<Integer, Integer>> bowlerMap = new HashMap<Integer, Map<Integer,Integer>>();
    Map<Integer, Map<Integer, Integer>> wkMap = new HashMap<Integer, Map<Integer,Integer>>();
    Map<Integer, Map<Integer, Integer>> allrMap = new HashMap<Integer, Map<Integer,Integer>>();
    Map<Integer, String> profile = new HashMap<Integer, String>();
    System.out.print("Finding a best team. This will take a while.");
    if (iCountry == 0) {
      FetchData.getPlayersData(batsManMap, wkMap, allrMap, bowlerMap, profile);
    }
    else {
      FetchData.getPlayersDataByCountry(batsManMap, wkMap, allrMap, bowlerMap, profile, iCountry);
    }
    System.out.println("\n!!! Final Team XI !!!");
    displayTeam(iBats-1, batsManMap, profile, "BAT  ");
    displayTeam(1, wkMap, profile, "WK   ");
    displayTeam(iAllrs, allrMap, profile, "ALLR ");
    displayTeam(iBowls, bowlerMap, profile, "BOWL ");
  }

  private static void displayTeam(Integer count,
      Map<Integer, Map<Integer, Integer>> rolesMap, Map<Integer, String> profile, String role)
  {
    for (Integer key : (new TreeSet<Integer>(rolesMap.keySet())).descendingSet()) {
      if (count == 0) {
        break;
      }
      Map<Integer, Integer> scoreMap = rolesMap.get(key);
      if (scoreMap == null) {
        continue;
      }
      for (Integer score : (new TreeSet<Integer>(scoreMap.keySet())).descendingSet()) {
        System.out.println(role + profile.get(scoreMap.get(score)));
        break;
      }
      count --;
    }
  }
}
