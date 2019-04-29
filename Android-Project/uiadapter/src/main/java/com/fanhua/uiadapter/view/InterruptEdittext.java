package com.fanhua.uiadapter.view;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

import java.lang.ref.SoftReference;

/**
 * EditText输入拦截.不能输入空字符
 */
public class InterruptEdittext extends AppCompatEditText {


    public InterruptEdittext(Context context) {
        super(context);
    }

    public InterruptEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterruptEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection real = super.onCreateInputConnection(outAttrs);
        return real == null ? null : new InputConnectionImpl(this, real, false);
    }

    public static class InputConnectionImpl extends InputConnectionWrapper {
        private SoftReference<EditText> mEditText;

        public InputConnectionImpl(EditText editText, InputConnection target, boolean mutable) {
            super(target, mutable);
            mEditText = new SoftReference<>(editText);
        }

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            // 拦截规则
            EditText editText = mEditText.get();

            if (text != null && text.length() > 0 && Character.isSpaceChar(text.charAt(0))
                    && editText != null
                    && TextUtils.isEmpty(editText.getText())) {
                // 第一个字符不能输入空白字符
                return false;
            }
            return super.commitText(text, newCursorPosition);
        }
    }
}
