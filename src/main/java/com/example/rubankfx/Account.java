package com.example.rubankfx;

import java.text.DecimalFormat;

/**
 * Represents an abstract account with basic functionalities.
 * This class provides a foundation for different types of bank accounts.
 * Subclasses should provide implementations for monthly interest and monthly fee calculations.
 * @author Arun Felix, Digvijay Singh
 */
public abstract class Account implements Comparable<Account> {

    /** The profile associated with this account. */
    protected Profile holder;

    /** The current balance of this account. */
    protected double balance;

    /**
     * Constructs a new account with the specified profile and balance.
     *
     * @param holder The profile associated with this account.
     * @param balance The initial balance of the account.
     */
    public Account(Profile holder, double balance) {
        this.holder = holder;
        this.balance = balance;
    }
    public abstract String toString();

    /**
     * Calculates the monthly interest for this account.
     *
     * @return The monthly interest amount.
     */
    public abstract double monthlyInterest();
    public abstract void applyWithdraw();
    public abstract String GetType();
    /**
     * Calculates the monthly fee for this account.
     *
     * @return The monthly fee amount.
     */
    public abstract double monthlyFee();


    public Profile getProfile(){
        return holder;
    }


    /**
     * Deposits the specified amount to this account.
     *
     * @param amount The amount to deposit.
     */
    public void deposit(double amount){
        balance += amount;
    }
    public void withdraw(double amount) {
        if(this.balance - amount > 0){
            balance -= amount;
        }
        else{
            System.out.printf("%s(%s) Withdraw - insufficient fund.\n",getProfile().toString(),GetType());
        }

    }

    /**
     * Retrieves the current balance of this account.
     *
     * @return The current balance.
     */
    public double getbalance(){
        DecimalFormat df = new DecimalFormat("#.00");
        String formattedResult = df.format(balance);
        return Double.parseDouble(formattedResult);
    }

    /**
     * Compares this account to another account.
     * This method should be implemented to define the ordering among accounts, e.g., based on holder's name or balance.
     *
     * @param otherAccount The other account to compare with.
     * @return A negative integer, zero, or a positive integer as this account is less than, equal to, or greater than the specified account.
     */
    @Override
    public int compareTo(Account otherAccount) {
        int compare = this.GetType().compareTo(otherAccount.GetType());
        if(compare == 0){
            return getProfile().compareTo(otherAccount.getProfile());
        }
        return compare;

    }


    /**
     * Checks whether this account is equal to another object.
     *
     * @param obj The object to check equality with.
     * @return true if the objects are the same or represent the same account, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Account){
            Account temp = (Account)obj;
            if(temp.getProfile().compareTo(this.getProfile()) == 0){
                return typecheck(this, temp);
            }
            return this.compareTo(temp) == 0;
        }
        return false;
    }
    private Boolean typecheck(Account A, Account B){
        // System.out.println("A:" + A.GetType() + ", B:" + B.GetType());
        boolean condition1 = A.GetType().equals("C") && B.GetType().equals("CC");
        boolean condition2 = A.GetType().equals("CC") && B.GetType().equals("C");
        boolean condition3 = A.GetType().equals(B.GetType());
        return condition1 || condition2 || condition3;
    }
}