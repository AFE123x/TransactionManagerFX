package com.example.rubankfx;

import java.text.DecimalFormat;

/**
 * Represents a College Checking account type which extends the base Checking class.
 * The College Checking account has an associated interest rate and fee,
 * as well as a campus association for the account holder.
 *
 * @author Arun Felix, Digvijay Singh
 */
public class CollegeChecking extends Checking {

    /** Constant interest rate for the College Checking account */
    private static final double INTEREST_RATE = 0.01;

    /** Constant fee for the College Checking account */
    private static final double FEE = 0.0;

    /**
     * Enum representing various campuses.
     */
    private enum Campus {
        NEW_BRUNSWICK(0, "New Brunswick"),
        NEWARK(1, "Newark"),
        CAMDEN(2, "Camden");
        /** Value associated with the campus */
        private final int value;
        /** Name of the campus */
        private final String name;

        /**
         * Constructor for Campus enum.
         *
         * @param value the associated value of the campus
         * @param name the name of the campus
         */
        Campus(int value, String name) {
            this.value = value;
            this.name = name;
        }
    }
    //0 – New Brunswick, 1 – Newark, 2 – Camden

    /** Variable storing the associated campus for the account */
    private Campus campus;

    /**
     * Constructs a new College Checking account.
     *
     * @param holder profile of the account holder
     * @param balance initial balance of the account
     * @param campusCode code representing the associated campus
     */
    public CollegeChecking(Profile holder, double balance, int campusCode) {
        super(holder, balance);
        switch(campusCode){
            case 0:
                this.campus = Campus.NEW_BRUNSWICK;
                break;
            case 1:
                this.campus = campus.NEWARK;
                break;
            case 2:
                this.campus = campus.CAMDEN;
                break;
            default:
                this.campus = null;
                break;
        }
    }

    /**
     * Creates a new CollegeChecking account with the specified profile, balance, and campus code.
     * This static factory method enforces validation on the campus code before creating a new account.
     *
     * @param profile     the profile of the account holder including personal details such as name and date of birth.
     * @param balance     the initial balance to be set for the new CollegeChecking account.
     * @param campusCode  the campus code associated with the account, which must be within valid range.
     * @return a new instance of CollegeChecking account with the specified details.
     * @throws IllegalArgumentException if the provided campus code is outside of the valid range (0 to 2).
     */
    public static CollegeChecking makeCollegeChecking(Profile profile, double balance, int campusCode)
            throws IllegalArgumentException {

        if (campusCode < 0 || campusCode > 2) {
            throw new IllegalArgumentException("Invalid campus code.");
        }

        return new CollegeChecking(profile, balance, campusCode);
    }

    /**
     * Returns the monthly interest added to the account.
     *
     * @return the balance, or the total amount of money in the account after the balance has been added
     */
    @Override
    public double monthlyInterest() {
        double unroundedInterest = balance * (INTEREST_RATE/12);

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String formattedInterest = decimalFormat.format(unroundedInterest);

        return Double.parseDouble(formattedInterest);
    }

    /**
     * Returns the monthly fee for the account.
     *
     * @return a double representing fee of the account
     */
    @Override
    public double monthlyFee() {
        return FEE;
    }

    /**
     * Returns the type of the account.
     *
     * @return a string representing the type of the account
     */
    @Override
    public String GetType(){
        return "CC";
    }
    /**
     * withdraw the fees from account.
     */
    @Override
    public void applyWithdraw() {
        balance +=  monthlyInterest() - monthlyFee();
    }

    //College Checking::Roy Brooks 10/31/1999::Balance $2,909.10::NEWARK
    /**
     * Returns a string representation of the College Checking account.
     *
     * @return a string representing the College Checking account
     */
    public String toString() {
        String temp =  "College Checking::" + holder + "::Balance $" + getbalance();
        return temp;
    }
}
