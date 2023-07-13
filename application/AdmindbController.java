package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdmindbController implements Initializable {

	int mcount1 = 0;
	int mcount2 = 0;
	int mcount3 = 0;
	int rs_sum = 0;

	@FXML
	Button searchButton, datesearchButton, datesearch2Button;

	@FXML
	DatePicker dateDatePicker, startDatePicker, endDatePicker;

	@FXML
	TextArea resultTextArea;

	@FXML
	TableView<Orderlist> orderlistTableView;

	@FXML
	TableColumn<Orderlist, String> idxTableColumn;
	@FXML
	TableColumn<Orderlist, String> dateTableColumn;
	@FXML
	TableColumn<Orderlist, String> count1TableColumn;
	@FXML
	TableColumn<Orderlist, String> count2TableColumn;
	@FXML
	TableColumn<Orderlist, String> count3TableColumn;
	@FXML
	TableColumn<Orderlist, String> sumTableColumn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		idxTableColumn.setCellValueFactory(new PropertyValueFactory<>("idx"));
		dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		count1TableColumn.setCellValueFactory(new PropertyValueFactory<>("count1"));
		count2TableColumn.setCellValueFactory(new PropertyValueFactory<>("count2"));
		count3TableColumn.setCellValueFactory(new PropertyValueFactory<>("count3"));
		sumTableColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));

	}

	// 전체 조회버튼
	@FXML
	private void searchButtonAction(ActionEvent e) {

		DBconnect DBconn = new DBconnect();
		Connection conn = DBconn.getConnection();

		String sql = "select idx, order_time, count1, count2, count3, total" + " from orderlist_accounts"
				+ " order by idx";
		resultTextArea.setText("");
		mcount1 = 0;
		mcount2 = 0;
		mcount3 = 0;
		rs_sum = 0;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			ObservableList<Orderlist> datalist = FXCollections.observableArrayList();

			while (rs.next()) {
				datalist.add(new Orderlist(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6)

				)

				);
				mcount1 += Integer.parseInt(rs.getString(3));
				mcount2 += Integer.parseInt(rs.getString(4));
				mcount3 += Integer.parseInt(rs.getString(5));
				rs_sum += Integer.parseInt(rs.getString(6));

			}
			orderlistTableView.setItems(datalist);
			resultTextArea.appendText("아메리카노: " + Integer.toString(mcount1) + "잔" + "\n");
			resultTextArea.appendText("카페라떼: " + Integer.toString(mcount2) + "잔" + "\n");
			resultTextArea.appendText("카푸치노: " + Integer.toString(mcount3) + "잔" + "\n");
			resultTextArea.appendText("총합: " + Integer.toString(rs_sum) + "원");
			
			conn.close();
			ps.close();
			rs.close();
			
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
			
	}

	// 날짜별 조회버튼
	@FXML
	private void datesearchButtonAction(ActionEvent e) {
			
		
		resultTextArea.setText("");
		mcount1 = 0;
		mcount2 = 0;
		mcount3 = 0;
		rs_sum = 0;
		if(dateDatePicker.getValue() == null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("날짜를 입력해주세요");
			alert.show();
		}else {
			
			DBconnect DBconn = new DBconnect();
			Connection conn = DBconn.getConnection();
			
			String sql = "select idx, to_char(order_time, 'yyyy-mm-dd') , count1, count2, count3, total"
					+ " from orderlist_accounts"
					+ " where to_char(order_time, 'yyyy-mm-dd') = ?";
			
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, dateDatePicker.getValue().toString());
				ResultSet rs = ps.executeQuery();
				
				ObservableList<Orderlist> datalist = FXCollections.observableArrayList();
				while(rs.next()) {
					datalist.add(new Orderlist(
										rs.getString(1),
										rs.getString(2),
										rs.getString(3),
										rs.getString(4),
										rs.getString(5),
										rs.getString(6)
							));
					mcount1 += Integer.parseInt(rs.getString(3));
					mcount2 += Integer.parseInt(rs.getString(4));
					mcount3 += Integer.parseInt(rs.getString(5));
					rs_sum += Integer.parseInt(rs.getString(6));
					
				}
				
				orderlistTableView.setItems(datalist);
				resultTextArea.appendText("아메리카노: " + mcount1 + "잔" + "\n");
				resultTextArea.appendText("카페라떼: " + mcount2 + "잔" + "\n");
				resultTextArea.appendText("카푸치노: " + mcount3 + "잔" + "\n");
				resultTextArea.appendText("총합: " +  + rs_sum + "원");
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
		}

		
	}

	// 기간별 조회버튼
	@FXML
	private void datesearch2ButtonAction(ActionEvent e) {
		// 시작 날짜 또는 종료 날짜가 비어있으면 ==> 경고메시지
		// 그 외에는 ==> DB접속, *sql 작성*, 쿼리 실행
		
		resultTextArea.setText("");
		mcount1 = 0;
		mcount2 = 0;
		mcount3 = 0;
		rs_sum = 0;
		if(startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("시작 날짜 또는 종료 날짜를 확인해주세요.");
		}else {
			
			DBconnect DBconn = new DBconnect();
			Connection conn = DBconn.getConnection();
			
			String sql = "select idx, to_char(order_time, 'yyyy-mm-dd') , count1, count2, count3, total"
					+ " from orderlist_accounts"
					+ " where to_char(order_time, 'yyyy-mm-dd') between ? and ?"
					+ " order by idx";
			
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, startDatePicker.getValue().toString());
				ps.setString(2, endDatePicker.getValue().plusDays(1).toString());
				
				ResultSet rs = ps.executeQuery();
				
				ObservableList<Orderlist> datalist = FXCollections.observableArrayList();
				while(rs.next()) {
					datalist.add(new Orderlist(
									rs.getString(1),
									rs.getString(2),
									rs.getString(3),
									rs.getString(4),
									rs.getString(5),
									rs.getString(6)

							));
					
					mcount1 += Integer.parseInt(rs.getString(3));
					mcount2 += Integer.parseInt(rs.getString(4));
					mcount3 += Integer.parseInt(rs.getString(5));
					rs_sum += Integer.parseInt(rs.getString(6));
				}
				orderlistTableView.setItems(datalist);
				resultTextArea.appendText("아메리카노: " + mcount1 + "잔" + "\n");
				resultTextArea.appendText("카푸치노:"  + mcount2 + "잔" + "\n");
				resultTextArea.appendText("카페라떼: " + mcount3 + "잔" + "\n");
				resultTextArea.appendText("총합: " + rs_sum + "원" + "\n");
				
			} catch (SQLException e1) {
				
				
				e1.printStackTrace();
			}
			
		}
		
	
	}
}
