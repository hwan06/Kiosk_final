# 성일정보 Kiosk 최종버전
### [완성 화면]
![image](https://github.com/hwan06/Kiosk_final/assets/114748934/b367f22d-c0c5-429a-b7dd-cdf3b1632a57)
---
![image](https://github.com/hwan06/Kiosk_final/assets/114748934/5e677dce-61ed-4c85-9211-e7136f3ba99c)
---
![image](https://github.com/hwan06/Kiosk_final/assets/114748934/1c76f178-98d4-463e-bcc1-8a8e62f7c055)
---
### [변수방]
---
```
private int sum=0;
    private String[] menu_name = {"아메리카노", "카푸치노", "카페라떼"};  
    private int countm[] = new int[3];
```
---
### [+ 버튼을 눌렀을 때 메뉴 추가]
![image](https://github.com/hwan06/Kiosk_final/assets/114748934/b367f22d-c0c5-429a-b7dd-cdf3b1632a57)
#### 메소드를 생성하여 +버튼을 누를때마다 count[n] +1 증가 // n == 메뉴 방 번호
``` java
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
  ```
  ---
### [- 버튼을 눌렀을 때 메뉴 제거]
#### countm[n]의 갯수가 0 이상이라면 -1 감소
``` java
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
```
---

### [취소 버튼을 눌렀을 때 모든 변수방과 사용자에게 보여지는 화면 초기화]
``` java
 @FXML
    public void CancleButtonAction(ActionEvent event) {
    	sumLabel.setText("0");
    	ListTextArea.setText("");
    	for(int i=0;i<3; i++) {
    		countm[i]=0;
    	}	
    }
```
---
### [계산 버튼을 눌렀을 때 추가한 메뉴의 총합계 화면에 출력]
* sum+""를 해주는 이뉴는 sum은 정수형인데 setText는 문자형을 나타내주는 함수기 때문에     
 ""를 더해줌으로써 sum이 문자형으로 바뀜.    

![image](https://github.com/hwan06/Kiosk_final/assets/114748934/5e2df094-36cb-46f8-9434-609e5fb465e7)

``` java
 @FXML
    public void SumButtonAction(ActionEvent event) {
    	
    	sum = kiosksum.ksum(countm);
    	sumLabel.setText(sum + "");
    }
```
---
### [주문하기 버튼을 눌렀을 때 DB에 연결하여 주문내역을 DB에 저장하기]

#### DB에 접속하기 위한 클래스 생성
``` java
public class DBconnect {
	
	public Connection conn;
	
	public Connection getConnection() {
		
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String id = "cafe3"; // DB 만들때 설정한 id
		String pw = "1234"; // DB 만들때 설정한 pw
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id, pw);
			System.out.println("디비 접속 성공 - 20230516");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("디비 접속 실패");
		}
		
		return conn;
		
	}
```
---
#### 총합계가 0이 아니면 DB접속 클래스를 호출하여 DB접속

``` java
@FXML
    public void OrderButtonAction(ActionEvent event) {
    		//실제 주문 내역 디비에 저장하기
    		//디비 접속해서 작업
    
    	if(sum != 0) { // sum != 0 이면 디비 접속
    		
    		//--			DB접속 			--//
    		DBconnect conn = new DBconnect();
        	Connection conn2 = conn.getConnection();
 ```
 ---
  #### DB에서 실행 할 SQL문 작성하고 ps에 SQL문을 저장
 ``` java
        				//--				sql문              --//
        	String sql = "insert into orderlist_accounts"
        				  + " (idx, order_time, menu1, count1,"
        				  + " menu2, count2, menu3, count3, sum)"
        				  + " values (orderlist_idx_pk.nextVal,"
        				  + " current_timestamp, '아메리카노', ?,"
        				  + " '카푸치노', ?, '카페라떼', ?, ?)";
        	try {
				PreparedStatement ps = conn2.prepareStatement(sql);
 ```
---
#### SQL문의 ? 자리에 각 메뉴와 합계를 넣어준 뒤 rs에 SQL을 실행하여 나온 결과값을 저장 
``` java
				ps.setInt(1, countm[0]); // 1번 ?에 들어갈 값
				ps.setInt(2, countm[1]); // 2번 ?에 들어갈 값
				ps.setInt(3, countm[2]); // 3번 ?에 들어갈 값
				ps.setInt(4, sum); 		 // 4번 ?에 들어갈 값
				ResultSet rs = ps.executeQuery(); // rs에 sql 실행값 저장
```
---

 ### rs에 저장된 값이 있다면 즉, SQL문이 실행이 되었다면 DB에 저장이 된것이기 때문에 "계산성공" 안내창 띄워주기 그리고 모든 변수방 초기화
 ![image](https://github.com/hwan06/Kiosk_final/assets/114748934/d6acae83-caeb-41b6-9996-8a7feb38b7ca)

 ``` java
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
  ```
  ---
 ### rs에 저장된 값이 없다면 SQL 오류 메시지 안내창 띄워주기 (코드상의 문제는 아님)
 ``` java
				}else { // 예상치 못한 하드웨어 오류시 메시지 출력
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("계산에 실패했습니다. 카운터에서 계산해 주세요");
					alert.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
 ```


### [관리자 로그인 버튼을 눌렀을 때 관리자 로그인 화면 활성화]
![image](https://github.com/hwan06/Kiosk_final/assets/114748934/5e677dce-61ed-4c85-9211-e7136f3ba99c)

``` java
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
```
# [로그인 화면에서 로그인하기]
![image](https://github.com/hwan06/Kiosk_final/assets/114748934/5e677dce-61ed-4c85-9211-e7136f3ba99c)
### 아이디 칸 또는 비밀번호 칸이 비어 있으면 경고 메시지 출력
``` java
@FXML
	private void LoginButtonAction(ActionEvent event) {
		
		if(IdTextField.getText().isEmpty() || PwPasswordField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("경고");
			alert.setTitle("경고창");
			alert.setContentText("아이디 비밀번호 확인");
			alert.show();
```
---
### 그 외에는 DB 접속 후 SQL 작성
``` java
else {
		DBconnect conn = new DBconnect();
		Connection conn2 = conn.getConnection();
		
		String sql = "select admind, adminpw"
				+ " from admin_accountss"
				+ " where admind = ? "
				+ " and adminpw = ? ";
```
---
### ps에 SQL문을 저장하고 ? 자리에 ID칸에 있는 데이터와 PW칸에 있는 데이터를 넣어주고 rs에 SQL문 실행 결과값을 저장 
``` java
try {
			PreparedStatement ps = conn2.prepareStatement(sql);
			
			ps.setString(1, IdTextField.getText());
			ps.setString(2, PwPasswordField.getText());
			
			ResultSet rs = ps.executeQuery();
```
---
### rs에 값이 있다면 id와 pw 모두 제대로 입력한 것이므로 로그인 성공 메시지를 띄워주고 관리자 로그인창은 닫기 그런다음 관리자 화면 띄우기 아니면 로그인 실패 메시지 띄우기
``` java
if(rs.next()) {
					try {
						Parent root = FXMLLoader.load(getClass().getResource("Admindb.fxml"));
						Scene scene = new Scene(root);
						Stage stage = new Stage();
						stage.setScene(scene);
						stage.show();
					} catch(Exception e) {
						e.printStackTrace();
					}
			    
				}else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText("틀렸습니다");
					alert.show();
				}
```
---
### 취소 버튼과 닫기 버튼 (취소 : 화면에 있는 모든걸 초기화 || 닫기 : 창 닫기)
``` java
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
```
# [로그인 성공 후]
![image](https://github.com/hwan06/Kiosk_final/assets/114748934/5fa1b20f-7407-46e7-b1ba-5c8355257f14)
### 새로운 Orderlist라는 이름의 클래스를 만들어서 생성자, 게터, 세터 지정 그런다음 각 열 이름 지정
``` java
//	새로운 클래스 만들기 생성자, 게터, 세터 만들기
//	<s> 각칼럼과 데이터형식이 일치하는 자료구조를 가진 클래스 파일명
		@FXML TableColumn<Orderlist, String> idxTableColumn;
		@FXML TableColumn<Orderlist, String> dateTableColumn;
		@FXML TableColumn<Orderlist, String> count1TableColumn;
		@FXML TableColumn<Orderlist, String> count2TableColumn;
		@FXML TableColumn<Orderlist, String> count3TableColumn;
		@FXML TableColumn<Orderlist, String> sumTableColumn;	
```
---

### 이니셜라이즈를 이용하여 코드가 실행되는 순간 바로 실행 DB연동을 통한 테이블의 열에 데이터 삽입
``` java
public void initialize(URL arg0, ResourceBundle arg1) {
			
			idxTableColumn.setCellValueFactory(new PropertyValueFactory<>("idx"));
			dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
			count1TableColumn.setCellValueFactory(new PropertyValueFactory<>("count1"));
			count2TableColumn.setCellValueFactory(new PropertyValueFactory<>("count2"));
			count3TableColumn.setCellValueFactory(new PropertyValueFactory<>("count3"));
			sumTableColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
		}
```
---
### 전체조회 클릭시 DB에 접속하고 SQL문을 실행하여 결과값 rs에 저장하기
``` java
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
```
---
### 값을 누적하여 저장한 뒤 출력 할 arraylist 생성, 최종값 출력을 위한 변수방 선언과 동시에 누를때마다 결과값 초기화
``` java
// arraylist 생성
				ObservableList<Orderlist> datalist = FXCollections.observableArrayList();
				mcount1 = 0;
				mcount2 = 0;
				mcount3 = 0;
				msum = 0;
				resultTextArea.setText("");
```
---
### while문을 이용하여 rs에 값이 있을때 동안 datalist에 값 누적시키기
``` java
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
```
---
### 누적 시킨 값 누적하여 변수방에 저장
``` java
mcount1 += Integer.parseInt(rs.getString(3));
					mcount2 += Integer.parseInt(rs.getString(4));
					mcount3 += Integer.parseInt(rs.getString(5));
					msum += Integer.parseInt(rs.getString(6));
```
---
### datalist와 통계창에 누적시킨 결과값 출력
![image](https://github.com/hwan06/Kiosk_final/assets/114748934/805f32d7-1309-4333-aed0-529f1582df0b)

``` java
orderlistTableView.setItems(datalist); // 데이터 리스트에 표시
				resultTextArea.appendText("아메리카노: " + mcount1 + "잔\n");
				resultTextArea.appendText("카푸치노: " + mcount2 + "잔\n");
				resultTextArea.appendText("카페라떼: " + mcount3 + "잔\n");
				resultTextArea.appendText("총합계: " + msum + "원\n");
```
---
## 날짜 조회와 기간별 조회는 SQL문 말고 다른 코드는 다 동일하므로 SQL문만 기재함.
### [날짜 조회] to_char을 쓴 이유는 DB에 저장되는 값은 몇시 몇분 몇초..... 이므로 일까지만 끊어서 SQL문 작성
``` java
String sql = "select idx, to_char(order_time,'yyyy-mm-dd hh24:mi:ss'), count1, count2, count3, sum"
							+" from orderlist_accounts"
							+" where to_char(order_time, 'yyyy-mm-dd') = ?";
```
---
#### [기간별 조회] 두 가지로 나뉘는 데, between과 부등호를 활용한 것으로 나뉨. 중요한 건 시작일은 상관 없지만 마지막 날짜는 1일을 더해줘야 한다.
#### 왜냐하면 DB에 저장된 값은 6/12 몇시 몇분 몇초 이기 때문에 6/12일 당일은 잡히지 않는다 따라서 1일을 더해줌으로써 12일까지 조회가 가능하게 된다.
``` java
String sql = "select idx, to_char(order_time, 'yyyy-mm-dd hh24:mi:ss'), count1, count2, count3, sum"
						+ " from orderlist_accounts"
						+ " where order_time >= ? and order_time <= ?"
//						+ " where order_time between ? and ? "
						+ " order by idx";
            ps.setString(2, endDatePicker.getValue().plusDays(1).toString()); // 1일 더해주는 코드
```
