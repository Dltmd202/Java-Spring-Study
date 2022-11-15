import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 프록시 패턴: 가상 프록시
 * 이 예제에서 구현한 ImageViewProxy가 정확한 proxy 구현이라 보기 힘듦
 * 하지만 이와 같은 상황에서 사용할 수 있는 것이 가상 프록시임
 */
public class ShowImage extends Application {
	private String imageURL = "";
	private boolean testURL(String imageURL) {
		try {
		    URL url = new URL(imageURL);
		    URLConnection conn = url.openConnection();
		    conn.connect();
		    return true;
		} 
		catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Exception Message");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
		return false;
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		while(true) {
			TextInputDialog getURLDialog = new TextInputDialog();
			getURLDialog.setTitle("이미지 주소 입력창");
			getURLDialog.setHeaderText("웹 이미지 주소를 입력하여 주십시오.");
			Optional<String> userInput = getURLDialog.showAndWait();
			// 주소의 유효성 확인 
			if(userInput.isPresent() && testURL(userInput.get())) {
				imageURL = userInput.get();
				break;
			}
		}
		
		Pane pane = new Pane();
		//ImageView iView = new ImageView(imageURL);
		ImageView iView = new ImageViewProxy(imageURL);
		pane.getChildren().add(iView);
		
		primaryStage.setTitle("Virtual Proxy Pattern App");
		primaryStage.setScene(new Scene(pane));
		primaryStage.show();
		
		PauseTransition pauseTransition = 
			new PauseTransition(Duration.seconds(1d));
		pauseTransition.setOnFinished(e->{
			primaryStage.setHeight(iView.getImage().getHeight());
			primaryStage.setWidth(iView.getImage().getWidth());
			primaryStage.setX(0);
			primaryStage.setY(0);
		});
		pauseTransition.play();
	

	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
