package cn.gietv.mlive.views;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.gietv.mlive.R;
import cn.gietv.mlive.utils.SharedPreferenceUtils;

/**
 * Created by houde on 2016/3/5.
 */
public class VideoSettingPopuWindow extends PopupWindow implements View.OnClickListener{
    private Context mContext;
    private View mRootView;
    private TextView mScreenResult,mFontMax,mFontMid,mFontMin,tempTextView;
    private ImageView mDanmuPosition1,mDanmuPosition2,mDanmuPosition3,temImageView;
    private SeekBar mScreenSeekBar;
    public CheckBox mDanmuSwitch;
    private int whiteId;
    public VideoSettingPopuWindow(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        this.mContext = context;
        mRootView = inflater.inflate(R.layout.popu_video_setting,null);
        mScreenResult = (TextView) mRootView.findViewById(R.id.screen_result);
        mScreenSeekBar = (SeekBar) mRootView.findViewById(R.id.seekbar_screen);
        mDanmuSwitch = (CheckBox) mRootView.findViewById(R.id.popu_setting_switch);
        mFontMax = (TextView)mRootView.findViewById(R.id.font_max);
        mFontMid = (TextView)mRootView.findViewById(R.id.font_mid);
        mFontMin = (TextView)mRootView.findViewById(R.id.font_min);
        mDanmuPosition1 = (ImageView)mRootView.findViewById(R.id.danmu_position1);
        mDanmuPosition2 = (ImageView)mRootView.findViewById(R.id.danmu_position2);
        mDanmuPosition3 = (ImageView)mRootView.findViewById(R.id.danmu_position3);
        mFontMax.setOnClickListener(this);
        mFontMid.setOnClickListener(this);
        mFontMin.setOnClickListener(this);
        mDanmuPosition1.setOnClickListener(this);
        mDanmuPosition2.setOnClickListener(this);
        mDanmuPosition3.setOnClickListener(this);
        mDanmuSwitch.setChecked(true);
        ContentResolver cr = context.getContentResolver();
        int value = 0;
        try {
            value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        mScreenSeekBar.setProgress(value * 100 / 255);
        mScreenResult.setText(value * 100 / 255 + "%");
        mScreenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mScreenResult.setText(progress + "%");
                if (mIScreenLightListener != null)
                    mIScreenLightListener.getPosition(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        if(SharedPreferenceUtils.getInt("video_font",24)==24){
            changeFontBackgroundAndColor(mFontMax,24);
        }else if(SharedPreferenceUtils.getInt("video_font",24)==22){
            changeFontBackgroundAndColor(mFontMid,22);
        }else if(SharedPreferenceUtils.getInt("video_font",24)==20){
            changeFontBackgroundAndColor(mFontMin,20);
        }
        changePosition(mDanmuPosition2,R.mipmap.green_position,R.mipmap.white_position);
        setContentView(mRootView);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.danmu_position1) {
            changePosition(mDanmuPosition1, R.mipmap.green_above, R.mipmap.white_above);
        }else if(v.getId() == R.id.danmu_position2) {
            changePosition(mDanmuPosition2, R.mipmap.green_position, R.mipmap.white_position);
        }else if(v.getId() == R.id.danmu_position3) {
            changePosition(mDanmuPosition3, R.mipmap.green_bottom, R.mipmap.bottom_white);
        }else if(v.getId() == R.id.font_max) {
            changeFontBackgroundAndColor(mFontMax, 24);
        }else if(v.getId() == R.id.font_min){
                changeFontBackgroundAndColor(mFontMid,22);
        }else if(v.getId() == R.id.font_min){
                changeFontBackgroundAndColor(mFontMin,20);
        }
    }
    private void changeFontBackgroundAndColor(TextView textView,int fontSize){
        if(tempTextView != null){
            tempTextView.setTextColor(mContext.getResources().getColor(R.color.white));
            tempTextView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.commen_button_theme_color_white));
        }
        textView.setTextColor(mContext.getResources().getColor(R.color.theme_green));
        textView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.commen_button_theme_color_greed));
        SharedPreferenceUtils.saveProp("video_font", fontSize);
        tempTextView= textView;
    }
    private void changePosition(ImageView imageView,int green,int white){
        if(temImageView != null){
            temImageView.setImageResource(whiteId);
        }
        imageView.setImageResource(green);
        temImageView = imageView;
        whiteId = white;
    }
    private IScreenLightListener mIScreenLightListener;
    public void setiScreenLightListener(IScreenLightListener iScreenLightListener){
        this.mIScreenLightListener = iScreenLightListener;
    }
    public interface IScreenLightListener{
        void getPosition(int progress);
    }
}
