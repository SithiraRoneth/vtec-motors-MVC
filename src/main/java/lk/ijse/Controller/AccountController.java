package lk.ijse.Controller;


import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.Model.UserModel;
import lk.ijse.dto.UserDto;
import lk.ijse.dto.tm.UserTm;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AccountController {

    public AnchorPane root;
    public TableView tblAccount;
    public TableColumn colUser;
    public TableColumn colPassword;
    public TableColumn colEmail;
    public JFXTextField txtusername;
    public JFXTextField txtPassword;
    public JFXTextField txtEmail;
    public TableColumn colDelete;
    public Label lblUsername;

    private UserModel userModel = new UserModel();
    private ObservableList<UserTm>obList = FXCollections.observableArrayList();

    public void initialize() {
        setCellValueFactory();
        loadAllUser();
        tableListener();
    }

    private void tableListener() {
        tblAccount.getSelectionModel().selectedItemProperty().addListener((observable, oldValued, newValue) -> {
            setData((UserTm) newValue);
        });
    }

    private void setData(UserTm row) {
        txtusername.setText(row.getUsername());
        txtPassword.setText(row.getPassword());
        txtEmail.setText(row.getEmail());
        lblUsername.setText(row.getUsername());
    }

    private void loadAllUser() {
        var model = new UserModel();

        ObservableList<UserTm> obList = FXCollections.observableArrayList();

        try {
            List<UserDto> dtoList = UserModel.getAllUser();

            for (UserDto dto : dtoList) {
                Button deleteBtn = new Button("Delete");

                setDeleteButtonOnAction(deleteBtn,dto.getPassword());
                obList.add(
                        new UserTm(
                                dto.getUser_name(),
                                dto.getPassword(),
                                dto.getEmail(),
                                deleteBtn
                        )
                );
            }

            tblAccount.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDeleteButtonOnAction(Button deleteBtn , String user) {
        deleteBtn.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove User ?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                obList.removeIf(userTm -> userTm.getUsername().equals(user));
                tblAccount.refresh();

                DeleteUser(user);
                loadAllUser();
            }
        });
    }

    private void DeleteUser(String id) {
        try {
            boolean isDeleted = UserModel.deleteUser(id);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "User deleted!").show();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "User not deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        tblAccount.refresh();
    }
    private void setCellValueFactory() {
        colUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
    }

    public void btnCloseOnAction(ActionEvent mouseEvent) {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Are you sure you want to exit ?", yes, no);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yes) {
            // Close the application
            Platform.exit();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String username = txtusername.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        var dto = new UserDto(username,email,password);

        try{
            boolean isUpdated = userModel.updateUser(dto);
            if (isUpdated){
                new Alert(Alert.AlertType.CONFIRMATION,"User updated").show();
            }
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }
}
