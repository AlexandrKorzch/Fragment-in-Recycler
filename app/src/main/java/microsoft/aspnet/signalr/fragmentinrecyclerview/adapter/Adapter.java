package microsoft.aspnet.signalr.fragmentinrecyclerview.adapter;

import android.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


import java.util.List;

import microsoft.aspnet.signalr.fragmentinrecyclerview.R;
import microsoft.aspnet.signalr.fragmentinrecyclerview.fragment.ChartFragment;
import microsoft.aspnet.signalr.fragmentinrecyclerview.model.DataModel;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {


    private final FragmentManager mFragmentManager;
    private List<DataModel> mDataModels;

    public Adapter(List<DataModel> myDataset, FragmentManager fragmentManager) {
        mDataModels = myDataset;
        mFragmentManager = fragmentManager;
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DataModel dataModel = mDataModels.get(position);
        holder.mTextView.setText(dataModel.getInstrumentName());
        int containerId = holder.mContainer.getId();

        ChartFragment oldFragment = (ChartFragment) mFragmentManager.findFragmentById(containerId);
        if (oldFragment != null) {
            try {
                mFragmentManager.beginTransaction().remove(oldFragment).commit();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }

        int newContainerId = getUniqueID(dataModel.getInstrumentName());
        holder.mContainer.setId(newContainerId);

        ChartFragment fragment = ChartFragment.newInstance(dataModel.getInstrumentName());
        mFragmentManager.beginTransaction().replace(newContainerId, fragment).commit();

    }

    @Override
    public int getItemCount() {
        return mDataModels.size();
    }

    public int getUniqueID(String instrumenName) {
        return instrumenName.hashCode();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public FrameLayout mContainer;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.tv_text);
            mContainer = (FrameLayout) view.findViewById(R.id.container);
        }
    }

}
