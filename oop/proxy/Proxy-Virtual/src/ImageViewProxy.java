import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @copyright 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * @version 2022년도 2학기
 * @author 김상진
 * 프록시 패턴: 가상 프록시
 * 가상 프록시 객체: 실제 이미지가 완전히 로드될 때까지 대체 이미지 제시
 */
public class ImageViewProxy extends ImageView {
	private Image actualImage = null;
	public ImageViewProxy(String urlString) {
		setImage(new Image("progress.gif"));
		Thread retrievalThread = new Thread(new Runnable(){
			public void run(){
				try{
					URL url = new URL(urlString);
					URLConnection con = url.openConnection();
					con.setConnectTimeout(2000);
					con.setReadTimeout(2000);
					try(
						InputStream in = con.getInputStream();
					){
						actualImage = new Image(in);
					}
					catch(Exception e) {
						throw e;
					}
				}
				catch(Exception e){
					actualImage = new Image("sorry.jpg");
				} // 너무 시간이 오래 걸리면 sorry.jpg
				setImage(actualImage);
			}
		});
		retrievalThread.start();
	}
}
