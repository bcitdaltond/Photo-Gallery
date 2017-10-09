package bcitdaltond.application.mydbexample;

/**
 * Created by runej on 2017-09-25.
 */

public class DataStorageImp implements IDataStore{

    private String state = null;

    @Override
    public void saveState(String content) {
        this.state = content;
    }

    @Override
    public String getState() {
        return state;
    }
}
