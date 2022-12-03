import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기 
 * @author 김상진
 * MVC 패턴
 * TicTacToe 프로그램
 * 뷰와 모델이 서로 소통하지 않음
 * 뷰와 모델 간 상호작용은 컨트롤러를 통해
 */
public class TicTacToe extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		TTTModel theModel = new TTTModel();
		TTTView theView = new TTTView();
		@SuppressWarnings("unused")
		TTTController theController = new TTTController(theModel, theView);
		primaryStage.setTitle("TicTacToe");
		primaryStage.setScene(new Scene(theView));
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	public static void main(String[] args) {
		Application.launch(args);
	}
}
