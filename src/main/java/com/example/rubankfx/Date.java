package com.example.rubankfx;

import java.util.Calendar;

/**
 * The Date class represents a valid date object. It provides functionality to check the validity of a date, compare it to other dates, and
 * convert it to a string for representation. This class is immutable once created.
 *
 * @author Digvijay Singh, Arun Felix
 */
public class Date implements Comparable<Date>{

    // constants
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUATERCENTENNIAL = 400;
    public static final int JAN = 1;
    public static final int FEB = 2;
    public static final int MAR = 3;
    public static final int APR = 4;
    public static final int MAY = 5;
    public static final int JUN = 6;
    public static final int JUL = 7;
    public static final int AUG = 8;
    public static final int SEP = 9;
    public static final int OCT = 10;
    public static final int NOV = 11;
    public static final int DEC = 12;


    private int year;
    private int month;
    private int day;
    private String lastMessage;

    /**
     * Initializes a new Date object with a specified year, month, and day.
     * Once created, the object is immutable.
     *
     * @param year The year for the date.
     * @param month The month for the date.
     * @param day The day for the date.
     */
    Date (int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Static function which creates the Date object.
     * @param input String containing date in MM/DD/YYYY format.
     * @return Date object assuming parsing succeeds. Null otherwise.
     */
    public static Date makeDate(String input){
        try {
            String [] dateArray = input.split("/");
            int month = Integer.parseInt(dateArray[0]);
            int day = Integer.parseInt(dateArray[1]);
            int year = Integer.parseInt(dateArray[2]);
            return new Date(year,month,day);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            return null;
        }
    }

    /**
     * Returns the year component of the date.
     *
     * @return The year.
     */
    public int getyear(){
        return this.year;
    }

    /**
     * Returns the month component of the date.
     *
     * @return The month.
     */
    public int getmonth(){
        return this.month;
    }

    /**
     * Returns the day component of the date.
     *
     * @return The day.
     */
    public int getDay(){
        return this.day;
    }

    /**
     * Checks if the date is valid and meets the specific conditions:
     * 1. It is a future date or the current date.
     * 2. The age of all account holders must be above 16.
     * 3. The day and month combination is valid.
     *
     * @return true if the date is valid according to the mentioned conditions,
     * @return false otherwise. If the date is not valid, it prints an error message
     */
    public Boolean isValid() {
        if (month < JAN || month > DEC || day < 1 || isInvalidDayForMonth()) return printError();

        Calendar currCalendar = Calendar.getInstance();
        Date currDate = new Date(currCalendar.get(Calendar.YEAR), currCalendar.get(Calendar.MONTH) + 1, currCalendar.get(Calendar.DAY_OF_MONTH));

        // if age if current or future date
        if(this.compareTo(currDate) >= 0){
            lastMessage = "Date cannot be the current or Future Date";
            return false;
        }

        int currentYear = currCalendar.get(Calendar.YEAR);
        int age = currentYear - year;
        if (month > currCalendar.get(Calendar.MONTH) + 1 ||
                (month == currCalendar.get(Calendar.MONTH) + 1 && day > currCalendar.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }


        // Check if age is 16 or above
        if(age < 16){
            lastMessage = "Age cannot be less than 16!";
        }
        return age >= 16;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    /**
     * Checks if the date (representing a date of birth) is valid for a College Checking account.
     * The criteria for validity is based on an age range of 16 to 24, inclusive.
     *
     * @return true if the age derived from the date is between 16 to 24, inclusive;
     *         false otherwise.
     */
    public boolean checkCollegeCheckingValidity() {
        Calendar currCalendar = Calendar.getInstance();
        int currentYear = currCalendar.get(Calendar.YEAR);
        int age = currentYear - year;

        if (month > currCalendar.get(Calendar.MONTH) + 1 ||
                (month == currCalendar.get(Calendar.MONTH) + 1 && day > currCalendar.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }
        // System.out.println(age);
        if (age >= 24) {
            lastMessage = "Age cannot be more than 24 for College Checking";
            return false;
        } else if (age < 16) {
            lastMessage = "Age cannot be less than 16";
            return false;
        }

        return true;
    }




    /**
     * Prints an error message with the date in case the date is invalid.
     *
     * @return returns false indicating that the date is invalid.
     */
    private boolean printError() {
        System.out.println(toString() + ": Invalid calendar date!");
        return false;
    }

    /**
     * Checks if the day is valid for the given month of the Date object.
     * It considers the different number of days in each month and also
     * accounts for leap years for February.
     *
     * @return true if the day is invalid for the given month, false otherwise.
     */
    private boolean isInvalidDayForMonth() {
        int maxDays;
        if (month == FEB) {
            maxDays = isLeapYear(year) ? 29 : 28;
        } else {
            maxDays = (month == APR || month == JUN || month == SEP || month == NOV) ? 30 : 31;
        }
        return day > maxDays;
    }

    /**
     * Compares the specified object with this date for equality.
     * Return true if and only if the specified object is
     * also a date and both dates represent the same year, month, and day.
     *
     * @param the object to be compared for equality with this date.
     * @return true if the specified object is equal to this date,
     *         false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Date otherDate = (Date) obj;

        // Check if year, month, and day are equal
        return year == otherDate.year
                && month == otherDate.month
                && day == otherDate.day;
    }

    /**
     * Checks if year is a leap year
     * @param year integer
     * @return true if year is leap, false otherwise.
     */
    private boolean isLeapYear(int year) {
        if (year % QUADRENNIAL != 0) {
            return false;
        }

        if (year % CENTENNIAL != 0) {
            return true;
        }

        return year % QUATERCENTENNIAL == 0;
    }


    /**
     *  Compares two date instances;
     * @param o Date we can to compare
     * @return negative number if this date is less than o, positive if greatetr, 0 if equal.
     */
    @Override
    public int compareTo(Date o) {
        if(this.year != o.year){
            return this.year - o.year;
        };
        if(this.month != o.month){
            return this.month - o.month;
        }
        else return this.day - o.day;
    }

    /**  This method returns a string representation of the date object in the "month/day/year" format
     * @return A string representation of the date object.
     */
    @Override
    public String toString(){
        return this.month + "/" + this.day + "/" + this.year;
    }

    /**
     * This method contains various test scenarios for date validity and
     * college checking validity based on age constraints.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // 1. Test with valid month and day
        testDateValidity(2005, 5, 15, true);

        // 2. Test for a future date (Assuming current year is 2023 for simplicity)
        testDateValidity(2025, 5, 15, false);

        // 3. Test for a past date with age less than 16
        testDateValidity(2010, 5, 15, false);

        // 4. Test for a past date with age of exactly 16
        testDateValidity(2007, 10, 18, false);

        // 5. Test for a past date with age between 16 and 24
        testDateValidity(2000, 5, 15, true);

        // 6. Test for a past date with age more than 24
        testDateValidity(1995, 5, 15, true);

        // 7. Test for leap year conditions
        // 7.1 Valid leap year date
        testDateValidity(2004, 2, 29, true);
        // 7.2 Invalid leap year date
        testDateValidity(2003, 2, 29, false);

        // 8. Test for College Checking account validity
        // 8.1 Age below 16
        testCollegeCheckingValidity(2010, 5, 15, false);
        // 8.2 Age 16
        testCollegeCheckingValidity(2007, 10, 14, true);
        // 8.3 Age 20 (within range 16-24)
        testCollegeCheckingValidity(2003, 5, 15, true);
        // 8.4 Age 25 (out of range 16-24)
        testCollegeCheckingValidity(1998, 5, 15, false);
    }
    /**
     * Test the validity of a Date object by comparing it to an expected result.
     *
     * @param year     The year component of the Date.
     * @param month    The month component of the Date.
     * @param day      The day component of the Date.
     * @param expected The expected validity result (true for valid, false for invalid).
     */
    private static void testDateValidity(int year, int month, int day, boolean expected) {
        Date date = new Date(year, month, day);
        boolean isValid = date.isValid();
        String result = isValid == expected ? "PASSED" : "FAILED";
        System.out.printf("Testing Date: %s Expected: %s Actual: %s Result: %s%n", date, expected, isValid, result);
    }
    /**
     * Test the validity of a College Checking Date object by comparing it to an expected result.
     *
     * @param year     The year component of the College Checking Date.
     * @param month    The month component of the College Checking Date.
     * @param day      The day component of the College Checking Date.
     * @param expected The expected validity result (true for valid, false for invalid).
     */
    private static void testCollegeCheckingValidity(int year, int month, int day, boolean expected) {
        Date date = new Date(year, month, day);
        boolean isValid = date.checkCollegeCheckingValidity();
        String result = isValid == expected ? "PASSED" : "FAILED";
        System.out.printf("Testing College Checking Date: %s Expected: %s Actual: %s Result: %s%n", date, expected, isValid, result);
    }

}
