package cn.gietv.mlive.modules.search.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

import cn.gietv.mlive.R;
import cn.gietv.mlive.base.AbsBaseActivity;
import cn.gietv.mlive.utils.StringUtils;
import cn.gietv.mlive.utils.ToastUtils;

/**
 * author：steven
 * datetime：15/10/5 16:26
 */
public class SearchActivity extends AbsBaseActivity implements OnClickListener {
    private EditText mEditText;
    private TextView mSearchButton;
    private static int i = 1;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        mEditText = (EditText) findViewById(R.id.search_et_text);
        mSearchButton = (TextView) findViewById(R.id.search_tv_search);
        mSearchButton.setOnClickListener(this);
        mEditText.requestFocus();
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    switch (event.getAction()){
                        case KeyEvent.ACTION_UP:
                            //发送请求
                            seachBody();
                            return true;
                        default:
                            return true;
                    }

                }
                return false;
            }
        });
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.search_tv_search){
            this.finish();
        }
    }
    private void seachBody() {
        if(!isFirst){
            return;
        }
        String text = mEditText.getText().toString();
        if (StringUtils.isNotEmpty(text)) {
            try {
                text = new String(text.getBytes(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            ToastUtils.showToast(this,"输入内容后再搜素");
            return;
        }
        isFirst = false;
        SearchResultActivity.openSearResultActivity(this,text,true);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_close_exit);
    }
}
