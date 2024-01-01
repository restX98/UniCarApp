package com.example.unicarapp.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.unicarapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class RideDialogFragment extends BottomSheetDialogFragment {

    private TextView hiddenTextView;

    public RideDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_dialog, container, false);

        hiddenTextView = view.findViewById(R.id.hiddenTextView);

        Button showMoreButton = view.findViewById(R.id.showMoreButton);
        showMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreContent();
            }
        });

        return view;
    }

    private void showMoreContent() {
        hiddenTextView.setVisibility(View.VISIBLE);
    }
}
