package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

	@FXML Button LoginButtion, ClearButton, CloseButton;
	@FXML TextField IdTextField;
	@FXML PasswordField PwPasswordField;

	@FXML
	private void LoginButtonAction(ActionEvent event) {
		
		if(IdTextField.getText().isEmpty() || PwPasswordField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("경고");
			alert.setTitle("경고창");
			alert.setContentText("아이디 비밀번호 확인");
			alert.show();
		}else {
		
		DBconnect conn = new DBconnect();
		Connection conn2 = conn.getConnection();
		
		String sql = "select admind, adminpw"
				+ " from admin_accountss"
				+ " where admind = ? "
				+ " and adminpw = ? ";
		
		try {
			PreparedStatement ps = conn2.prepareStatement(sql);
			
			ps.setString(1, IdTextField.getText());
			ps.setString(2, PwPasswordField.getText());
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("성공");
				alert.setTitle("로그인 성공 여부");
				alert.setContentText("로그인에 성공하였어요");
				alert.show();
				Stage stage = (Stage)CloseButton.getScene().getWindow();
				stage.close();
				CloseButtonAction(event);
				
				Parent root = FXMLLoader.load(getClass().getResource("admindb.fxml"));
				Stage stage1 = new Stage();
				Scene scene = new Scene(root);
				stage1.setTitle("관리자 화면");
				stage1.setScene(scene);
				stage1.show();
				
				
			}else{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("실패");
				alert.setTitle("로그인 성공 여부");
				alert.setContentText("로그인에 실패하였어요");
				alert.show();
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
	
	@FXML
	private void ClearButttonAction(ActionEvent event) {
		IdTextField.setText("");
		PwPasswordField.setText("");
	}
	
	@FXML
	private void CloseButtonAction(ActionEvent event) {
		Stage stage = (Stage)CloseButton.getScene().getWindow();
		stage.close();
	}
}
