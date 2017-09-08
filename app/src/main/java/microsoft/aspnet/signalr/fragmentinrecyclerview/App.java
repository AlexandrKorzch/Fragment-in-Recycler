package microsoft.aspnet.signalr.fragmentinrecyclerview;

import android.app.Application;

import microsoft.aspnet.signalr.fragmentinrecyclerview.data.DbManager;


/**
 * Created by akorzh on 08.09.2017.
 */

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        DbManager.init(this);
    }
}

