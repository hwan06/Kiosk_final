package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;


public class AdminloginController {
	
	@FXML
	private Button ClearButton, CloseButton, LoginButton;
	@FXML
	private TextField IdTextField;
	@FXML
	private PasswordField PwPasswordField;
	
	@FXML
	private void ClearButtonAction(ActionEvent event) {
		IdTextField.setText("");
		PwPasswordField.setText("");
		
	}
	
	
	@FXML
	private void CloseButtonAction(ActionEvent event) {
		Stage stage = (Stage)CloseButton.getScene().getWindow();
		stage.close();
		
	}
	
	
	
	@FXML
	private void LoginButtonAction(ActionEvent event) {
		
		if(IdTextField.getText().isEmpty() || PwPasswordField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("모두입력해");
			alert.show();
		}else {
			DBconnect DBconn = new DBconnect();
			Connection conn = DBconn.getConnection();
			
			String sql = "select ADMIN_ID, ADMIN_PW"
					+ " from admin_accounts"
					+ " where ADMIN_ID = ? and ADMIN_PW = ?";
			
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, IdTextField.getText());
				ps.setString(2, PwPasswordField.getText());
				ResultSet rs = ps.executeQuery();
				
				if(rs.next()) {
					try {
						Parent root = FXMLLoader.load(getClass().getResource("Admindb.fxml"));
						Scene scene = new Scene(root);
						Stage stage = new Stage();
						stage.setScene(scene);
						stage.show();
						CloseButtonAction(event);
					} catch(Exception e) {
						e.printStackTrace();
					}
			    
				}else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("틀렸습니다");
					alert.show();
				}
								}
				
			 catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	}
}
