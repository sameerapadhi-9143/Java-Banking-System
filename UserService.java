package com.example.testjdbc;

import java.sql.*;
import java.util.Scanner;

public class UserService {

    Scanner sc = new Scanner(System.in);

    // REGISTER
    public void registerUser() {
        try {
            Connection con = DBConnection.getConnection();

            System.out.print("Enter username: ");
            String username = sc.nextLine();

            System.out.print("Enter password: ");
            String password = sc.nextLine();

            // insert user
            String query = "INSERT INTO users(username, password) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();

            // get user id
            String getId = "SELECT id FROM users WHERE username=?";
            PreparedStatement ps2 = con.prepareStatement(getId);
            ps2.setString(1, username);
            ResultSet rs = ps2.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");

                // create account
                String accQuery = "INSERT INTO accounts(user_id, balance) VALUES (?, 0)";
                PreparedStatement ps3 = con.prepareStatement(accQuery);
                ps3.setInt(1, userId);
                ps3.executeUpdate();
            }

            System.out.println("Registered Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LOGIN
    public int loginUser() {
        try {
            Connection con = DBConnection.getConnection();

            System.out.print("Enter username: ");
            String username = sc.nextLine();

            System.out.print("Enter password: ");
            String password = sc.nextLine();

            String query = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Login Successful!");
                return rs.getInt("id");
            } else {
                System.out.println("Invalid Credentials!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // DEPOSIT
    public void deposit(int userId) {
        try {
            Connection con = DBConnection.getConnection();

            System.out.print("Enter amount: ");
            double amount = sc.nextDouble();

            String query = "UPDATE accounts SET balance = balance + ? WHERE user_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setDouble(1, amount);
            ps.setInt(2, userId);
            ps.executeUpdate();

            saveTransaction(userId, "DEPOSIT", amount);

            System.out.println("Amount Deposited!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // WITHDRAW
    public void withdraw(int userId) {
        try {
            Connection con = DBConnection.getConnection();

            System.out.print("Enter amount: ");
            double amount = sc.nextDouble();

            String check = "SELECT balance FROM accounts WHERE user_id=?";
            PreparedStatement ps1 = con.prepareStatement(check);
            ps1.setInt(1, userId);
            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");

                if (balance >= amount) {

                    String query = "UPDATE accounts SET balance = balance - ? WHERE user_id=?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setDouble(1, amount);
                    ps.setInt(2, userId);
                    ps.executeUpdate();

                    saveTransaction(userId, "WITHDRAW", amount);

                    System.out.println("Amount Withdrawn!");
                } else {
                    System.out.println("Insufficient Balance!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CHECK BALANCE
    public void checkBalance(int userId) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT balance FROM accounts WHERE user_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Balance: " + rs.getDouble("balance"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TRANSACTION HISTORY
    public void transactionHistory(int userId) {
        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT t.type, t.amount, t.date FROM transactions t " +
                    "JOIN accounts a ON t.account_id = a.account_id WHERE a.user_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                        rs.getString("type") + " " +
                                rs.getDouble("amount") + " " +
                                rs.getTimestamp("date")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // SAVE TRANSACTION
    public void saveTransaction(int userId, String type, double amount) {
        try {
            Connection con = DBConnection.getConnection();

            String accQuery = "SELECT account_id FROM accounts WHERE user_id=?";
            PreparedStatement psAcc = con.prepareStatement(accQuery);
            psAcc.setInt(1, userId);
            ResultSet rs = psAcc.executeQuery();

            if (rs.next()) {
                int accId = rs.getInt("account_id");

                String query = "INSERT INTO transactions(account_id, type, amount) VALUES (?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, accId);
                ps.setString(2, type);
                ps.setDouble(3, amount);

                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}