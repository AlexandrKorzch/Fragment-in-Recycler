package microsoft.aspnet.signalr.fragmentinrecyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;
import java.util.Random;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import microsoft.aspnet.signalr.fragmentinrecyclerview.adapter.Adapter;
import microsoft.aspnet.signalr.fragmentinrecyclerview.data.DbManager;
import microsoft.aspnet.signalr.fragmentinrecyclerview.model.DataModel;

public class MainActivity extends AppCompatActivity {


    private int mInstrumentsCount = 2;
    private int mDelayMs = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createInstruments();
        getAndShowInstruments();
        startUpdateInstruments();
    }


    private void createInstruments() {
        for (int i = 0; i < mInstrumentsCount; i++) {
            DbManager.setInstrumentData("instrument_" + i, "instrument_" + i);
        }
    }

    private void getAndShowInstruments() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DbManager.getInstrumentsAsync().subscribe(new Consumer<List<DataModel>>() {
                    @Override
                    public void accept(@NonNull List<DataModel> dataModels) throws Exception {
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                        recyclerView.setLayoutManager(layoutManager);
                        final Adapter adapter = new Adapter(dataModels, getFragmentManager());
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }, 1000);
    }

    private void startUpdateInstruments() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Random rand = new Random();
                try {
                    Thread.sleep(2000);
                    while (true) {
                        try {
                            Thread.sleep(mDelayMs);
                            for (int i = 0; i < mInstrumentsCount; i++) {
                                DbManager.setInstrumentData("instrument_" + i, rand.nextInt()+"");
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
