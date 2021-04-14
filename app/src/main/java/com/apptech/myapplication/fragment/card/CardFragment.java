package com.apptech.myapplication.fragment.card;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apptech.myapplication.R;
import com.apptech.myapplication.adapter.CardAdapter;
import com.apptech.myapplication.databinding.CardFragmentBinding;
import com.apptech.myapplication.modal.card.CardList;
import com.apptech.myapplication.other.SessionManage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CardFragment extends Fragment {


    CardFragmentBinding binding;
    private CardViewModel mViewModel;
    SessionManage sessionManage;
    private static final String TAG = "CardFragment";


    public static CardFragment newInstance() {
        return new CardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CardFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CardViewModel.class);
//        sessionManage = new SessionManage(requireContext());
        sessionManage = SessionManage.getInstance(requireContext());
        String js = sessionManage.getUserDetails().get("CARD_DATA");
        Gson gson = new Gson();
        Type type = new TypeToken<List<CardList>>() {
        }.getType();
        List<CardList> cardData = gson.fromJson(js, type);
        Log.e(TAG, "onActivityCreated: " + cardData.size() );
//        binding.CardRecyclerView.setAdapter(new CardAdapter(cardData));



    }

}








































