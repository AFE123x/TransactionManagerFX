package com.example.rubankfx;

import java.text.DecimalFormat;

/**
 * Represents a checking account with associated functionalities.
 * Checking accounts may earn interest and may have associated fees.
 *
 * @author Arun Felix, Digvijay Singh
 */
public class Checking extends Account {

    /** Constant interest rate for the checking account. */
    private static final double INTEREST_RATE = 0.01;

    /** Constant monthly fee for the checking account. */
    private static final double FEE = 12.0;

    /**
     * Constructs a new checking account with the specified holder and balance.
     *
     * @param holder The profile of the account holder.
     * @param balance The initial balance of the account.
     */
    public Checking(Profile holder, double balance) {
        super(holder, balance);
    }

    /**
     * Creates and returns a new Checking instance based on the provided input data.
     * If any input data is missing or invalid, this method may return null or print an error message.
     *
     * @param input An array of strings containing account-related data.
     * @return A new Checking instance constructed from the input data, or null if input data is invalid.
     * @throws NumberFormatException If there's an error in parsing the balance from the input data.
     * @throws IndexOutOfBoundsException If the input data array is shorter than expected.
     * @throws NullPointerException If any required data is missing.
     */
    public static Checking makeChecking(String [] input) throws NumberFormatException, IndexOutOfBoundsException, NullPointerException{
        Profile profile = Profile.makeProfile(input);
        boolean exists = !input[0].equals("C");
        // System.out.println(exists);
        double balance = exists ? Double.parseDouble(input[5]) : 0.0;

        if(balance <= 0 && exists){System.out.println("Initial deposit cannot be 0 or negative.");}
        return (!exists || balance > 0 )? new Checking(profile,balance) : null;
    }

    /**
     * Retrieves the type of the account.
     *
     * @return A string representing the type of the account (e.g., "C" for Checking).
     */
    @Override
    public String GetType(){
        return "C";
    }

    /**
     * Calculates the monthly interest for the Checking account.
     *
     * @return The monthly interest amount.
     */
    @Override
    public double monthlyInterest() {
        double result = balance * (INTEREST_RATE / 12.0);
        DecimalFormat df = new DecimalFormat("#.00");
        String formattedResult = df.format(result);
        return Double.parseDouble(formattedResult);
    }

    /**
     * Calculates the monthly fee for the Checking account.
     *
     * @return The monthly fee amount, or 0 if the balance is below a certain threshold.
     */
    @Override
    public double monthlyFee() {
        if(balance >= 1000){
            return 0;
        }
        return FEE;
    }

    /**
     * Returns a string representation of the Checking account.
     *
     * @return A string representing the Checking account details.
     */
    @Override
    public String toString() {
        return "Checking::" + holder + "::Balance $" + getbalance();
    }

    @Override
    public void applyWithdraw() {
        balance +=  monthlyInterest() - monthlyFee();
    }
}