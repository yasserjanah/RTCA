package Communication;

import java.io.Serializable;
import java.util.List;


// used to update active users on client side
public class Update implements Serializable {

    List<String> activeusers;

    public Update(List<String> activeusers) {
        this.activeusers = activeusers;
    }

    public List<String> getActiveusers() {
        return activeusers;
    }
}
