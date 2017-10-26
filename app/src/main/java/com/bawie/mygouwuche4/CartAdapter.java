package com.bawie.mygouwuche4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 张肖肖 on 2017/10/24.
 */

public class CartAdapter extends BaseAdapter{

    private RefreshPriceInterface refreshPriceInterface;
    private Map<String,Integer> pitchOnMap;
    private ViewHolder vh;
    private final Context context;

    private final List<HashMap<String, String>> goodlist;

    public CartAdapter(Context context, List<HashMap<String, String>> goodsList) {
        this.context = context;
        this.goodlist = goodsList;

        pitchOnMap=new HashMap<>();
        for(int i=0;i<goodlist.size();i++){
            pitchOnMap.put(goodlist.get(i).get("id"),0);
        }
    }

    @Override
    public int getCount() {
        if (goodlist != null) {
            return goodlist.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        vh=new ViewHolder();
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_shopcart_product,null);

            vh.checkBox=(CheckBox)view.findViewById(R.id.check_box);
            vh.icon=(ImageView)view.findViewById(R.id.iv_adapter_list_pic);
            vh.name=(TextView)view.findViewById(R.id.tv_goods_name);
            vh.price=(TextView)view.findViewById(R.id.tv_goods_price);
          // vh.sellerName=(TextView)view.findViewById(R.id.tv_shangjia);
            vh.num=(TextView)view.findViewById(R.id.tv_num);
            vh.reduce=(TextView)view.findViewById(R.id.tv_reduce);
            vh.add=(TextView)view.findViewById(R.id.tv_add);

            view.setTag(vh);
        }else {
            vh=(ViewHolder)view.getTag();
        }

        if(goodlist.size()>0){

            if(pitchOnMap.get(goodlist.get(position).get("id"))==0)vh.checkBox.setChecked(false);
            else vh.checkBox.setChecked(true);
            HashMap<String,String> map=goodlist.get(position);
            vh.name.setText(map.get("name"));
            vh.num.setText(map.get("count"));
            vh.price.setText("￥ "+(Double.valueOf(map.get("price")) * Integer.valueOf(map.get("count"))));
            //商家的名字
         //  vh.sellerName.setText(map.get("sellerName"));

            Glide.with(context).load(map.get("img")).into(vh.icon);

            vh.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int index=position;
                    if(((CheckBox)view).isChecked())pitchOnMap.put(goodlist.get(index).get("id"),1);else pitchOnMap.put(goodlist.get(index).get("id"),0);
                    refreshPriceInterface.refreshPrice(pitchOnMap);
                }
            });
            vh.reduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int index=position;
                    goodlist.get(index).put("count",(Integer.valueOf(goodlist.get(index).get("count"))-1)+"");
                    if(Integer.valueOf(goodlist.get(index).get("count"))<=0){
                        //可提示是否删除该商品,确定就remove,否则就保留1
                        String deID=goodlist.get(index).get("id");
                        goodlist.remove(index);
                        pitchOnMap.remove(deID);
                    }
                    notifyDataSetChanged();
                    refreshPriceInterface.refreshPrice(pitchOnMap);
                }
            });
            vh.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int index=position;
                    goodlist.get(index).put("count",(Integer.valueOf(goodlist.get(index).get("count"))+1)+"");
                    if(Integer.valueOf(goodlist.get(index).get("count"))>15){
                        //15为库存可选择上限
                        Toast.makeText(context,"已达库存上限~",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    notifyDataSetChanged();
                    refreshPriceInterface.refreshPrice(pitchOnMap);
                }
            });
        }

        return view;
    }

    public Map<String,Integer> getPitchOnMap(){
        return pitchOnMap;
    }

    public void setPitchOnMap(Map<String,Integer> pitchOnMap){
        this.pitchOnMap=new HashMap<>();
        this.pitchOnMap.putAll(pitchOnMap);
    }

    public interface RefreshPriceInterface{
        void refreshPrice(Map<String,Integer> pitchOnMap);
    }

    public void setRefreshPriceInterface(RefreshPriceInterface refreshPriceInterface){
        this.refreshPriceInterface=refreshPriceInterface;
    }

    class ViewHolder{
        CheckBox checkBox;
        ImageView icon;
        TextView name,price,num,reduce,add;
    }
}
