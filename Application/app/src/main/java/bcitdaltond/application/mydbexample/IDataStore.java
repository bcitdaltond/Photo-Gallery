package bcitdaltond.application.mydbexample;

/**
 * Created by runej on 2017-09-25.
 */

public interface IDataStore {
    void saveState(String content);
    String getState();

}
