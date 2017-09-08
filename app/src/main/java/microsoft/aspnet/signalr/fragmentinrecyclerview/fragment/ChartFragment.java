package microsoft.aspnet.signalr.fragmentinrecyclerview.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import microsoft.aspnet.signalr.fragmentinrecyclerview.R;
import microsoft.aspnet.signalr.fragmentinrecyclerview.data.DbManager;


public class ChartFragment extends Fragment {

    private static final String ARG_PARAM = "param1";
    private String mInstrumentName;

    public ChartFragment() {
    }

    public static ChartFragment newInstance(String param) {
        ChartFragment mFragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        mFragment.setArguments(args);
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mInstrumentName = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        final TextView textView = (TextView) view.findViewById(R.id.tv_text_fr);

        DbManager.getPrice(mInstrumentName)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        textView.setText(s);
                    }
                });

        return view;
    }
}
