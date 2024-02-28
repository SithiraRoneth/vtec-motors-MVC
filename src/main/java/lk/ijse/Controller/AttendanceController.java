package lk.ijse.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.Model.AttendanceModel;
import lk.ijse.dto.AttendanceDto;
import lk.ijse.dto.tm.AttendanceTm;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class AttendanceController {
    public DatePicker txtDate;

    public AnchorPane root;
    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colEmployee_id;

    @FXML
    private TableColumn<?, ?> colEmployee_name;

    @FXML
    private TableView<AttendanceTm> tblAttendance;

    private AttendanceModel attendanceModel = new AttendanceModel();

    public void initialize(){
        setCellValueFactory();
      //  loadAllAttendance();
    }

    /*private void loadAllAttendance() {
        var model  = new AttendanceModel();

        ObservableList<AttendanceTm>obList = FXCollections.observableArrayList();

        try{
            List<AttendanceDto>dtoList = model.getAllAttendance();

            for (AttendanceDto dto : dtoList){
                obList.add(
                        new AttendanceTm(
                                dto.getDate(),
                                dto.getEmp_id(),
                                dto.getEmp_name()
                        )
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/
    private void setCellValueFactory(){
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colEmployee_id.setCellValueFactory(new PropertyValueFactory<>("emp_id"));
        colEmployee_name.setCellValueFactory(new PropertyValueFactory<>("emp_name"));
    }


    public void btnUpdateAttendance(ActionEvent actionEvent) throws IOException {

        root.getChildren().clear();
        root.getChildren().add(FXMLLoader.load(getClass().getResource("/view/addattendance_form.fxml")));

    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        root.getChildren().clear();
        root.getChildren().add(FXMLLoader.load(getClass().getResource("/view/employee_form.fxml")));
    }

    public void btnDateOnAction(ActionEvent actionEvent) {
        try {
            // Get the selected date from the DatePicker
            DatePicker selectedDate = txtDate;

            if (selectedDate != null) {
                // Convert LocalDate to java.sql.Date
                Date date = java.sql.Date.valueOf(selectedDate.getValue());

                // Call a method to load attendance data for the selected date
                loadAttendanceForDate(date);
            } else {
                // Handle the case where no date is selected
                // You may show an alert or provide a message to the user
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }

    private void loadAttendanceForDate(Date date) {
        ObservableList<AttendanceTm> obList = FXCollections.observableArrayList();

        try {
            List<AttendanceDto> dtoList = attendanceModel.getAttendanceForDate(date);

            for (AttendanceDto dto : dtoList) {
                obList.add(new AttendanceTm(java.sql.Date.valueOf(dto.getDate()), dto.getEmp_id(),dto.getEmp_name()));
            }

            // Set the data to the TableView
            tblAttendance.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }

}
