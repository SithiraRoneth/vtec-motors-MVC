package lk.ijse.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.Launcher;
import lk.ijse.Model.EmployeeModel;
import lk.ijse.Model.ExpenditureModel;
import lk.ijse.Model.SalaryModel;
import lk.ijse.dto.EmployeeDto;
import lk.ijse.dto.ExpenditureDto;
import lk.ijse.dto.SalaryDto;
import lk.ijse.dto.ServiceDto;
import lk.ijse.dto.tm.SalaryTm;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.awt.*;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;

public class SalaryController {
    public Label lblFinalSalary;
    public Label lblEmpName;
    public TableColumn<?, ?> colId;
    public TableColumn<?, ?> colName;

    @FXML
    private JFXComboBox<String> cmbEmpId;

    @FXML
    private JFXComboBox<Month> cmbMonth;

    @FXML
    private TableColumn<?, ?> colDiscount;

    @FXML
    private TableColumn<?, ?> colFinalSalary;

    @FXML
    private TableColumn<?, ?> colSalary;

    @FXML
    private TableColumn<?, ?> colVat;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<SalaryTm> tblSalary;

    @FXML
    private JFXTextField txtBonus;

    @FXML
    private JFXTextField txtE;

    @FXML
    private JFXTextField txtSalary;
    private String month;
    private SalaryModel salaryModel = new SalaryModel();
    private ExpenditureModel expenditureModel = new ExpenditureModel();

    private ObservableList<SalaryTm> obList = FXCollections.observableArrayList();

    public void initialize() {
        setMonth();
        month = String.valueOf(cmbMonth.getValue());
        setCellValueFactory();
        loadEmployee();
    }

    private void loadEmployee() {
        ObservableList<String>obList = FXCollections.observableArrayList();
        try {
            List<EmployeeDto>dtoList = EmployeeModel.getAllEmployee();
            for (EmployeeDto dto : dtoList){
                obList.add(dto.getId());
            }
            cmbEmpId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("bonus"));
        colVat.setCellValueFactory(new PropertyValueFactory<>("etf"));
        colFinalSalary.setCellValueFactory(new PropertyValueFactory<>("finalSalary"));
    }

    public void setMonth() {
        ObservableList<Month> months = FXCollections.observableArrayList(Month.values());
        cmbMonth.setItems(months);
        cmbMonth.setPromptText("select month");
        tblSalary.refresh();

    }

    public void cmbEmpIdOnAction(ActionEvent actionEvent) {
        String id = cmbEmpId.getValue();

        try {
            EmployeeDto dto = EmployeeModel.searchEmployee(id);
            lblEmpName.setText(dto.getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        String id = cmbEmpId.getValue();
        String name = lblEmpName.getText();
        double salary = Double.parseDouble(txtSalary.getText());
        double bonus = Double.parseDouble(txtBonus.getText());
        double etf = Double.parseDouble(txtE.getText());
        calFinalSalary();

        double finalSalary = Double.parseDouble(lblFinalSalary.getText());
        String month = String.valueOf(cmbMonth.getValue());
        try {
            var dto = new SalaryDto(id, name, salary, bonus, etf, finalSalary, month);
            boolean isSaved = salaryModel.saveSalary(dto);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Salary pay success").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        goExpenditure();
    }

    private void goExpenditure() {
        LocalDate currentDate = LocalDate.now();
        String dese = "Salary Payment";
        double amount = Double.parseDouble(lblFinalSalary.getText());
        int year = currentDate.getYear();
        String month = String.valueOf(cmbMonth.getValue());
        String localDate = currentDate.toString();

        var exDto = new ExpenditureDto(dese, amount, year, month, localDate);

        try {
            boolean isSuccess = expenditureModel.addExpenditure(exDto);
            if (isSuccess) {
                new Alert(Alert.AlertType.CONFIRMATION,"Expenditure added").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        private void calFinalSalary () {
            try {
                double salary = Double.parseDouble(txtSalary.getText());
                double bonus = Double.parseDouble(txtBonus.getText());
                double etf = Double.parseDouble(txtE.getText());

                double finalSalary = (salary + bonus) - etf;
                lblFinalSalary.setText(String.valueOf(finalSalary));
            } catch (NumberFormatException e) {
                System.err.println("Invalid format salary");
                e.printStackTrace();
            }

        }

        public void cmbMonthOnAction (ActionEvent actionEvent) throws SQLException {
            Month selectMonth = cmbMonth.getValue();
            obList.clear();
            if (selectMonth != null) {
                month = selectMonth.name();

            }
            List<SalaryTm> dtoList = salaryModel.searchSalary(month);
            for (SalaryTm dto : dtoList) {
                obList.add(
                        new SalaryTm(
                                dto.getId(),
                                dto.getName(),
                                dto.getSalary(),
                                dto.getBonus(),
                                dto.getEtf(),
                                dto.getFinalSalary()
                        )
                );

            }
            tblSalary.setItems(obList);

        }
    public void btnPrintOnAction(ActionEvent actionEvent) throws JRException {
        SalaryTm newValue = tblSalary.getSelectionModel().getSelectedItem();

        if (newValue != null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", newValue.getId());
            map.put("name", newValue.getName());
            map.put("salary", newValue.getSalary());
            map.put("bonus", newValue.getBonus());
            map.put("etf", newValue.getEtf());
            map.put("finalSalary", newValue.getFinalSalary());

            InputStream resourceAsStream = getClass().getResourceAsStream("");
            JasperDesign load = JRXmlLoader.load(resourceAsStream);
            JasperReport compileReport = JasperCompileManager.compileReport(load);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    compileReport,
                    map,
                    new JREmptyDataSource()
            );
            JasperViewer.viewReport(jasperPrint, false);
        } else {
            new Alert(Alert.AlertType.ERROR, "Please select a salary record to print.").show();
        }
    }
}

