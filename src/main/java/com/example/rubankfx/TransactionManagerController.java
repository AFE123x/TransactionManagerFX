package com.example.rubankfx;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class TransactionManagerController {
    @FXML
    public TextField InitDeposit;
    //Open/Close variables
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
    private RadioButton Campus_CD;
    @FXML
    private RadioButton Campus_CA;
    @FXML
    private RadioButton Campus_B;
    @FXML
    private RadioButton Campus_L;

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
        Campus_CD.setDisable(!OC_CC.isSelected());
        Campus_CA.setDisable(!OC_CC.isSelected());
        Campus_B.setDisable(!OC_CC.isSelected());
        Campus_L.setDisable(!OC_CC.isSelected());
    }
    @FXML
    void OpenAccount(ActionEvent event){
        System.out.println("Opening Account");
    }
    @FXML
    void CloseAccount(ActionEvent event){
        System.out.println("Closing Account");
    }


    @FXML
    private Button Open;
    @FXML
    private Button Close;
    @FXML
    private Button Clear;

    @FXML
    private ListView<?> listView1; // Replace with the correct type

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

    @FXML
    private ListView<String> listView2; // Replace with the correct type

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



}