package com.humming.ascwg.model;

import android.view.inputmethod.EditorInfo;

/**
 * Created by Zhtq on 2016/5/12.
 */
public class TextEditorData {
    private String title;
    private int maxLines = 1;
    private String text = "";
    private String hint;
    private String imeOptions = "actionUnspecified";
    private String imeActionLabel;
    private int imeActionId;
    private boolean singleLine = true;
    private int inputType = EditorInfo.TYPE_CLASS_TEXT;

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMaxLines() {
        return maxLines;
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getImeOptions() {
        return imeOptions;
    }

    public void setImeOptions(String imeOptions) {
        this.imeOptions = imeOptions;
    }

    public String getImeActionLabel() {
        return imeActionLabel;
    }

    public void setImeActionLabel(String imeActionLabel) {
        this.imeActionLabel = imeActionLabel;
    }

    public int getImeActionId() {
        return imeActionId;
    }

    public void setImeActionId(int imeActionId) {
        this.imeActionId = imeActionId;
    }

    public boolean isSingleLine() {
        return singleLine;
    }

    public void setSingleLine(boolean singleLine) {
        this.singleLine = singleLine;
    }
}
