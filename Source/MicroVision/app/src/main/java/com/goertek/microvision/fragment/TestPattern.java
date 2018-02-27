package com.goertek.microvision.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import android.widget.TextView;

import com.goertek.microvision.R;

import picop.interfaces.def.PicoP_TestPatternInfoE;

import static com.goertek.microvision.MessageCenter.messageHandler;
import static com.goertek.microvision.Utils.MSG_GET_DRAWTESTPATTERN;
import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;


public class TestPattern extends Fragment {

    public TestPattern(){}

    int selectedColor = -12666182;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_test_pattern, container, false);

        final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radio_group_pattern_picker);
        radioGroup.check(R.id.radio_btn_checker_board); // pre-select the first one

        final Button button = (Button)rootView.findViewById(R.id.btn_draw_test_pattern);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PicoP_TestPatternInfoE pattern;

                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.radio_btn_checker_board:
                        pattern = PicoP_TestPatternInfoE.eCHECKER_BOARD_PATTERN;
                        break;
                    case R.id.radio_btn_splash_screen:
                        pattern = PicoP_TestPatternInfoE.eSPLASH_SCREEN;
                        break;
                    case R.id.radio_btn_grid_pattern:
                        pattern = PicoP_TestPatternInfoE.eGRID_PATTERN_16x12;
                        break;
                    case R.id.radio_btn_cross_hair:
                        pattern = PicoP_TestPatternInfoE.eCROSS_HAIR_PATTERN;
                        break;
                    case R.id.radio_btn_all_on:
                        pattern = PicoP_TestPatternInfoE.eALL_ON;
                        break;
                    case R.id.radio_btn_all_off:
                        pattern = PicoP_TestPatternInfoE.eALL_OFF;
                        break;
                    case R.id.radio_btn_nine_point:
                        pattern = PicoP_TestPatternInfoE.eNINE_POINT_PATTERN;
                        break;
                    default:
                        pattern = PicoP_TestPatternInfoE.eSPLASH_SCREEN;
                }

                // send message

                Message msg = messageHandler.obtainMessage(MSG_GET_DRAWTESTPATTERN);
                Bundle b = new Bundle();
                b.putInt("pattern", pattern.ordinal());
                b.putInt("color", selectedColor);
                msg.setData(b);
//                b.callback = some code
                messageHandler.sendMessage(msg);
            }
        });

        final HSLColorPicker colorPicker = (HSLColorPicker) rootView.findViewById(R.id.colorPicker);
        final TextView textView = (TextView) rootView.findViewById(R.id.foregroundColorSelectedTextView);
        colorPicker.setColor(selectedColor); // default

        colorPicker.setColorSelectionListener(new SimpleColorSelectionListener() {
            @Override
            public void onColorSelected(int intColor) {
                selectedColor = intColor;

                // for formatting purposes
                int red = Color.red(intColor);
                int green = Color.green(intColor);
                int blue = Color.blue(intColor);

                String colorText = String.format("R:%d G:%d B:%d", red, green, blue);
                textView.setText(colorText);
            }
        });

        return rootView;
    }
}