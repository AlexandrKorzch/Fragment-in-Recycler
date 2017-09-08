package microsoft.aspnet.signalr.fragmentinrecyclerview.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by akorzh on 08.09.2017.
 */

public class DataModel extends RealmObject {

    @PrimaryKey
    private String mInstrumentName;
    private String mData;

    public DataModel() {
    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        this.mData = data;
    }

    public String getInstrumentName() {
        return mInstrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.mInstrumentName = instrumentName;
    }
}
