package bamboobush.com.wheresx.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import bamboobush.com.wheresx.R;

public class ScoreDetailBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {

    private ScoreDetailBottomSheet mThis;

    OnBottomSheetListener mCallback;
    private TextView core_message;
    private Button btn_next_level;
    private String message;

    @Override
    public void onClick(View v) {
        getDialog().dismiss();
    }

    //Container activity must implement this interface
    public interface OnBottomSheetListener {
        public void onBottomSheetReturn(String action);
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThis = this;
        try {
            mCallback = (OnBottomSheetListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnBottomSheetListener");
        }
    }

    public void setMessage( String _message ) {
        message = _message;
    }

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View contentView = View.inflate(getContext(), R.layout.score_details_bottom_sheet, null);
        btn_next_level =(Button) contentView.findViewById(R.id.btn_next_level);
        btn_next_level.setOnClickListener(this);
        core_message =(TextView) contentView.findViewById(R.id.core_message);
        core_message.setText(message);

        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        mCallback.onBottomSheetReturn("Next");

    }
}