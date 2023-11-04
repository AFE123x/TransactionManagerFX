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
     * Constructs a new checking account with the specified holder and balance.
     *
     * @param holder The profile of the account holder.
     * @param balance The initial balance of the account.
     */
    public static Checking makeChecking(Profile profile, double balance) throws Exception {

        return new Checking(profile, balance);
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

    /**
     * Applies the withdrawal logic specific to this account type.
     * For the Checking account, this method updates the balance by adding monthly interest and subtracting monthly fees.
     */
    @Override
    public void applyWithdraw() {
        balance +=  monthlyInterest() - monthlyFee();
    }
}
