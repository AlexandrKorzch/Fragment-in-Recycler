package microsoft.aspnet.signalr.fragmentinrecyclerview.data;

import android.content.Context;
import android.util.Log;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmResults;
import microsoft.aspnet.signalr.fragmentinrecyclerview.model.DataModel;

/**
 * Created by akorzh on 08.09.2017.
 */

public class DbManager{

    private static final String TAG = "LogApp";
    private static Realm sRealm;

    public DbManager() {
    }

    public static DbManager init(Context context) {
        Log.d(TAG, "init: ");
        Realm.init(context);
        DbManager dbManager = new DbManager();
        dbManager.createRealm();
        return dbManager;
    }

    private void createRealm() {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("inMemory")
                .schemaVersion(42)
                .inMemory()
                .build();
        sRealm = Realm.getInstance(config);
    }


    public static void setInstrumentData(final String instrumentName, final String data) {
        Completable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                sRealm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        DataModel dataModel = realm.where(DataModel.class).contains("mInstrumentName", instrumentName).findFirst();
                        if (dataModel == null) {
                            dataModel = new DataModel();
                            dataModel.setInstrumentName(instrumentName);
                        }
                        dataModel.setData(data);
                        RealmModel savesModel = realm.copyToRealmOrUpdate(dataModel);
                        Log.d(TAG, "execute: savesModel - "+savesModel);
                    }
                });
                return null;
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }



    public static Flowable<List<DataModel>> getInstrumentsAsync() {
        return Flowable.fromPublisher(new Publisher<List<DataModel>>() {
            @Override
            public void subscribe(final Subscriber<? super List<DataModel>> s) {
                sRealm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults realmResults = realm.where(DataModel.class).findAll();
                        List<DataModel> dataModels = realm.copyFromRealm(realmResults);
                        Single.just(dataModels)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<List<DataModel>>() {
                                    @Override
                                    public void accept(@NonNull List<DataModel> dataModels) throws Exception {
                                        s.onNext(dataModels);
                                    }
                                });
                    }
                });
            }
        });
    }


    public static Flowable<String> getPrice(final String instrumentName){
        return Flowable.fromPublisher(new Publisher<String>() {
            @Override
            public void subscribe(final Subscriber<? super String> s) {
                DataModel mInstrumentName = sRealm.where(DataModel.class).contains("mInstrumentName", instrumentName).findFirst();
                mInstrumentName.addChangeListener(new RealmChangeListener() {
                    @Override
                    public void onChange(Object o) {
                        Log.d(TAG, "onChange: - "+o);
                        s.onNext(((DataModel)o).getData());
                    }
                });
            }
        });
    }

}






//    public Flowable<List<DataModel>> subscribeToDataList(){
//
//        return Flowable.fromPublisher(new Publisher<List<DataModel>>() {
//            @Override
//            public void subscribe(final Subscriber<? super List<DataModel>> s) {
//                final RealmResults[] realmResults = new RealmResults[1];
//                sRealm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        realmResults[0] = realm.where(DataModel.class).findAll();
//                    }
//                });
//
//                realmResults[0].addChangeListener(new RealmChangeListener<RealmResults>() {
//                    @Override
//                    public void onChange(RealmResults realmResults) {
//                        Log.d(TAG, "onChange: realmResults - "+realmResults);
//                        List<DataModel> dataModels = sRealm.copyFromRealm(realmResults);
//                        Log.d(TAG, "onChange: dataModels - "+dataModels.size());
//                        s.onNext(dataModels);
//                    }
//                });
//            }
//        });
//    }


