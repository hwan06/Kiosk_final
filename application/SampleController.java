package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;


public class SampleController {

    @FXML private Button CalButton, CancleButton;
    @FXML private Button M1pButton, M2pButton, M3pButton;
    @FXML private Button M1mButton, M2mButton, M3mButton;
    @FXML private TextArea ListTextArea;
    @FXML private Label sumLabel;
    @FXML private Button AdminButton, OrderButton;
    
    private int sum=0;
    private String[] menu_name = {"아메리카노", "카푸치노", "카페라떼"};  
    private int countm[] = new int[3];
    
    Kiosksum kiosksum = new Kiosksum(); // 객체 선언
    
    @FXML
    public void M1pButtonAction(ActionEvent event) {
    	countm[0]=countm[0]+1;
    	menu_append();
    }

	private void menu_append() {		
		ListTextArea.setText("");
		for(int i=0; i<3;i++) {
			ListTextArea.appendText(menu_name[i] + " : " + countm[i] + "잔"+"\n");
		}	// 버튼 눌렀을때 전 메뉴 출력
	}

	@FXML
	public void M2pButtonAction(ActionEvent event) {
    	countm[1]++;    	
    	menu_append();
    }
    
    @FXML
    public void M3pButtonAction(ActionEvent event) {
    	countm[2]++;    	
    	menu_append();
    }
    
    
    @FXML
    public void M1mButtonAction(ActionEvent event) {
    	if(countm[0]>0) countm[0]--;
    	menu_append();
    }

    @FXML
    public void M2mButtonAction(ActionEvent event) {
    	if(countm[1]>0) countm[1]--;
    	menu_append();
    }
    
    @FXML
    public  void M3mButtonAction(ActionEvent event) {
    	if(countm[2]>0) countm[2]--;
    	menu_append();
    }
    
    
    @FXML
    public void CancleButtonAction(ActionEvent event) {
    	sumLabel.setText("0");
    	ListTextArea.setText("");
    	for(int i=0;i<3; i++) {
    		countm[i]=0;
    	}	
    }


    @FXML
    public void SumButtonAction(ActionEvent event) {
    	
    	sum = kiosksum.ksum(countm);
    	sumLabel.setText(sum + "");
    }
    
    
    @FXML
    public void AdminButtonAction(ActionEvent event) {
    	try {
			Parent root = FXMLLoader.load(getClass().getResource("adminlogin.fxml"));
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("관리자 로그인 화면-5월");
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
    
    }
    
    
    @FXML
    public void OrderButtonAction(ActionEvent event) {
    		//실제 주문 내역 디비에 저장하기
    		//디비 접속해서 작업
    
    	if(sum != 0) { // sum != 0 이면 디비 접속
    		
    		//--			DB접속 			--//
    		DBconnect conn = new DBconnect();
        	Connection conn2 = conn.getConnection();
        				//--				sql문              --//
        	String sql = "insert into orderlist_accounts"
        				  + " (idx, order_time, menu1, count1,"
        				  + " menu2, count2, menu3, count3, sum)"
        				  + " values (orderlist_idx_pk.nextVal,"
        				  + " current_timestamp, '아메리카노', ?,"
        				  + " '카푸치노', ?, '카페라떼', ?, ?)";
        	try {
				PreparedStatement ps = conn2.prepareStatement(sql);

				ps.setInt(1, countm[0]); // 1번 ?에 들어갈 값
				ps.setInt(2, countm[1]); // 2번 ?에 들어갈 값
				ps.setInt(3, countm[2]); // 3번 ?에 들어갈 값
				ps.setInt(4, sum); 		 // 4번 ?에 들어갈 값
				ResultSet rs = ps.executeQuery(); // rs에 sql 실행값 저장
				
				if(rs.next()) { // rs에 저장된 값이 있다면 계산성공 메시지 출력
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("계산 성공");
					alert.show();
					sum = 0;
					sumLabel.setText("0");
					ListTextArea.setText("");
					for(int i = 0; i < menu_name.length; i++) {
					countm[i] = 0;
					}
				}else { // 예상치 못한 하드웨어 오류시 메시지 출력
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("계산에 실패했습니다. 카운터에서 계산해 주세요");
					alert.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
        				  
    	}else { // 만약 sum = 0이라면 경고 메세지 
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setContentText("메뉴를 선택하고, 계산 버튼을 클릭 후 눌러주세요");
    		alert.show();
    	}	
    }
}
