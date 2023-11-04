package com.example.rubankfx;

/**
 * The Profile class represents an individual's profile with attributes such as first name, last name, and date of birth.
 * It provides functionalities to create a profile object from a string array and compare profiles.
 *
 * @author Digvijay Singh, Arun Felix
 */
public class Profile implements Comparable<Profile> {

    /**First name of the profile holder*/
    private String fname;

    /** Last name of the profile holder*/
    private String lname;

    /** Date of birth of the profile holder*/
    private Date dob;

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

    /**
     * Returns a string representation of the Profile, which includes the first name, last name, and date of birth.
     * The format is: "FirstName LastName DateOfBirth", where DateOfBirth is provided by the {@code toString} method of the {@code Date} class.
     *
     * @return a string representation of the Profile.
     */
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

    /**
     * Checks if this Profile is equal to another object. Equality is determined by comparing the state of this Profile to the state of the other object.
     * If the other object is also a Profile and their states match (based on {@code compareTo}), they are considered equal.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the {@code obj} argument; {@code false} otherwise.
     */
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
