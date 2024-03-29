package lk.ijse.Controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import lk.ijse.Model.EmployeeModel;
import lk.ijse.dto.EmployeeDto;
import org.controlsfx.control.Notifications;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class UpdateEmployeeController {
    public AnchorPane root;
    public JFXTextField txtEmployee_id;
    public JFXTextField txtName;
    public JFXTextField txtContact;
    public JFXTextField txtNic;
    public JFXTextField txtJob;
    public JFXComboBox<String> comboEmployee_id;

    private EmployeeModel employeeModel = new EmployeeModel();

    public void initialize() {
        setValue();
    }

    private void setValue() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<EmployeeDto> idList = employeeModel.getAllEmployee();

            for ( EmployeeDto dto : idList) {
                obList.add(dto.getId());
            }
            comboEmployee_id.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String id = comboEmployee_id.getValue();
        String name = txtName.getText();
        String contact = txtContact.getText();
        String nic = txtNic.getText();
        String job = txtJob.getText();


        try{
            if (!validateEmployee()){
                return;
            }
            var dto = new EmployeeDto(id,name,contact,nic,job);
            boolean isUpdate = employeeModel.updateEmployee(dto);

            if (isUpdate){
                new Alert(Alert.AlertType.CONFIRMATION,"Employee is updated").show();
                clearFields();
            }
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private boolean validateEmployee() {
        boolean isValidate = true;
        boolean name = Pattern.matches("[A-Za-z]{5,}", txtName.getText());
        if (!name){
            showErrorNotification("Invalid Customer Name", "The customer name you entered is invalid");
            isValidate = false;
        }
        boolean con = Pattern.matches("[0-9]{10,}",txtContact.getText());
        if (!con){
            showErrorNotification("Invalid Contact Number", "The contact number you entered is invalid");
            isValidate = false;
        }
        boolean NIC = Pattern.matches("^([0-9]{9}|[0-9]{12})$",txtNic.getText());
        if (!NIC){
            showErrorNotification("Invalid NIC", "The NUC Number you entered is invalid");
            isValidate = false;

        }
        boolean Job = Pattern.matches("[A-Za-z]{5,}",txtJob.getText());
        if (!Job){
            showErrorNotification("Invalid job type", "The job type you entered is invalid");
            isValidate = false;
        }
        return isValidate;
    }

    private void showErrorNotification(String title, String text) {
        Notifications.create()
                .title(title)
                .text(text)
                .showError();
    }

    private void clearFields() {
        comboEmployee_id.setValue("");
        txtName.setText("");
        txtContact.setText("");
        txtNic.setText("");
        txtJob.setText("");
    }


    public void comboEmployee_idOnAction(ActionEvent actionEvent) {
        String id = comboEmployee_id.getValue();

        try{
            EmployeeDto dto = employeeModel.searchEmployee(id);
            txtName.setText(dto.getName());
            txtContact.setText(dto.getContact());
            txtNic.setText(dto.getNic());
            txtJob.setText(dto.getJob());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
