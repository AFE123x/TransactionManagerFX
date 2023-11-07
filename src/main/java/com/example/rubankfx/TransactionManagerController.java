package com.example.rubankfx;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

/**
 * Controller for the Transaction Manager application.
 * This class handles the user interactions with the GUI for account transactions,
 * including opening and closing accounts, depositing and withdrawing funds,
 * and viewing account details.
 * It implements the {@code Initializable} interface to perform setup operations
 * when the FXML file is loaded.
 * @author Digvijay Singh, Arun felix
 */
public class TransactionManagerController implements Initializable{

    /*
        This area contains all the components from the Open/Close page.
         */
    /** Represents the database of accounts. */
    AccountDatabase database = new AccountDatabase();

    /** Toggle group for campus radio buttons, ensuring mutual exclusivity in campus selection. */
    @FXML
    private ToggleGroup campusToggleGroup;

    /** Input field for the initial deposit amount when opening a new account. */
    @FXML
    public TextField InitDeposit;

    /** Input field for the first name in the account creation section. */
    @FXML
    private TextField OC_First_Name;

    /** Input field for the last name in the account creation section */
    @FXML
    private TextField OC_Last_Name;

    /** Date picker for selecting the date of birth in the account creation section. */
    @FXML
    private DatePicker OC_DOB;

    /** Radio button for selecting a checking account type in the account creation section. */
    @FXML
    private RadioButton OC_Checking;

    /** Radio button for selecting a college checking account type in the account creation section. */
    @FXML
    private RadioButton OC_CC;

    /** Radio button for selecting a savings account type in the account creation section. */
    @FXML
    private RadioButton OC_Savings;

    /** Radio button for selecting a money market account type in the account creation section. */
    @FXML
    private RadioButton OC_MM;

    /** Checkbox to indicate loyalty status for a savings account. */
    public CheckBox Loyalty;

    /** Button to open a new account. */
    public Button Open;

    /** Button to close an existing account. */
    public Button Close;

    /** Button to clear the input fields in the account creation section. */
    public Button Clear;

    /** Radio button for selecting the New Brunswick campus in the college checking account section. */
    @FXML
    private RadioButton Campus_NB;

    /** Radio button for selecting the Newark campus in the college checking account section. */
    @FXML
    private RadioButton Campus_NW;

    /** Radio button for selecting the Camden campus in the college checking account section. */
    @FXML
    private RadioButton Campus_C;

