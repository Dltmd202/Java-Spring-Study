import javax.swing.text.html.ImageView;
import java.awt.*;

public class ImageViewProxy extends ImageView {

    private Image actualImage = null;
    /**
     * Creates a new view that represents an IMG element.
     *
     * @param elem the element to create a view for
     */
    public ImageViewProxy(String urlString) {
        Thread retrievalThread = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        })
        super(elem);
    }
}
