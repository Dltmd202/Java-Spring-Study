import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기 
 * @author 김상진
 * MVC 패턴
 * TTTView.java
 * TicTacToe 게임에서 뷰 역할
 */
public class TTTView extends BorderPane {
	private GridPane boardPane = new GridPane();
	private TextField information = new TextField();
	private Button startButton = new Button("새 게임");
	TTTButton[][] cellButtons = new TTTButton[3][3];
	public TTTView() {
		boardPane.setPadding(new Insets(10));
		for(int r=0; r<3; r++)
			for(int c=0; c<3; c++) {
				cellButtons[r][c] = new TTTButton();
				cellButtons[r][c].setMinHeight(100);
				cellButtons[r][c].setMinWidth(100);
				cellButtons[r][c].setRowCol(r,c);
				boardPane.add(cellButtons[r][c],c,r);
			}
		setCenter(boardPane);
		
		HBox controlPane = new HBox();
		controlPane.setPadding(new Insets(10));
		controlPane.setAlignment(Pos.CENTER);
		controlPane.getChildren().addAll(information, startButton);
		information.setMinWidth(200);
		information.setEditable(false);
		startButton.setMinWidth(100);
		setBottom(controlPane);
	}
	public void clear() {
		for(int r=0; r<3; r++)
			for(int c=0; c<3; c++)
				cellButtons[r][c].setGraphic(null);
		information.setText("");
	}
	public void update(int row, int col, boolean player1) {
		assert (row>=0&&col>=0&&row<3&&col<3);
		ImageView image = player1?
			new ImageView(new Image("pikachu.png")):
			new ImageView(new Image("squirtle.png"));
		image.setFitWidth(80);
		image.setPreserveRatio(true);	
		cellButtons[row][col].setGraphic(image);
	}
	public void setButtonHandler(EventHandler<ActionEvent> boardButtonHandler,
			EventHandler<ActionEvent> newGameButtonHandler){
		for(int r=0; r<3; r++)
			for(int c=0; c<3; c++)
				cellButtons[r][c].setOnAction(boardButtonHandler);
		startButton.setOnAction(newGameButtonHandler);
	}
	public void setInformation(String msg) {
		information.setText(msg);
	}
}
