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

    long startSum = bank.calculateBankBalance();

    ExecutorService executorService = Executors
        .newFixedThreadPool(numberThreads);

    for (int i = 0; i < 100; i++) {
      executorService.submit(() ->
          bank.transfer(generatedAccNumber(), generatedAccNumber(), generatedMoneyAmount()));
    }
    executorService.shutdown();

//    while (true) {
//      try {
//        if (executorService.awaitTermination(1, TimeUnit.MINUTES)) {
//          break;
//        }
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//        return;
//      }
//    }

    while (!executorService.isTerminated()) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.println("BEFORE starting the transaction  " + startSum);
    System.out.println("AFTER completion  " + bank.calculateBankBalance());
  }

  public static String generatedAccNumber() {
    Random r = new Random();
    Long generatedAccNumberLong = r.nextInt(100) + 1000000000000000000L;
    String generatedAccNumber = generatedAccNumberLong.toString();
    return generatedAccNumber;
  }

  public static long generatedMoneyAmount() {
    Random r = new Random();
    long generatedMoneyAmount = r.nextInt(100) + 100000L;
    return generatedMoneyAmount;
  }
}
