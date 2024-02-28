package lk.ijse.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.Model.UserModel;
import lk.ijse.dto.UserDto;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class RegisterController {
    public JFXTextField txtUsername;
    public JFXTextField txtPassword;
    public JFXButton btnSignup;
    public Label lblTime;

    public AnchorPane root;
    public JFXTextField txtEmail;

    private UserModel userModel = new UserModel();

    private void updateTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = timeFormat.format(new Date());
        lblTime.setText(formattedTime);

         /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
         String formattedDate = dateFormat.format(new Date());
         lblDate.setText(formattedDate);*/
    }

    public void initialize() {
        updateTime();

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(1),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        updateTime();
                    }
                }
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnSignupOnAction(new ActionEvent());
            }
        });
    }
    public void btnSignupOnAction(ActionEvent actionEvent) {
        String username = txtUsername.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        try {
            if (!validateUsername()){
                return;
            }
            var dto = new UserDto(username,email,password);
            boolean isReg = userModel.checkRegister(dto);
            if (isReg){
                new Alert(Alert.AlertType.CONFIRMATION,"Registered Successfully").show();
                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));
                Stage stage = (Stage) root.getScene().getWindow();

                stage.setScene(new Scene(anchorPane));
                stage.centerOnScreen();
            }
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validateUsername() {
        boolean isValidate = true;
        boolean username = Pattern.matches("[A-Za-z]{1,}", txtUsername.getText());
        if (!username){
            showErrorNotification("Invalid username", "The username you entered is invalid");
            isValidate = false;
        }
        boolean password = Pattern.matches("[0-9]{5,}", txtPassword.getText());
        if (!password){
            showErrorNotification("Invalid password", "The password you entered is invalid");
            isValidate = false;
        }
        boolean email = Pattern.matches("^(.+)@(.+)$",txtEmail.getText());
        if (!email){
            showErrorNotification("Invalid email","The email you entered is invalid");
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

    public void hyperLinkOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.centerOnScreen();
    }
}
