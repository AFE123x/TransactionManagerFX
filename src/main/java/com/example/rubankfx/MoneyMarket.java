package com.example.rubankfx;
import java.text.DecimalFormat;

/**
 * Represents a Money Market account.
 * @author Arun Felix, Digvijay Singh
 */
public class MoneyMarket extends Savings {
    /** Tracks the number of withdrawals made */
    private int withdrawal;

    /** Constant interest rate for the money market account */
    private static final double INTEREST_RATE_MONEY_MARKET = 0.045; // 4.5%

    /** Minimum balance required for money market account */
    private static final double MIN_BALANCE = 2000.0;

    /** Maximum number of withdrawals allowed without incurring a fee */
    private static final int MAX_WITHDRAWALS = 3;

    /** Fee charged for excessive withdrawals */
    private static final double WITHDRAWAL_FEE = 10.0;

    /**
     * Creates a new Money Market account with the specified holder, balance, and number of withdrawals.
     * Money market accounts has a default loyalty status as True.
     * @param holder The profile of the account holder.
     * @param balance The initial balance of the account.
     */
    public MoneyMarket(Profile holder, double balance) {
        super(holder, balance, true);
        this.withdrawal = 0;
    }


    /**
     * Calculates the monthly interest for the Money Market account.
     * @return The monthly interest.
     */
    @Override
    public double monthlyInterest() {
        double unroundedInterest = balance * ((INTEREST_RATE_MONEY_MARKET/12.0) + (isLoyal ? LOYALTY_BONUS : 0));
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String formattedInterest = decimalFormat.format(unroundedInterest);

        return Double.parseDouble(formattedInterest);
    }

    /**
     * Constructs and returns a MoneyMarket object based on the provided parameters.
     *
     * @param profile object of the user contains name, and date of birth
     * @param balance double pertaining to initial balance of the account to be created.
     * @return A new MoneyMarket instance.
     */
    public static MoneyMarket makeMoneyMarket(Profile profile, double balance){
        return new MoneyMarket(profile, balance);
    }

    /**
     * Calculates the monthly fee for the Money Market account.
     * @return The monthly fee.
     */
    @Override
    public double monthlyFee() {
        if (balance < MIN_BALANCE) {
            isLoyal = false;
            return WITHDRAWAL_FEE;
        } else if (withdrawal > MAX_WITHDRAWALS) {
            return WITHDRAWAL_FEE;
        }
        isLoyal = true;
        return 0;
    }

    /**
     * Deposits the specified amount into the account and checks if the updated balance qualifies the account as loyal.
     * If the balance reaches or exceeds a threshold (in this case, $2000), the account is marked as loyal.
     *
     * @param amount the amount to be deposited into the account; must be a positive value.
     */
    @Override
    public void deposit(double amount){
        balance += amount;
        if(balance >= 2000){
            isLoyal = true;
        }
    }

    /**
     * Withdraws the specified amount from the Money Market account.
     * @param amount The amount to be withdrawn.
     */
    @Override
    public void withdraw(double amount) {
        if(this.balance - amount > 0){
            balance -= amount;
            withdrawal++;
            if(balance < 2000){
                isLoyal = false;
            }
        }
        else{
            helper = "Cannot Withdraw - insufficient fund."+ this.getProfile().toString() + this.GetType();
        }

    }

    String helper;

    /**
     * Retrieves the helper message. This message is typically used to provide feedback on the last operation performed.
     *
     * @return the current helper message as a String.
     */
    public String getHelper() {
        return helper;
    }

    /**
     * Applies the fees to the balance, and resets withdrawal to zero.
     */
    @Override
    public void applyWithdraw() {
        balance +=  monthlyInterest() - monthlyFee();
        if(balance < 2000){
            isLoyal = false;
        }
        else{
            isLoyal = true;
        }
        withdrawal = 0;
    }

    /** Returns the type of the Account
     * @return A string representing the type of the account, in this case "MM"
     */
    @Override
    public String GetType(){
        return "MM";
    }

    /**
     * Returns a string representation of the Money Market account.
     * @return A string representing the Money Market account.
     */
    @Override
    public String toString() {
        //Money Market::Savings::Roy Brooks 10/31/1979::Balance $2,909.10::is loyal::withdrawal: 0
        //Money Market::Savings::April March 1/15/1987::Balance $2,500.00::is loyal::withdrawal: 0
        String royalty = isLoyal ? "::is loyal" : "";
        return "Money Market::Savings::" + holder + " " + holder.getDob().toString() + "::Balance $" + getbalance() + "" + royalty + "::withdrawal: " + withdrawal;
    }
}
