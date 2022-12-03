import javax.swing.text.Element;
import javax.swing.text.html.ImageView;

public class ImageViewProxy extends ImageView {
    /**
     * Creates a new view that represents an IMG element.
     *
     * @param elem the element to create a view for
     */
    public ImageViewProxy(Element elem) {
        super(elem);
    }
}