package com.team.validate;

import java.util.Scanner;

import com.team.fetchdata.FetchData;


public class Validations {
  
  public static Integer getAndValidateBatsInput(Scanner scanner)
  {
    System.out.println("Enter no. of batsman:");
    String bats = scanner.nextLine();
    try {
      Integer iBats = Integer.valueOf(bats);
      if (iBats <= 0) {
        System.out.println("Please enter valid input.");
        getAndValidateBatsInput(scanner);
      }
      return iBats;
    }
    catch (Exception e) {
      System.out.println("Please enter valid input.");
      getAndValidateBatsInput(scanner);
    }
    return 0;
  }
  
  public static Integer getAndValidateBowlsInput(Scanner scanner)
  {
    System.out.println("Enter no. of bowlers:");
    String bowls = scanner.nextLine();
    try {
      Integer iBowls = Integer.valueOf(bowls);
      if (iBowls < 2) {
        System.out.println("Minimum 2 blowers required. Please enter valid input.");
        getAndValidateBowlsInput(scanner);
      }
      return iBowls;
    }
    catch (Exception e) {
      System.out.println("Please enter valid input.");
      getAndValidateBowlsInput(scanner);
    }
    return 0;
  }
  
  public static Integer getAndValidateAllRsInput(Scanner scanner)
  {
    System.out.println("Enter no. of all-rounders:");
    String allrs = scanner.nextLine();
    try {
      Integer iAllrs = Integer.valueOf(allrs);
      if (iAllrs < 1) {
        System.out.println("Minimum 1 all-rounder required. Please enter valid input.");
        getAndValidateAllRsInput(scanner);
      }
      return iAllrs;
    }
    catch (Exception e) {
      System.out.println("Please enter valid input.");
      getAndValidateAllRsInput(scanner);
    }
    return 0;
  }
  
  public static Integer getAndValidateCountryInput(Scanner scanner)
  {
    System.out.println("Enter country code (optional, to skip enter some random text):");
    String country = scanner.nextLine();
    try {
      Integer iCountry = Integer.valueOf(country);
      if (iCountry <= 0) {
        System.out.println("Please enter valid input.");
        getAndValidateCountryInput(scanner);
      }
      if (!FetchData.getAllCountriesData().contains(iCountry)) {
        System.out.println("Country code doesn't exist.");
        getAndValidateCountryInput(scanner);
      }
      return iCountry;
    }
    catch (Exception e) {
      System.out.println("No proper input provided for country code.");
    }
    return 0;
  }
  
}
