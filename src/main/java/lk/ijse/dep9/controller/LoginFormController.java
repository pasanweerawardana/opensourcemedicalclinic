package lk.ijse.dep9.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginFormController {
    public TextField txtUser;
    public PasswordField txtPassword;
    public Button btnLogin;

    public void initialize(){
        btnLogin.setDefaultButton(true);
    }

    public void btnLoginOnAction(ActionEvent actionEvent) throws ClassNotFoundException, IOException {
        String username=txtUser.getText();
        String password=txtPassword.getText();


        if (username.isBlank()){
            new Alert(Alert.AlertType.ERROR,"Username cant be empty").show();
            txtUser.requestFocus();
            txtUser.selectAll();
            return;
        } else if (password.isBlank()) {
            new Alert(Alert.AlertType.ERROR,"password cant be empty").show();
            txtPassword.requestFocus();
            txtPassword.selectAll();
            return;
        } else if (username.matches("[A-Za-z0-9]+$")) {
            new Alert(Alert.AlertType.ERROR,"Invalid logincedentials");
            txtUser.requestFocus();
            txtUser.selectAll();
            return;
        }
        Class.forName("com.mysql.cj.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/medical_clinic", "root", "Nokiac5@03")) {
//            String sql="SELECT role FROM User WHERE username='%s' AND password='%s'";
//            sql=String.format(sql,username,password);
//
//            Statement stm = connection.createStatement();
//            ResultSet rst = stm.executeQuery(sql);

            String sql="SELECT role FROM User WHERE username=? AND password=?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1,username);
            stm.setString(2,password);
            ResultSet rst = stm.executeQuery();

            if (rst.next()){
                String role= rst.getString("role");
                Scene scene=null;
                switch (role){
                    case "Admin":
                        scene=new Scene(FXMLLoader.load(this.getClass().getResource("/view/AdminDashboard.fxml")));
                        break;
                    case "Doctor":
                        scene=new Scene(FXMLLoader.load(this.getClass().getResource("/view/DoctorDashboardForm.fxml")));
                        break;
                    default:
                        scene=new Scene(FXMLLoader.load(this.getClass().getResource("/view/ReceptionistDashboard.fxml")));

                }
                Stage stage=new Stage();
                stage.setTitle("medical clinic");
                stage.setScene(scene);
                stage.show();
                stage.centerOnScreen();
            }else {
                new Alert(Alert.AlertType.ERROR,"Invalid login").show();
                txtUser.requestFocus();
                txtUser.selectAll();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
