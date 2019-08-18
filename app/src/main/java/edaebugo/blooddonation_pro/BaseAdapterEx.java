package edaebugo.blooddonation_pro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BaseAdapterEx extends BaseAdapter {
    Context mContext = null;
    ArrayList<Bloodcard> mData = null;
    LayoutInflater mLayoutInflater = null;

    public BaseAdapterEx(Context context, ArrayList<Bloodcard> data){
        mContext = context;
        mData = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemLayout = mLayoutInflater.inflate(R.layout.item_layout,null);
        TextView numberTV = (TextView) itemLayout.findViewById(R.id.number);
        TextView orderTV = (TextView) itemLayout.findViewById(R.id.order);

        numberTV.setText(mData.get(position).mNumber);
        orderTV.setText(mData.get(position).mOrder);
        return itemLayout;
    }
}
