package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
								// 버튼을 누르기 전에 먼저 실행
public class admindbController implements Initializable{

	
	int mcount1 = 0;
	int mcount2 = 0;
	int mcount3 = 0;
	int msum = 0;
	
	@FXML Button searchButton, datesearchButton, datesearch2Button;
	@FXML DatePicker dateDatePicker;
	@FXML TextArea resultTextArea;
	@FXML DatePicker startDatePicker, endDatePicker;
	@FXML TableView<Orderlist> orderlistTableView;
	@FXML PieChart rsPieChart;
	@FXML Button countButton, sumButton;
//	 TableColumn<S> S == 클래스파일명
//	새로운 클래스 만들기 생성자, 게터, 세터 만들기
//	<s> 각칼럼과 데이터형식이 일치하는 자료구조를 가진 클래스 파일명
		@FXML TableColumn<Orderlist, String> idxTableColumn;
		@FXML TableColumn<Orderlist, String> dateTableColumn;
		@FXML TableColumn<Orderlist, String> count1TableColumn;
		@FXML TableColumn<Orderlist, String> count2TableColumn;
		@FXML TableColumn<Orderlist, String> count3TableColumn;
		@FXML TableColumn<Orderlist, String> sumTableColumn;	
//		TableColumn<S,T> S == 클래스 파일명 T == 자료형
		
		
		@Override
//		주문리스트 초기화
		public void initialize(URL arg0, ResourceBundle arg1) {
			
			idxTableColumn.setCellValueFactory(new PropertyValueFactory<>("idx"));
			dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
			count1TableColumn.setCellValueFactory(new PropertyValueFactory<>("count1"));
			count2TableColumn.setCellValueFactory(new PropertyValueFactory<>("count2"));
			count3TableColumn.setCellValueFactory(new PropertyValueFactory<>("count3"));
			sumTableColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
		}
		@FXML
		private void searchButtonAction(ActionEvent e) {
			
			// sql문을 이용하여 데이터 검색
			// 쿼리 실행
			
			// db접속 
			DBconnect conn = new DBconnect();
			Connection conn2 = conn.getConnection();
			
			// sql문
			String sql = "select idx, to_char(order_time,'yyyy-mm-dd hh24:mi:ss'), count1, count2, count3, sum"
					+ " from orderlist_accounts "
					+ " order by idx";
			
			try {
				// sql 실행문을 ps에 저장
				PreparedStatement ps = conn2.prepareStatement(sql);
				// db 실행문을 rs에 저장
				ResultSet rs = ps.executeQuery();
				
				// arraylist 생성
				ObservableList<Orderlist> datalist = FXCollections.observableArrayList();
				mcount1 = 0;
				mcount2 = 0;
				mcount3 = 0;
				msum = 0;
				resultTextArea.setText("");
				// rs에 값이 있을때 동안 datalist에 순서대로 값 추가하기
				
				while(rs.next()) {
					datalist.add(
							new Orderlist(rs.getString(1), 
										rs.getString(2), 
										rs.getString(3),
										rs.getString(4),
										rs.getString(5), 
										rs.getString(6)
										)
							);
					
					mcount1 += Integer.parseInt(rs.getString(3));
					mcount2 += Integer.parseInt(rs.getString(4));
					mcount3 += Integer.parseInt(rs.getString(5));
					msum += Integer.parseInt(rs.getString(6));
					
					
		
				}
				// 데이터리스트에 표시
				orderlistTableView.setItems(datalist);
				resultTextArea.appendText("아메리카노: " + mcount1 + "잔\n");
				resultTextArea.appendText("카푸치노: " + mcount2 + "잔\n");
				resultTextArea.appendText("카페라떼: " + mcount3 + "잔\n");
				resultTextArea.appendText("총합계: " + msum + "원\n");
				
				ps.close();
				rs.close();
				conn2.close();
				
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
		}
		
		@FXML
		private void datesearchButtonAction(ActionEvent e) {
			//만약 날짜가 비어있으면 ==> 경고메시지
			//그 외에는 DB 접속
			
			if(dateDatePicker.getValue() == null) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("알리무 메시지");
				alert.setContentText("날짜를 선택하세요");
				alert.show();
			}else {
				mcount1 = 0;
				mcount2 = 0;
				mcount3 = 0;
				msum = 0;
				resultTextArea.setText("");
				
				DBconnect conn = new DBconnect();
				Connection conn2 = conn.getConnection();
				
				// 데이트피커에 있는 날짜에 해당하는 데이터를 검색
				String sql = "select idx, to_char(order_time,'yyyy-mm-dd hh24:mi:ss'), count1, count2, count3, sum"
							+" from orderlist_accounts"
							+" where to_char(order_time, 'yyyy-mm-dd') = ?";
				
				try {
					PreparedStatement ps = conn2.prepareStatement(sql);
					ps.setString(1, (dateDatePicker.getValue().toString()));
					ResultSet rs = ps.executeQuery();
					
					ObservableList<Orderlist> datalist = FXCollections.observableArrayList();
					
					while(rs.next()) {
						datalist.add(
								new Orderlist(rs.getString(1), 
											rs.getString(2), 
											rs.getString(3),
											rs.getString(4),
											rs.getString(5), 
											rs.getString(6)
											)
								);
						
						mcount1 += Integer.parseInt(rs.getString(3));
						mcount2 += Integer.parseInt(rs.getString(4));
						mcount3 += Integer.parseInt(rs.getString(5));
						msum += Integer.parseInt(rs.getString(6));
						
					}
					orderlistTableView.setItems(datalist);
					
					resultTextArea.appendText("아메리카노: " + mcount1 + "잔\n");
					resultTextArea.appendText("카푸치노: " + mcount2 + "잔\n");
					resultTextArea.appendText("카페라떼: " + mcount3 + "잔\n");
					resultTextArea.appendText("총합계: " + msum + "원\n");
					
					conn2.close();
					ps.close();
					rs.close();
					
				} catch (SQLException e1) {
					
					e1.printStackTrace();
				}
				
			}
			
		}
		// 기간별 조회
		@FXML
		private void datesearch2ButtonAction(ActionEvent e) {
			// 변수와 textarea 초기화.
			mcount1 = 0;
			mcount2 = 0;
			mcount3 = 0;
			msum = 0;
			resultTextArea.setText("");
			
			// 시작 날짜 또는 마지막 날짜를 입력 안했다면 ==> 경고 메시지
			if(startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("시작 날짜와 마지막 날짜 모두 입력해주세요");
				alert.show();
				
			// 그 외에는 DB 접속 ==> 해당 기간에 일치하는 데이터 검색 sql 작성
			}else {
				DBconnect conn = new DBconnect();
				Connection conn2 = conn.getConnection();
				
				// 쿼리 세팅
				String sql = "select idx, to_char(order_time, 'yyyy-mm-dd hh24:mi:ss'), count1, count2, count3, sum"
						+ " from orderlist_accounts"
						+ " where order_time >= ? and order_time <= ?"
//						+ " where order_time between ? and ? "
						+ " order by idx";
				
				try {
					PreparedStatement ps = conn2.prepareStatement(sql);
					ps.setString(1, startDatePicker.getValue().toString());
					ps.setString(2, endDatePicker.getValue().plusDays(1).toString());
					
					// 실행 값 rs에 저장
					ResultSet rs = ps.executeQuery();
					
					// arraylist 생성
					ObservableList<Orderlist> datalist = FXCollections.observableArrayList();
					while(rs.next()) { // while 문으로 rs에 값이 있을동안 arraylist에 값을 추가(add)
						datalist.add(
								new Orderlist(
										rs.getString(1),
										rs.getString(2),
										rs.getString(3),
										rs.getString(4),
										rs.getString(5),
										rs.getString(6)
										)
								);
						// 통계에 들어갈 변수값
						mcount1 += Integer.parseInt(rs.getString(3));
						mcount2 += Integer.parseInt(rs.getString(4));
						mcount3 += Integer.parseInt(rs.getString(5));
						msum += Integer.parseInt(rs.getString(6));
					}
					// 테이블 뷰에 표시(setItems)
					orderlistTableView.setItems(datalist);
					
					// 통계도 표시(append)
					resultTextArea.appendText("아메리카노: " + mcount1 + "잔\n");
					resultTextArea.appendText("카푸치노: " + mcount2 + "잔\n");
					resultTextArea.appendText("카페라떼: " + mcount3 + "잔\n");
					resultTextArea.appendText("총합계: " + msum + "원");
					
					rs.close();
					ps.close();
					conn2.close();
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
				
		}
		
		@FXML
		private void countButtonAction(ActionEvent e) {
			rsPieChart.setTitle("메뉴별 판매 수량 그래푸"); // like 할무니
			rsPieChart.setData(FXCollections.observableArrayList(
					new PieChart.Data("아메리카노" + mcount1 + "잔\n", mcount1),
					new PieChart.Data("카페라떼" + mcount2 + "잔\n", mcount2),
					new PieChart.Data("카푸치노" + mcount3 + "잔\n", mcount3)
					));
		}
		
		@FXML
		private void sumButtonAction(ActionEvent e) {
			rsPieChart.setData(FXCollections.observableArrayList(
					new PieChart.Data("아메리카노 판매금액" + mcount1 * 1000 + "원\n",mcount1 * 1000),
					new PieChart.Data("카페라떼 판매금액" + mcount2 * 2000 + "원\n" ,mcount2 * 2000),
					new PieChart.Data("카푸치노 판매금액" + mcount3 * 3000 + "원\n" ,mcount3 * 3000)
					));
		}
		
}
