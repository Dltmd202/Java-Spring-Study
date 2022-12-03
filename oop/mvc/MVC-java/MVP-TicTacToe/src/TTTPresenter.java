/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기 
 * @author 김상진
 * MVP 패턴
 * TTTPresenter.java
 * TicTacToe 게임에서 컨트롤러 역할: 모델과 뷰 유지
 *  1) JavaFX와 관련된 요소가 전혀 없음
 *  2) 게임 로직은 여전히 구현되어 있음???
 */
public class TTTPresenter {
	private TTTModel theModel;
	private TTTView theView;
	public TTTPresenter(TTTModel theModel, TTTView theView) {	
		this.theModel = theModel;
		this.theView = theView;
		theView.setPresenter(this);
	}
	public void newGameButtonClicked() {
		theModel.clear();
	}
	public void boardButtonClicked(int row, int col) {
		if(theModel.hasGameFinished()) return;
		if(!theModel.isEmpty(row, col)) return;
		boolean player1 = theModel.setCell(row, col);
		theView.update(row,col,player1);
		if(theModel.hasWinner()) {
			theView.setInformation(player1? "사용자 1 승": "사용자 2 승");
		}
		else if(theModel.hasGameFinished()) {
			theView.setInformation("무승부");
		}
	}	
}
