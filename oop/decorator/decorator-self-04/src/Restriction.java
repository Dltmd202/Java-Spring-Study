import java.util.HashSet;
import java.util.Set;

public class Restriction {
    private int maxAddition = 0;
    private Set<String> exclusionList = new HashSet<>();

    public Restriction(int maxAddition) {
        this.maxAddition = maxAddition;
        this.exclusionList = new HashSet<>();
    }

    public int getMaxAddition() {
        return maxAddition;
    }

    public Set<String> getExclusionList() {
        return exclusionList;
    }
}
