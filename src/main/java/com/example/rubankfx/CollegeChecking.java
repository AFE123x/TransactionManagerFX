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
        NEW_BRUNSWICK("0", "New Brunswick"),
        NEWARK("1", "Newark"),
        CAMDEN("2", "Camden");
        /** Value associated with the campus */
        private final String value;
        /** Name of the campus */
        private final String name;

        /**
         * Constructor for Campus enum.
         *
         * @param value the associated value of the campus
         * @param name the name of the campus
         */
        Campus(String value, String name) {
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
    public CollegeChecking(Profile holder, double balance, String campusCode) {
        super(holder, balance);
        switch(campusCode){
            case "0":
                this.campus = Campus.NEW_BRUNSWICK;
                break;
            case "1":
                this.campus = campus.NEWARK;
                break;
            case "2":
                this.campus = campus.CAMDEN;
                break;
            default:
                this.campus = null;
                break;
        }
    }

    /**
     * Creates and returns a new CollegeChecking instance based on the provided input data.
     * The input array is expected to contain account-related data, such as holder details, balance, and campus code.
     * If any input data is missing or invalid, this method may return null or throw an exception.
     *
     * @param input An array of strings containing account-related data.
     *              The first element is expected to be an account type identifier ("C" for CollegeChecking).
     *              Subsequent elements are expected to contain profile details, balance, and campus code.
     * @return A new CollegeChecking instance constructed from the input data, or null if input data is invalid.
     * @throws NumberFormatException If there's an error in parsing the balance from the input data.
     * @throws IndexOutOfBoundsException If the input data array is shorter than expected.
     */
    public static CollegeChecking makeCollegeChecking(String [] input) throws NumberFormatException, IndexOutOfBoundsException{
        Profile profile = Profile.makeProfile(input);
        if(profile == null){
            return null;
        }

        if(!profile.getDob().checkCollegeCheckingValidity()){

            return null;
        }
        Boolean exists = input[0].equals("C") ? false : true;
        // System.out.println(exists);
        double tempbalance = exists == true ? Double.parseDouble(input[5]) : 0.0;
        String tempcampusCode = input[0].equals("O") ? input[6] : null;
        if(exists == true && tempbalance <= 0){System.out.println("Initial deposit cannot be 0 or negative."); return null;}
        if(!input[0].equals("C")){
            int jahr = Integer.parseInt(input[6]);
            if(jahr < 0 || jahr > 2){System.out.println("Invalid campus code."); return null;}
        }
        else{
            tempcampusCode = "_";
        }
        return  new CollegeChecking(profile, tempbalance, tempcampusCode);
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