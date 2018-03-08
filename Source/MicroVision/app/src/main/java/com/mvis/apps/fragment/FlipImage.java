package com.mvis.apps.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mvis.apps.R;

import static com.mvis.apps.MessageCenter.messageHandler;
import static com.mvis.apps.Utils.MSG_FLIP_IMAGE;

/**
 * Created on 2017/4/18.
 */

public class FlipImage extends Fragment {
    protected static final String TAG = "FlipImage";
    private LinearLayout mLayout_Horizontal, mLayout_Vertical, mLayout_Both;
    private TextView mTv_Horizontal, mTv_Vertical, mTv_both;
    private View rootView;
    private Button mButtonSet;
    private int direction = 0;

    public static final int MSG_FLIPIMAGE_RESPONSE_GET = 90001;
    public static Handler flipimageHandler = new Handler() {
        // when handler.message() called, below code will be triggered.
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case MSG_FLIPIMAGE_RESPONSE_GET:
                    Bundle b = msg.getData();
                    int result = b.getInt("result");
                    if (result == 0) {
                        int direction = b.getInt("direction");
                    }
            }
        }
    };

    public FlipImage() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_flipimage, container, false);
        mLayout_Horizontal = (LinearLayout) rootView.findViewById(R.id.fragment_flipimage_layout_horizontal);
        mLayout_Horizontal.setOnClickListener(layoutClickListener);
        mLayout_Vertical = (LinearLayout) rootView.findViewById(R.id.fragment_flipimage_layout_vertical);
        mLayout_Vertical.setOnClickListener(layoutClickListener);
        mLayout_Both = (LinearLayout) rootView.findViewById(R.id.fragment_flipimage_layout_both);
        mLayout_Both.setOnClickListener(layoutClickListener);

        mTv_Horizontal = (TextView) rootView.findViewById(R.id.fragment_flipimage_layout_tv_horizontal);
        mTv_Vertical = (TextView) rootView.findViewById(R.id.fragment_flipimage_layout_tv_vertical);
        mTv_both = (TextView)rootView.findViewById(R.id.fragment_flipimage_layout_tv_both);

        mButtonSet = (Button)rootView.findViewById(R.id.fragment_flipimage_bt_set);
        mButtonSet.setOnClickListener(layoutClickListener);

        direction = 1;
        return rootView;
    }

    private View.OnClickListener layoutClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_flipimage_layout_horizontal: {
                    direction = 0;
                    mLayout_Horizontal.setBackgroundResource(R.drawable.shape_cart);
                    mLayout_Vertical.setBackgroundResource(R.drawable.shape_cart3);
                    mLayout_Both.setBackgroundResource(R.drawable.shape_cart3);

                    mTv_Horizontal.setTextColor(v.getResources().getColor(R.color.tab_light));
                    mTv_Vertical.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    mTv_both.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    break;
                }
                case R.id.fragment_flipimage_layout_vertical: {
                    direction = 1;
                    mLayout_Horizontal.setBackgroundResource(R.drawable.shape_cart3);
                    mLayout_Vertical.setBackgroundResource(R.drawable.shape_cart);
                    mLayout_Both.setBackgroundResource(R.drawable.shape_cart3);

                    mTv_Horizontal.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    mTv_Vertical.setTextColor(v.getResources().getColor(R.color.tab_light));
                    mTv_both.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    break;
                }
                case R.id.fragment_flipimage_layout_both:{
                    direction = 2;
                    mLayout_Horizontal.setBackgroundResource(R.drawable.shape_cart3);
                    mLayout_Vertical.setBackgroundResource(R.drawable.shape_cart3);
                    mLayout_Both.setBackgroundResource(R.drawable.shape_cart);

                    mTv_Horizontal.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    mTv_Vertical.setTextColor(v.getResources().getColor(R.color.tab_dark));
                    mTv_both.setTextColor(v.getResources().getColor(R.color.tab_light));
                    break;
                }
                case R.id.fragment_flipimage_bt_set:{
                    Message msg0 = messageHandler.obtainMessage(MSG_FLIP_IMAGE);
                    Bundle b = new Bundle();
                    b.putInt("direction", direction);
                    msg0.setData(b);
                    messageHandler.sendMessage(msg0);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    };

}



