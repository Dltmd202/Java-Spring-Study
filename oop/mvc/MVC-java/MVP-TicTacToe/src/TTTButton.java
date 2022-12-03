import javafx.scene.control.Button;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기 
 * @author 김상진
 * MVP 패턴
 * TTTButton.java
 * TicTacToe 게임 뷰에서 사용할 버튼을 위한 클래스
 * 버튼의 위치(행, 열)를 기억함
 */
public class TTTButton extends Button {
	private int r;
	private int c;
	public TTTButton() {}
	public TTTButton(String msg) { super(msg); }
	public int getRow() { return r; }
	public int getCol() { return c; }
	public void setRowCol(int r, int c) {
		this.r = r;
		this.c = c;
	}
}
