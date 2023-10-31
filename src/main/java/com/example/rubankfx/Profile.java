package com.example.rubankfx;

/**
 * The Profile class represents an individual's profile with attributes such as first name, last name, and date of birth.
 * It provides functionalities to create a profile object from a string array and compare profiles.
 *
 * @author Digvijay Singh, Arun Felix
 */
public class Profile implements Comparable<Profile> {

    private String fname; // First name of the profile holder
    private String lname; // Last name of the profile holder
    private Date dob;     // Date of birth of the profile holder

    /**
     * Initializes a new Profile object with a specified first name, last name, and date of birth.
     *
     * @param fname The first name for the profile.
     * @param lname The last name for the profile.
     * @param dob The date of birth for the profile.
     */
    public Profile(String fname, String lname, Date dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    /**
     * Creates and returns a Profile object from a string array.
     * The input array is expected to have the date of birth at index 4,
     * first name at index 2, and last name at index 3.
     *
     * @param input The string array containing profile details.
     * @return A Profile object or null if the input is not valid.
     */
    public static Profile makeProfile(String[] input) {
        Date dob = Date.makeDate(input[3]);
        if (dob.isValid()){
            return new Profile(input[1], input[2], dob);
        }else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * Retrieves the first name of the profile holder.
     *
     * @return The first name.
     */
    public String getFname() {
        return fname;
    }

    /**
     * Retrieves the last name of the profile holder.
     *
     * @return The last name.
     */
    public String getLname() {
        return lname;
    }

    /**
     * Retrieves the date of birth of the profile holder.
     *
     * @return The date of birth.
     */
    public Date getDob() {
        return dob;
    }

    public String toString(){
        //John Doe 2/19/2000
        return fname + " " + lname + " " + dob.toString();
    }
    /**
     * Compares the current profile to another profile object.
     * The method currently returns 0, indicating that profiles are treated as equal.
     * This should be enhanced to provide a meaningful comparison.
     *
     * @param profile The profile to be compared.
     * @return Currently returns 0.
     */
    @Override
    public int compareTo(Profile profile) {
        int firstcompare = this.lname.toLowerCase().compareTo(profile.getLname().toLowerCase());
        if(firstcompare != 0){
            return firstcompare;
        }
        firstcompare = this.fname.toLowerCase().compareTo(profile.getFname().toLowerCase());
        if(firstcompare != 0){
            return firstcompare;
        }
        return this.dob.compareTo(profile.dob);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj instanceof Profile){
            Profile temp = (Profile)obj;
            return this.compareTo(temp) == 0;
        }
        return false;
    }
}