    /**
     * Handles radio button selection to ensure only one account type is selected at a time.
     * Disables or enables campus radio buttons based on the account type selected.
     *
     * @param event The event that triggered this method call, expected to be a selection on a radio button.
     */
    @FXML
    private void OChandleRadioButtonAction(ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton) event.getSource();
        if (selectedRadioButton == OC_Checking) {
            clearcampus();
            OC_CC.setSelected(false);
            OC_Savings.setSelected(false);
            OC_MM.setSelected(false);
            Loyalty.setSelected(false);
        } else if (selectedRadioButton == OC_CC) {
            OC_Checking.setSelected(false);
            OC_Savings.setSelected(false);
            OC_MM.setSelected(false);
            Loyalty.setSelected(false);
        } else if (selectedRadioButton == OC_Savings) {
            clearcampus();
            OC_Checking.setSelected(false);
            OC_CC.setSelected(false);
            OC_MM.setSelected(false);
            clearcampus();
        } else if (selectedRadioButton == OC_MM) {
            OC_Checking.setSelected(false);
            OC_CC.setSelected(false);
            OC_Savings.setSelected(false);
            clearcampus();
            Loyalty.setSelected(false);
        }
        Campus_NB.setDisable(!OC_CC.isSelected());
        Campus_NW.setDisable(!OC_CC.isSelected());
        Campus_C.setDisable(!OC_CC.isSelected());
    }

    /**
     * Clears the campus radiobuttons based on Account selection.
     */
    private void clearcampus(){
        Campus_NB.setSelected(false);
        Campus_NW.setSelected(false);
        Campus_C.setSelected(false);
    }


    /**
     * Attempts to open a new account based on the input provided by the user.
     * Validates the provided input and, if valid, creates an account of the selected type
     * with the specified initial deposit and customer details.
     *
     * <p>This method will update the messageListView with the status of the operation, such as
     * successful account creation or an error message if the operation fails.</p>
     *
     * @param event The event that triggered this method call, typically a button press indicating
     *              the user's intent to open a new account.
     */
    public boolean OpenAccount(ActionEvent event){
        try {
            LocalDate localDate = OC_DOB.getValue();
            if (localDate == null) {
                messageListView.getItems().add("Please enter a valid date! ");
                return false;
            }
            Date date = new Date(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
            if (!date.isValid()) {
                messageListView.getItems().add(date.getLastMessage());
                return false;
            }
            if(OC_First_Name.getText().isEmpty() || OC_Last_Name.getText().isEmpty()){
                messageListView.getItems().add("Please enter a name! ");
                return false;
            }
            if(InitDeposit.getText().isEmpty()){
                addMessageToListView("Please add a balance");
                return false;
            }
            Profile profile = makeProfile(date);
            double balance;
            try{ balance = parseInitialDeposit(InitDeposit.getText());
                if (balance <= 0) {
                    messageListView.getItems().add("Initial deposit must be greater than zero!");
                    return false;
                }
            }catch(NumberFormatException e){ addMessageToListView("Enter a valid number");
                return false;
            }
            int decision = OCAcctdecision();
            switch (decision) {
                case 0: openCheckingAccount(profile, balance);
                    break;
                case 1: openCollegeCheckingAccount(profile, balance, date);
                    break;
                case 2: boolean loyal = Loyalty.isSelected();
                    openSavingsAccount(profile, balance, loyal);
                    break;
                case 3: openMoneyMarketAccount(profile,balance);
                    break;
                default: addMessageToListView("Please select an account type!"); return false;
            }
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Attempts to open a new Checking account in the bank database.
     * @param profile the customer's profile information
     * @param balance the initial deposit balance for the account
     */
    private void openCheckingAccount(Profile profile, double balance) {
        Checking checkingAccount = Checking.makeChecking(profile, balance);
        boolean added = database.open(checkingAccount);
        handleAccountOpeningResult(added);
    }

    /**
     * Attempts to open a new College Checking account in the bank database with a campus code.
     * If the campus code is invalid or the account's date is not valid for a college checking account, the process is aborted.
     * @param profile the customer's profile
     * @param balance the initial deposit balance for the account
     * @param date the date to validate college checking account eligibility
     */
    private void openCollegeCheckingAccount(Profile profile, double balance,Date date) {
        Integer campusCodeInt = validateAndGetCampusCode();
        if (campusCodeInt == null) {
            return; // Exit since either no code was selected or it was invalid.
        }
        if (!date.checkCollegeCheckingValidity()) {
            messageListView.getItems().add(date.getLastMessage());
            return;
        }
        try {
            CollegeChecking collegeChecking = CollegeChecking.makeCollegeChecking(profile, balance, campusCodeInt);
            boolean added = database.open(collegeChecking);
            handleAccountOpeningResult(added);
        } catch (IllegalArgumentException e) {
            showAlert("Error", "Please enter all details");
        }
    }

    /**
     * Attempts to open a new Savings account in the bank database, flagging the account as loyal if applicable.
     * @param profile the customer's profile information
     * @param balance the initial deposit balance for the account
     * @param loyal a boolean flag indicating if the account is a loyal account
     */
    private void openSavingsAccount(Profile profile, double balance, boolean loyal) {
        try {
            Savings savingAccount = Savings.makeSavings(profile, balance, loyal);
            boolean added = database.open(savingAccount);
            handleAccountOpeningResult(added);
        } catch (IllegalArgumentException e) {
            showAlert("Error", e.getMessage());
        }catch (DateTimeParseException e){
            showAlert("Error", "bad date format");
        }
    }

    /**
     * Attempts to open a new Money Market account in the bank database.
     * If the initial deposit is less than the required minimum, the process is aborted.
     * @param profile the customer's profile information
     * @param balance the initial deposit balance for the account
     */
    private void openMoneyMarketAccount(Profile profile, double balance) {
        if(balance < 2000) {
            messageListView.getItems().add("Balance cannot be below 2000!");
            return;
        }
        try {
            MoneyMarket moneyMarket = MoneyMarket.makeMoneyMarket(profile, balance);
            boolean added = database.open(moneyMarket);
            handleAccountOpeningResult(added);
        }
        catch (IllegalArgumentException e) {
            showAlert("Error", e.getMessage());
        }
    }

    /**
     * Handles the result of attempting to open an account, displaying an appropriate message.
     * @param added a boolean indicating if the account was successfully added to the database
     */
    private void handleAccountOpeningResult(boolean added) {
        if (!added && database.getLastMessage().equals("AE")) {
            messageListView.getItems().add("Account already exists!");
        } else {
            messageListView.getItems().add("Account opened successfully!");
        }
    }

    /**
     * Parses the initial deposit amount from a string.
     * @param initialDepositText the string representing the initial deposit
     * @return the parsed deposit amount as a double
     * @throws NumberFormatException if the initial deposit text cannot be parsed as a double
     */
    private double parseInitialDeposit(String initialDepositText) throws NumberFormatException {
        double balance = Double.parseDouble(initialDepositText);
        return balance;
    }

    /**
     * Validates the selected campus code.
     *
     * @return The selected campus code as an Integer or null if no valid code is selected or if the code is invalid.
     */
    private Integer validateAndGetCampusCode() {
        String campusCode = getSelectedCampusCode();
        if (campusCode == null) {
            messageListView.getItems().add("No campus code selected! Please select a valid code! ");
            return null;
        }

        try {
            return Integer.parseInt(campusCode);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid campus code.");
            return null;
        }
    }



    /**
     * Creates a new profile using the provided date and the names from input fields.
     *
     * @param date The date to be used for the profile creation, typically the user's date of birth.
     * @return A Profile instance with the user's first name, last name, and date of birth.
     */
    private Profile makeProfile(Date date) {
        String firstName = OC_First_Name.getText();
        String lastName = OC_Last_Name.getText();
        return new Profile(firstName, lastName, date);
    }


    /**
     * Displays an alert dialog to the user with a specific error message.
     *
     * @param error   The title of the alert dialog, typically "Error".
     * @param message The message to be displayed in the body of the alert dialog.
     */
    private void showAlert(String error, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Adds a message to the end of the message list view and auto-scrolls to the latest message.
     *
     * @param message The message to be added to the messageListView.
     */
    private void addMessageToListView(String message) {
        messageListView.getItems().add(message);
        messageListView.scrollTo(messageListView.getItems().size() - 1);
    }


    /**
     * Retrieves the code of the selected account type based on the UI selection.
     *
     * @return A String representing the selected account type, or null if no selection is made.
     */
    private String getSelectedAccountType() {
        if (OC_Checking.isSelected()) {
            return "C";
        } else if (OC_CC.isSelected()) {
            return "CC";
        } else if (OC_Savings.isSelected()) {
            return "S";
        } else if (OC_MM.isSelected()) {
            return "MM";
        } else {
            return null;
        }
    }



    /**
     * Retrieves the campus code based on the selected radio button in the UI.
     *
     * @return A String representing the selected campus code, or null if no campus is selected.
     */

    private String getSelectedCampusCode() {
        if (Campus_NB.isSelected()) {
            return "0";
        } else if (Campus_NW.isSelected()) {
            return "1";
        } else if (Campus_C.isSelected()) {
            return "2";
        }
        return null;
    }


    /**
     * Handles the action to close an account.
     * It validates the user's input and closes the selected account if it exists.
     *
     * @param event The event that triggered the method call, typically a button press.
     */
    @FXML
    void CloseAccount(ActionEvent event){
        try{
            String type = getSelectedAccountType();
            if(type == null){
                addMessageToListView("Please Select an Account type!");
                return;
            }
            boolean closed;
            LocalDate localDate = OC_DOB.getValue();
            if (localDate == null) {
                addMessageToListView("Please enter a valid date! ");
                return;
            }
            Date date = new Date(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
            if (!date.isValid()) {
                messageListView.getItems().add(date.getLastMessage());
                  return;
            }
            Profile profile = makeProfile(date);
            Account accountToClose = database.getAccountByProfileAndType(profile, type);
            if(accountToClose == null){
                addMessageToListView("Account does not Exist!");
                return;
            }else{
                closed = database.close(accountToClose);
                if(closed){
                    addMessageToListView("Account Closed Successfully!");
                }
            }
        }catch(Exception e){
            addMessageToListView("Fatal error occurred! Account cannot be removed");
        }

    }

    /**
     * Determines the decision for the account type to be opened based on the radio button selection.
     *
     * @return An integer representing the account decision, where each number corresponds to a specific account type.
     */
    @FXML
    private int OCAcctdecision() {
        if (OC_Checking.isSelected()) {
            return 0;
        } else if (OC_CC.isSelected()) {
            return 1;
        } else if (OC_Savings.isSelected()) {
            return 2;
        } else if (OC_MM.isSelected()) {
            return 3;
        }
        return -1;
    }


    /**
     * Creates a new profile for deposit/withdrawal operations using the provided date and the names from input fields.
     *
     * @param date The date to be used for the profile creation, typically the user's date of birth.
     * @return A Profile instance with the user's first name, last name, and date of birth for deposit/withdrawal operations.
     */
    private Profile makeProfileDW(Date date) {
        String firstName = DW_First_Name.getText();
        String lastName = DW_Last_Name.getText();
        return new Profile(firstName, lastName, date);
    }

    /**
     * Clears all messages from the screen.
     *
     * @param actionEvent The event that triggered the method call, typically a button press to clear the screen.
     */
    public void ClearScreen(ActionEvent actionEvent) {
        messageListView.getItems().clear();
    }


    /**
     * The ListView that displays messages to the user, such as success notifications or error messages.
     */
    @FXML
    private ListView<String> messageListView;

    //DEPOSIT AND WITHDRAW STUFF

    /**
     * The TextField where the user enters their first name for deposit and withdrawal operations.
     */
    @FXML
    private TextField DW_First_Name;

    /**
     * The TextField where the user enters their last name for deposit and withdrawal operations.
     */
    @FXML
    private TextField DW_Last_Name;

    /**
     * The DatePicker where the user selects their date of birth for deposit and withdrawal operations.
     */
    @FXML
    private DatePicker DW_DOB;
    /**
     * The RadioButton for selecting a Checking account in the deposit and withdrawal section.
     */
    @FXML
    private RadioButton DW_Checking;
    /**
     * The RadioButton for selecting a College Checking account in the deposit and withdrawal section.
     */
    @FXML
    private RadioButton DW_CC;
    /**
     * The RadioButton for selecting a Savings account in the deposit and withdrawal section.
     */
    @FXML
    private RadioButton DW_Savings;
    /**
     * The RadioButton for selecting a Money Market account in the deposit and withdrawal section.
     */
    @FXML
    private RadioButton DW_MM;

    /**
     * Handles the action for when a Deposit or Withdraw RadioButton is selected.
     * It ensures that only one account type RadioButton can be selected at a time.
     *
     * @param event The action event that occurred.
     */
    @FXML
    private void DWhandleRadioButtonAction(ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton) event.getSource();
        if (selectedRadioButton == DW_Checking) {
            DW_CC.setSelected(false);
            DW_Savings.setSelected(false);
            DW_MM.setSelected(false);
        } else if (selectedRadioButton == DW_CC) {
            DW_Checking.setSelected(false);
            DW_Savings.setSelected(false);
            DW_MM.setSelected(false);
        } else if (selectedRadioButton == DW_Savings) {
            DW_Checking.setSelected(false);
            DW_CC.setSelected(false);
            DW_MM.setSelected(false);
        } else if (selectedRadioButton == DW_MM) {
            DW_Checking.setSelected(false);
            DW_CC.setSelected(false);
            DW_Savings.setSelected(false);
        }
    }

    /**
     * The TextField for inputting the balance for deposit or withdrawal.
     */
    @FXML
    public TextField BalanceDW;

    /**
     * The Button to trigger a deposit action.
     */
    public Button Deposit;

    /**
     * The Button to trigger a withdrawal action.
     */
    @FXML
    public Button Withdraw;

    /**
     * Determines the selected account type based on the RadioButton that is selected.
     *
     * @return A string representing the selected account type, or {@code null} if no account is selected.
     */
    private String DWAcctdecision() {
        if (DW_Checking.isSelected()) {
            return "C";
        } else if (DW_CC.isSelected()) {
            return "CC";
        } else if (DW_Savings.isSelected()) {
            return "S";
        } else if (DW_MM.isSelected()) {
            return "MM";
        }
        return null;
    }


    /**
     * Handles the deposit action. It validates the amount and date, retrieves the selected account,
     * and performs a deposit.
     *
     * @param event The action event that occurred.
     */
    @FXML
    private boolean deposit(ActionEvent event){
        String type = DWAcctdecision();
        if(type == null){
            addMessageWithdrawView("Please Select an Account type!");
            return false;
        }
        double balance;
        try{
            balance = Double.parseDouble(BalanceDW.getText());
        }catch(NumberFormatException e){
            addMessageWithdrawView("Please enter a valid amount to deposit");
            return false;
        }
        LocalDate localDate = DW_DOB.getValue();
        if (localDate == null) {
            addMessageWithdrawView("Please enter a valid date! ");
            return false;
        }
        Date date = new Date(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        if (!date.isValid()) {
            addMessageWithdrawView(date.getLastMessage());
            return false;
        }
        Profile profile = makeProfileDW(date);
        Account accountToDeposit = database.getAccountByProfileAndType(profile, type);
        if(accountToDeposit == null){
            addMessageWithdrawView("Account does not exist!");
            return false;
        }
        else {
            accountToDeposit.deposit(balance);
            addMessageWithdrawView("Deposit successful");
        }
        return true;
    }

    /**
     * Handles the withdrawal action. It validates the amount and date, retrieves the selected account,
     * and performs a withdrawal.
     *
     * @param event The action event that occurred.
     * @return true/false depending on whether money was withdrawn successfully.
     */
    @FXML
    private boolean withDraw(ActionEvent event){
        String type = DWAcctdecision();
        double balance;
        if(type == null){
            addMessageToListView("Please Select an Account type!");
            return false;
        }
        try {
            balance = Double.parseDouble(BalanceDW.getText());
        }catch(NumberFormatException e){
            addMessageWithdrawView("Please enter a valid account to withdraw!");
            return false;
        }
        LocalDate localDate = DW_DOB.getValue();
        if (localDate == null) {
            addMessageWithdrawView("Please enter a valid date! ");
            return false;
        }
        Date date = new Date(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        if (!date.isValid()) {
            addMessageWithdrawView(date.getLastMessage());
            return false;
        }
        Profile profile = makeProfileDW(date);
        Account accountToWithdraw = database.getAccountByProfileAndType(profile, type);
        if(accountToWithdraw == null){
            addMessageWithdrawView("Account does not exist!");
            return false;
        }
        else {
            accountToWithdraw.withdraw(balance);
            String returned = accountToWithdraw.getHelperMessage();
            addMessageWithdrawView(returned);
        }
        return true;
    }


    /**
     * Adds a message to the withdrawView ListView and scrolls to the last inserted item.
     *
     * @param message The message to be added to the ListView.
     */
    private void addMessageWithdrawView(String message) {
        withdrawView.getItems().add(message);
        withdrawView.scrollTo(withdrawView.getItems().size() - 1);
    }

    /**
     * The ListView for displaying withdrawal-related messages to the user.
     */
    @FXML
    private ListView<String> withdrawView;

    /**
     * The ListView for displaying accounts to the user.
     */
    @FXML
    private ListView<String> accountsListView;

    /**
     * Handles the action to print all accounts. It sorts the accounts in the database,
     * clears the accountsListView, and repopulates it with the sorted accounts.
     *
     * @param event The action event that occurred.
     */
    @FXML
    private void handlePrintAccountsAction(ActionEvent event) {
        database.Sort();
        Account[] allAccounts = database.getAllAccounts();
        accountsListView.getItems().clear();
        if(allAccounts == null || allAccounts.length == 0){
            accountsListView.getItems().add("Account Database is Empty! ");
            return;
        }
        for (Account account : allAccounts) {
            if(account != null) {
                accountsListView.getItems().add(account.toString());
            }
        }
    }

    /**
     * The Button to trigger printing all accounts.
     */
    @FXML
    private Button Print_Account;

    /**
     * The Button to trigger loading accounts from a file.
     */
    @FXML
    private Button Load_File;

    /**
     * The Button to apply interest to all accounts.
     */
    @FXML
    private Button Apply_Interest;

    /**
     * The Button to print all fees associated with the accounts.
     */
    @FXML
    private Button Print_Fees;

    /**
     * Applies interest to all accounts by calling the updateBalances method in the database.
     */
    @FXML
    private void applyInterest(){
        database.updateBalances();
    }

    /**
     * Handles the action to print all fees. It sorts the accounts in the database,
     * clears the accountsListView, and repopulates it with the interest information
     * for each account.
     *
     * @param event The action event that occurred.
     */
    @FXML
    private void handlePrintFeesAction(ActionEvent event) {
        database.Sort();
        accountsListView.getItems().clear();
        List<String> Infolist = database.getInterestInfo();
        if(Infolist == null || Infolist.isEmpty()){
            accountsListView.getItems().add("Account Database is Empty! ");
            return;
        }
        for (String interestInfo: Infolist) {
            if(interestInfo != null) {
                accountsListView.getItems().add(interestInfo);
            }
        }
    }

    /**
     * Handles the action to load accounts from a file. It opens a FileChooser to let the user
     * select a file and then loads the account data from that file into the database.
     *
     * @param event The action event that occurred.
     */
    @FXML
    private void onLoadFileClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Open Account Data File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());

        if (selectedFile != null) {
            loadAccountsFromFile(selectedFile);
        }
    }

    /**
     * Adds a message to the accountsListView and scrolls to the last inserted item.
     *
     * @param message The message to be added to the ListView.
     */
    private void addMessageAccountsView(String message) {
        accountsListView.getItems().add(message);
        accountsListView.scrollTo(accountsListView.getItems().size() - 1);
    }

    /**
     * Loads accounts from the given file. It reads the file line by line, parses each line to create
     * an account object, and adds the account to the database.
     *
     * @param file The file from which to load the account data.
     */
    private void loadAccountsFromFile(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            for (String line : lines) {
                Account account = parseAccountLine(line);
                if (account != null) {
                    database.open(account);
                }
            }
            addMessageAccountsView("Accounts loaded successfully!");
        } catch (IOException e) {
            addMessageAccountsView("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Parses a line from the accounts data file and creates an account object.
     * It adds error messages to the accountsListView if the line cannot be parsed.
     *
     * @param line The line to parse.
     * @return The Account created from the line, or {@code null} if the line could not be parsed into an account.
     */
    private Account parseAccountLine(String line) {
        String[] tokens = line.split(",");
        if (tokens.length < 5) {
//            addMessageAccountsView("-" + line);
            addMessageAccountsView("Incomplete information to add the account");
            return null;
        }

        String accountType = tokens[0];
        String firstName = tokens[1];
        String lastName = tokens[2];
        Date dob = Date.makeDate(tokens[3]);
        if (dob == null) {
            addMessageAccountsView("Date of birth is not a valid! ");
            return null;
        }
        if (!dob.isValid()) {
            return null;
        }

        double accountBalance;
        try {
            accountBalance = Double.parseDouble(tokens[4]);
        }catch(NumberFormatException e){
            addMessageAccountsView("Invalid number! Please enter a valid number!");
            return null;
        }
        Profile profile = new Profile(firstName, lastName, dob);
        Account newAccount;

        switch (accountType) {
            case "C":
                newAccount = new Checking(profile, accountBalance);
                if (database.contains(newAccount)) {
                    addMessageAccountsView("Account already exists!");
                }
                break;
            case "CC":
                if (tokens.length < 6) {
                    addMessageAccountsView("Not enough data to open an account! ");
                }
                int campus;
                try {
                    campus = Integer.parseInt(tokens[5]);
                } catch (NumberFormatException e) {
                    addMessageAccountsView("Not a valid campus code, it must be an integer");
                    return null;
                }
                if (campus < 0 || campus > 2) {
                    addMessageAccountsView("Not a valid campus code, must be between 0 and 2");
                }
                newAccount = new CollegeChecking(profile, accountBalance, campus);
                if (database.contains(newAccount)) {
                    addMessageAccountsView("Account already exists!");
                }
                break;
            case "S":
                if (tokens.length < 6) {
                    addMessageAccountsView("Not enough data to open an account!");
                }
                boolean isLoyal = tokens[5].equals("1");
                newAccount = new Savings(profile, accountBalance, isLoyal);
                break;
            case "MM":
                newAccount = new MoneyMarket(profile, accountBalance);  // Loyal customer status by default
                break;
            default:
                addMessageAccountsView("Account cannot be created! Wrong Account type");
                return null;
        }

        return newAccount;

    }


    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It sets up the UI components and
     * binds actions to certain UI elements like buttons and toggle groups.
     *
     * @param url Used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Set up a new ToggleGroup for campus radio buttons
        campusToggleGroup = new ToggleGroup();
        Campus_NB.setToggleGroup(campusToggleGroup);
        Campus_NW.setToggleGroup(campusToggleGroup);
        Campus_C.setToggleGroup(campusToggleGroup);

        Apply_Interest.setOnAction(event -> {
            applyInterest();
        });
        //Disable the Loyalty checkbox unless 'OC Savings' is selected
        Loyalty.disableProperty().bind(OC_Savings.selectedProperty().not());

        Load_File.setOnAction(this::onLoadFileClick);


    }

}
