package com.example.a2048;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.View;

import androidx.fragment.app.Fragment;

public class WinFragment extends Fragment {

    public WinFragment(){
        super(R.layout.win_fragment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_in));

    }

    @Override
    public void onViewCreated(View v ,Bundle savedInstanceState ){
        super.onViewCreated(v,savedInstanceState);
        getActivity().findViewById(R.id.button).setVisibility(View.INVISIBLE);
    }

    @Override
    public  void onStop(){
        super.onStop();
        getActivity().findViewById(R.id.button).setVisibility(View.VISIBLE);
    }

}
