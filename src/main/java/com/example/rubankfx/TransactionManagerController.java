package com.example.rubankfx;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;

public class TransactionManagerController implements Initializable{
    public CheckBox Loyalty;
    /*
        This area contains all the components from the Open/Close page.
         */
    AccountDatabase database = new AccountDatabase();
    @FXML
    private ToggleGroup campusToggleGroup;
    @FXML
    public TextField InitDeposit;
    @FXML
    private TextField OC_First_Name;
    @FXML
    private TextField OC_Last_Name;
    @FXML
    private DatePicker OC_DOB;
    @FXML
    private RadioButton OC_Checking;
    @FXML
    private RadioButton OC_CC;
    @FXML
    private RadioButton OC_Savings;
    @FXML
    private RadioButton OC_MM;

    @FXML
    private RadioButton Campus_NB;
    @FXML
    private RadioButton Campus_NW;
    @FXML
    private RadioButton Campus_C;
    AccountDatabase AccountDatabase = new AccountDatabase();

    @FXML
    private void OChandleRadioButtonAction(ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton) event.getSource();
        if (selectedRadioButton == OC_Checking) {
            OC_CC.setSelected(false);
            OC_Savings.setSelected(false);
            OC_MM.setSelected(false);
        } else if (selectedRadioButton == OC_CC) {
            OC_Checking.setSelected(false);
            OC_Savings.setSelected(false);
            OC_MM.setSelected(false);
        } else if (selectedRadioButton == OC_Savings) {
            OC_Checking.setSelected(false);
            OC_CC.setSelected(false);
            OC_MM.setSelected(false);
        } else if (selectedRadioButton == OC_MM) {
            OC_Checking.setSelected(false);
            OC_CC.setSelected(false);
            OC_Savings.setSelected(false);
        }
        Campus_NB.setDisable(!OC_CC.isSelected());
        Campus_NW.setDisable(!OC_CC.isSelected());
        Campus_C.setDisable(!OC_CC.isSelected());
    }
    @FXML
    void OpenAccount(ActionEvent event){
        try {
            boolean added;
            LocalDate localDate = OC_DOB.getValue();
            if (localDate == null) {
                messageListView.getItems().add("Please enter a valid date! ");
                return;
            }
            Date date = new Date(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
            if (!date.isValid()) {
                messageListView.getItems().add(date.getLastMessage());
                return;
            }
            Profile profile = makeProfile(date);
            double balance;
            try {
                balance = Double.parseDouble(InitDeposit.getText());
                if (balance <= 0) {
                    messageListView.getItems().add("Initial deposit must be greater than zero!");
                    return;
                }
            } catch (NumberFormatException e) {
                messageListView.getItems().add("Please enter a valid number for initial deposit");
                return;
            }
            int decision = OCAcctdecision();
            switch (decision) {
                case 0: // Checking Account
                    Checking checkingAccount = Checking.makeChecking(profile, balance);
                    added = database.open(checkingAccount);
                    if (!added && database.getLastMessage().equals("AE")) {
                        messageListView.getItems().add("Account already exists!");
                    }
                    else {
                        messageListView.getItems().add("Account opened successfully!");
                    }
                    break;
                case 1: //College Checking Account
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
                        added = database.open(collegeChecking);
                        if (!added && database.getLastMessage().equals("AE")) {
                            messageListView.getItems().add("Account already exists!");
                        }
                        else {
                            messageListView.getItems().add("Account opened successfully!");
                        }
                    } catch (IllegalArgumentException e) {
                        showAlert("Error", e.getMessage());
                    }
                    break;
                case 2: // Savings Account
                    boolean loyal;
                    loyal = Loyalty.isSelected();
                    try{
                        Savings Saving = Savings.makeSavings(profile,balance,loyal);
                        added = database.open(Saving);
                        if(!added && database.getLastMessage().equals("AE")){
                            messageListView.getItems().add("Account already exists!");
                        }
                        else {
                            messageListView.getItems().add("Account opened successfully!");
                        }
                    }catch (IllegalArgumentException e){
                        showAlert("Error",e.getMessage());
                    }
                    break;
                case 3: //Money Market
                    if(balance < 2000) {
                        messageListView.getItems().add("Balance cannot be below 2000!");
                        return;
                    }
                    try{
                        MoneyMarket Money = MoneyMarket.makeMoneyMarket(profile, balance);
                        added = database.open(Money);
                        if(!added && database.getLastMessage().equals("AE")){
                            messageListView.getItems().add("Account already exists!");
                        }
                        else {
                            messageListView.getItems().add("Account opened successfully!");
                        }
                    }
                    catch (IllegalArgumentException e){
                        showAlert("Error",e.getMessage());
                    }
                    break;
            }
        }catch(Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

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


    private Profile makeProfile(Date date) {
        String firstName = OC_First_Name.getText();
        String lastName = OC_Last_Name.getText();
        return new Profile(firstName, lastName, date);
    }


    private void showAlert(String error, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addMessageToListView(String message) {
        messageListView.getItems().add(message);
        messageListView.scrollTo(messageListView.getItems().size() - 1);
    }


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
            return null; // or "None" depending on how you want to handle no selection
        }
    }


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


@FXML
    private Button Open;
    @FXML
    private Button Close;
    @FXML
    private Button Clear;

    public void ClearScreen(ActionEvent actionEvent) {
        messageListView.getItems().clear();
    }

    @FXML
    private ListView<String> messageListView;; // Replace with the correct type

    //DEPOSIT AND WITHDRAW STUFF
    @FXML
    private TextField DW_First_Name;
    @FXML
    private TextField DW_Last_Name;
    @FXML
    private DatePicker DW_DOB;
    @FXML
    private RadioButton DW_Checking;
    @FXML
    private RadioButton DW_CC;
    @FXML
    private RadioButton DW_Savings;
    @FXML
    private RadioButton DW_MM;
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
    @FXML
    private TextField textField1; // Replace with the correct type

    @FXML
    private Button Deposit;
    @FXML
    private Button Withdraw;

    public ToggleGroup getCampusToggleGroup() {
        return campusToggleGroup;
    }



    @FXML
    private ListView<String> accountsListView; // Replace with the correct type

    @FXML
    private void handlePrintAccountsAction(ActionEvent event) {
        Account[] allAccounts = database.getAllAccounts();
        accountsListView.getItems().clear();
        if(allAccounts == null){
            accountsListView.getItems().add("Account Database is Empty! ");
            return;
        }
        for (Account account : allAccounts) {
            accountsListView.getItems().add(account.toString());
        }
    }


    @FXML
    private Button Print_Account;
    @FXML
    private Button Load_File;
    @FXML
    private Button Apply_Interest;
    @FXML
    private Button Print_Fees;

    @FXML
    private ListView<String> listView3; // Replace with the correct type


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        campusToggleGroup = new ToggleGroup();
        Campus_NB.setToggleGroup(campusToggleGroup);
        Campus_NW.setToggleGroup(campusToggleGroup);
        Campus_C.setToggleGroup(campusToggleGroup);
    }

}
