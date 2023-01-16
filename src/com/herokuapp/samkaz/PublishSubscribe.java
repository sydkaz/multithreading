package com.herokuapp.samkaz;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Printer
{
    // Initial 100 paper are set in Printer
    transient int totalNoOfPaper = 20;
    // Synchronized the method for inter-thread communication
    synchronized void printingPages(int pages) throws InterruptedException {
        System.out.println("Printing the Pages");

        while (true) {
            System.out.println("Consumer <<<<<< ");
            if(totalNoOfPaper>= pages ){
                System.out.println(" Total number of pages printed "+pages+" total number of pages in printer "+totalNoOfPaper);
                this.totalNoOfPaper -= pages;
            } else {
                System.out.println("Not enough pages required "+ pages + " but have "+totalNoOfPaper);
                wait();
            }
        }
    }
    synchronized void addPages(int noOfPages)
    {
        System.out.println("Producer >>>>> ");
        // Adding more Papers in Printer;
        System.out.println("Adding pages to printer noOfPages "+noOfPages);
        this.totalNoOfPaper += noOfPages;
        System.out.println("Adding pages to printer total "+totalNoOfPaper);
        // After adding the paper in printer. Notify the Paused thread;
        notify();
    }
}
public class PublishSubscribe {
    public static void main(String args[])
    {
        Printer printer = new Printer();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(
                ()->{
                    // User want to print 120 pages
                    try {
                        printer.printingPages(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

        );

        // Second thread for Add pages in printer
        executorService.execute(()->
                {
                    // Add 100 more pages in Printer
                    for(int i = 0; i <= 500; i++){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        printer.addPages(1);
                    }

                }
        );

        executorService.shutdown();

    }
}