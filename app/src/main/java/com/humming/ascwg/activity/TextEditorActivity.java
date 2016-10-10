package com.humming.ascwg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.humming.ascwg.Application;
import com.humming.ascwg.model.TextEditorData;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.AbstractActivity;

public class TextEditorActivity extends AbstractActivity {
    public static final int ACTIVITY_TEXT_EDITOR_RESULT = 10001;

    public static final String KEY_TEXT = "text";

    public static final int ACTIVITY_CHANGE_NAME = 10101;
    public static final String CURRENT_TEXT_VALUE = "text_value";
    private TextEditorData textEditorData;
    private ActionBar actionBar;
    private EditText editText;
    private boolean changed = true;
    private MenuItem menuItem;
    private TextView title;
    private TextView back;
    private TextView confrim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        actionBar = getSupportActionBar();
        editText = (EditText) findViewById(R.id.text);
        String name = getIntent().getStringExtra(TextEditorActivity.CURRENT_TEXT_VALUE);
        if (!"".equals(name)) {
            editText.setText(name);
        }
        textEditorData = Application.getInstance().getTextEditorData();
        boolean singleLine = textEditorData.isSingleLine();

        title = (TextView) findViewById(R.id.toolbar_title);
        back = (TextView) findViewById(R.id.toolbar_back);
        confrim = (TextView) findViewById(R.id.toolbar_cofirm);
        confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle resultBundle = new Bundle();
                resultBundle.putString(
                        TextEditorActivity.KEY_TEXT,
                        editText.getText().toString());
                Intent resultIntent = new Intent()
                        .putExtras(resultBundle);
                setResult(
                        TextEditorActivity.ACTIVITY_TEXT_EDITOR_RESULT,
                        resultIntent);
                finish();
            }
        });
        title.setText(getResources().getString(R.string.nick_name));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle resultBundle = new Bundle();
                resultBundle.putString(
                        TextEditorActivity.KEY_TEXT,
                        "");
                Intent resultIntent = new Intent()
                        .putExtras(resultBundle);
                setResult(
                        TextEditorActivity.ACTIVITY_TEXT_EDITOR_RESULT,
                        resultIntent);
                finish();
            }
        });
        editText.setLines(textEditorData.getMaxLines());
        // editText.setMaxLines(textEditorData.getMaxLines());
        editText.setHint(textEditorData.getHint());
        editText.setInputType(textEditorData.getInputType());
        editText.setSingleLine(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Bundle resultBundle = new Bundle();
            resultBundle.putString(
                    TextEditorActivity.KEY_TEXT,
                    "");
            Intent resultIntent = new Intent()
                    .putExtras(resultBundle);
            setResult(
                    TextEditorActivity.ACTIVITY_TEXT_EDITOR_RESULT,
                    resultIntent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
