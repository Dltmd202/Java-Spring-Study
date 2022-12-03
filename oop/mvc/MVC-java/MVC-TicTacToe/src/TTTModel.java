/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기 
 * @author 김상진
 * MVC 패턴
 * TTTModel.java
 * TicTacToe 게임에서 모델 역할
 *  1) 승자 존재 여부 검사
 *  2) 사용자 조작 결과를 받아 보드 정보 갱신
 */
public class TTTModel {
	public enum CellState {CIRCLE, CROSS, EMPTY};
	private boolean player1 = true;
	private boolean gameFinished = false;
	private CellState[][] board = new CellState[3][3];
	private int count = 0;
	public TTTModel() {
		clear();
	}
	private boolean validIndex(int i) {
		return (i>=0&&i<3);
	}
	public void clear() {
		player1 = true;
		gameFinished = false;
		count = 0;
		for(int r=0; r<3; r++)
			for(int c=0; c<3; c++) 
				board[r][c] = CellState.EMPTY;
	}
	public boolean hasWinner() {
		gameFinished = true;
		// row test
		for(int r=0; r<3; r++) {
			CellState test = board[r][0];
			if(test!=CellState.EMPTY&&test==board[r][1]&&test==board[r][2]) return true;
		}
		// col test
		for(int c=0; c<3; c++) {
			CellState test = board[0][c];
			if(test!=CellState.EMPTY&&test==board[1][c]&&test==board[2][c]) return true;
		}
		// diagonal test
		CellState test = board[0][0];
		if(test!=CellState.EMPTY&&test==board[1][1]&&test==board[2][2]) return true;
		test = board[0][2];
		if(test!=CellState.EMPTY&&test==board[1][1]&&test==board[2][0]) return true;
		if(count<9) gameFinished = false;
		return false;
	}
	public boolean isEmpty(int r, int c) {
		assert (validIndex(r)&&validIndex(c));
		return board[r][c]==CellState.EMPTY;
	}
	public boolean hasGameFinished() {
		return gameFinished;
	}
	public boolean setCell(int r, int c) {
		assert (validIndex(r)&&validIndex(c));
		++count;
		board[r][c] = player1? CellState.CIRCLE: CellState.CROSS;
		player1 = !player1;
		return !player1;
	}
}
