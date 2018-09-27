package com.example.mycashcalc;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<ListItem> objects;

    MyAdapter(Context context, ArrayList<ListItem> products, MyCallBack callBack){
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myCallBack = callBack;
    }

    interface MyCallBack {
        void itemButtonCallingBack(int itemID, int position);
        void longTapCallingBack(int position);
    }

    private MyCallBack myCallBack;


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.myitem, parent, false);
        }

        ListItem li = getListItem(position);

        ((TextView) view.findViewById(R.id.tvDescr)).setText(li.name);
        ((TextView) view.findViewById(R.id.tvDirection)).setText(li.direction);
        ((TextView) view.findViewById(R.id.tvPrice)).setText(li.price);
//        ((ImageView) view.findViewById(R.id.ivImage)).setImageResource(li.image);

//        CheckBox cbDir = (CheckBox) view.findViewById(R.id.cbBox);
//        cbDir.setOnCheckedChangeListener(myChBoxChangeListListner);
//        cbDir.setTag(position);
//        cbDir.setChecked(li.chBox);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                myCallBack.longTapCallingBack(position);
                return false;
            }
        });

        final FloatingActionButton mm = (FloatingActionButton) view.findViewById(R.id.idButtonMenu);
//        final View finalView = view;
        mm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == mm) {
                    PopupMenu popup = new PopupMenu(ctx, v);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            myCallBack.itemButtonCallingBack(item.getItemId(), position);
                            return false;
                        }
                    });
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.button_menu, popup.getMenu());
                    popup.show();
                }
            }
        });

        return view;

    }

//    private void showMenu(View v, final int position) {
//        PopupMenu popup = new PopupMenu(ctx, v);
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                myCallBack.itemButtonCallingBack(item.getItemId(), position);
//                return false;
//            }
//        });
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.button_menu, popup.getMenu());
//        popup.show();
//    }


//    CompoundButton.OnCheckedChangeListener myChBoxChangeListListner = new CompoundButton.OnCheckedChangeListener() {
//
//        @Override
//        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//        }
//    };

    private ListItem getListItem(int position) {
        return ((ListItem) getItem(position));
    }
}
