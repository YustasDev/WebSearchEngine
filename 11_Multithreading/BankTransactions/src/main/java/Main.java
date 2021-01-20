import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

  public class Main {

    public static void main(String[] args) {

      int numberThreads = 100;

      Bank bank = new Bank();
      bank.bankBuilder();

      ConcurrentMap<String, Account> currentBank = bank.getAccounts();
      int sizeCM = currentBank.size();
      System.out.println("sizeCM = " + sizeCM);
      int iz = 0;
      for (var pair : currentBank.entrySet()) {
        String key = pair.getKey();
        long value = pair.getValue().getMoney();
        iz++;
        System.out.println("ix= " + iz + "key = " + key + " value= " + value);
      }

      long startSum = bank.calculateBankBalance();

      ExecutorService executorService = Executors
          .newFixedThreadPool(numberThreads);

      for (int i = 0; i < 100; i++) {
        executorService.submit(() ->
            bank.transfer(generatedAccNumber(), generatedAccNumber(), generatedMoneyAmount()));
      }
      executorService.shutdown();

      try {
        executorService.awaitTermination(1, TimeUnit.MINUTES);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      System.out.println("BEFORE starting the transaction  " + startSum);
      System.out.println("AFTER completion" + bank.calculateBankBalance());


    }

    public static String generatedAccNumber() {
      Random r = new Random();
      Long generatedAccNumberLong = r.nextInt(100) + 1000000000000000000L;
      String generatedAccNumber = generatedAccNumberLong.toString();
      return generatedAccNumber;
    }

    public static long generatedMoneyAmount() {
      Random r = new Random();
      long generatedMoneyAmount = r.nextInt(100) + 1L;
      return generatedMoneyAmount;
    }
  }
