package com.example.testjdbc;
import java.util.Scanner;
public class Main { public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserService us = new UserService();

        int userId = -1;

        while (true) {

            if (userId == -1) {
                System.out.println("\n1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");

                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 1) us.registerUser();
                else if (choice == 2) userId = us.loginUser();
                else break;
            } else {
                System.out.println("\n1. Deposit");
                System.out.println("2. Withdraw");
                System.out.println("3. Balance");
                System.out.println("4. History");
                System.out.println("5. Logout");

                int choice = sc.nextInt();

                if (choice == 1) us.deposit(userId);
                else if (choice == 2) us.withdraw(userId);
                else if (choice == 3) us.checkBalance(userId);
                else if (choice == 4) us.transactionHistory(userId);
                else if (choice == 5) userId = -1;
            }
        }
    }
}