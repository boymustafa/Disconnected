package com.mustafa.silentplease.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import com.mustafa.silentplease.CustomSwitchCheckedListener;
import com.mustafa.silentplease.R;
import com.mustafa.silentplease.utils.Constants;

/**
 * Created by Mustafa.Gamesterz on 25/05/16.
 */
public class TimeIntervalDialogFragment extends DialogPreference {

    private static final int DEFAULT_MIN_VALUE = 1;
    private static final int DEFAULT_MAX_VALUE = 60;
    private static final int DEFAULT_VALUE = 5;

    private int minValue;
    private int maxValue;
    private int value;
    private NumberPicker numberPicker;
    private CustomSwitchCheckedListener switchCheckedListener;

    public TimeIntervalDialogFragment(Context context){
        this(context,null);
    }

    public TimeIntervalDialogFragment(Context context, AttributeSet attrs){
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumberPickerDialogPreference,0,0);
        try {
            setMinValue(a.getInteger(R.styleable.NumberPickerDialogPreference_min,DEFAULT_MIN_VALUE));
            setMaxValue(a.getInteger(R.styleable.NumberPickerDialogPreference_android_max,DEFAULT_MAX_VALUE));
        }finally {
            a.recycle();
        }

        setDialogLayoutResource(R.layout.preference_time_interval_picker_dialog);
        setPositiveButtonText(context.getString(R.string.set));
        setNegativeButtonText(context.getString(R.string.negative_dialog_button));
        setDialogIcon(null);
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public CustomSwitchCheckedListener getSwitchCheckedListener() {
        return switchCheckedListener;
    }

    public int getValue() {
        return value;
    }

    public boolean isSwitchOn(){
        return switchCheckedListener.isSwitchON();
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        TextView dialogMessageText = (TextView) view.findViewById(R.id.text_dialog_message);
        dialogMessageText.setText(getDialogMessage());

        this.numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        this.numberPicker.setMinValue(minValue);
        this.numberPicker.setMaxValue(maxValue);
        this.numberPicker.setValue(value);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        Switch sith = (Switch) view.findViewById(R.id.switchWidget);
        switchCheckedListener = new CustomSwitchCheckedListener(getContext(),sith,getKey()+ Constants.PREF_KEY_SWITCH_SUFFIX);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if(positiveResult){
            int numPickerVal = numberPicker.getValue();
            if(callChangeListener(numPickerVal)){
                setValue(numPickerVal);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index,DEFAULT_VALUE);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedInt(DEFAULT_VALUE) : (Integer) defaultValue);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if(state == null || !state.getClass().equals(SavedState.class)){
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState state1 = (SavedState) state;
        setMinValue(state1.minValue);
        setMaxValue(state1.maxValue);
        setValue(state1.value);


        super.onRestoreInstanceState(state1.getSuperState());
    }

    @Override
    protected Parcelable onSaveInstanceState() {

        final Parcelable superState = super.onSaveInstanceState();

        final SavedState savedState = new SavedState(superState);
        savedState.minValue = getMinValue();
        savedState.maxValue = getMaxValue();
        savedState.value = getValue();

        return savedState;
    }

    public void setMaxValue(int maxValue){
        this.maxValue = maxValue;
        setValue(Math.min(this.value,maxValue));
    }

    public void setMinValue(int minValue){
        this.minValue = minValue;
        setValue(Math.max(this.value,minValue));
    }

    public void setValue(int value){
        value = Math.max(Math.min(value,this.maxValue),this.minValue);

        if(value != this.value){
            this.value = value;
            persistInt(value);
            notifyChanged();
        }
    }

    private static class SavedState extends BaseSavedState {
        int minValue;
        int maxValue;
        int value;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);

            minValue = source.readInt();
            maxValue = source.readInt();
            value = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);

            dest.writeInt(minValue);
            dest.writeInt(maxValue);
            dest.writeInt(value);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
