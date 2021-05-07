package com.apptech.myapplication.ui.message_centre;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.myapplication.R;
import com.apptech.myapplication.adapter.ExpansisAdapter;
import com.apptech.myapplication.adapter.MessageAdapter;
import com.apptech.myapplication.databinding.MessageCentreFragmentBinding;
import com.apptech.myapplication.modal.message.MessageList;
import com.apptech.myapplication.modal.message.NotificationListBrandWise;
import com.apptech.myapplication.other.SessionManage;
import com.apptech.myapplication.service.ApiClient;
import com.apptech.myapplication.service.LavaInterface;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MessageCentreFragment extends Fragment implements MessageAdapter.ExpInterface {

    private MessageCentreViewModel mViewModel;
    MessageCentreFragmentBinding binding;
    List<MessageList> messageLists = new ArrayList<>();
    private static final String TAG = "MessageCentreFragment";
    MessageAdapter messageAdapter;
    SessionManage sessionManage;
    LavaInterface lavaInterface;

    public static MessageCentreFragment newInstance() {
        return new MessageCentreFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = MessageCentreFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MessageCentreViewModel.class);
        // TODO: Use the ViewModel

        sessionManage = SessionManage.getInstance(requireContext());
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);

        MessageCenter();


        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select date");

        MaterialDatePicker materialDatePicker = builder.build();
        binding.datepicker.setOnClickListener(v -> {
            binding.datepicker.setClickable(false);
            materialDatePicker.show(getChildFragmentManager(), "");
        });

        materialDatePicker.addOnCancelListener(dialog -> {
            binding.datepicker.setClickable(true);
        });


        materialDatePicker.addOnDismissListener(dialog -> {
            binding.datepicker.setClickable(true);
        });


//        messageAdapter = new MessageAdapter(messageLists, this);
//        binding.messageRecyclerView.setAdapter(messageAdapter);


        binding.msgCount.setText(String.valueOf(messageLists.size()));

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {

            binding.datepicker.setClickable(true);

//            Log.e(TAG, "onActivityCreated: " + selection);
//            Log.e(TAG, "onActivityCreated: " + materialDatePicker.getHeaderText());
            String dateString = String.valueOf(selection);
            String breackString = dateString.substring(5, dateString.length() - 1);
            String[] DataSplit = breackString.split(" ");
            long StartDate = Long.parseLong(DataSplit[0]);
            long EndDate = Long.parseLong(DataSplit[1]);

            Log.e(TAG, "onActivityCreated: " + getTimeStamp(StartDate));
            Log.e(TAG, "onActivityCreated: " + getTimeStamp(EndDate));

        });


    }

    public String getTimeStamp(long timeinMillies) {
        String date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // modify format
        date = formatter.format(new Date(timeinMillies));
        System.out.println("Today is " + date);
        return date;
    }


    @Override
    public void onitemClick(MessageList list, int pos) {

//        List<MessageList> l = new ArrayList<>(messageLists);
//        Log.e(TAG, "onitemClick: " + l.get(pos).isExpanble());
//        l.get(pos).setExpanble(!list.isExpanble());
//        Log.e(TAG, "onitemClick: " + l.get(pos).isExpanble());
//        messageAdapter.setData(l);

    }


    private void MessageCenter() {

        lavaInterface.NotificationListBrandWise(sessionManage.getUserDetails().get("BRAND_ID")).enqueue(new Callback<NotificationListBrandWise>() {
            @Override
            public void onResponse(Call<NotificationListBrandWise> call, Response<NotificationListBrandWise> response) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {

                    if (!response.body().getError()) {
                        if (response.body().getList().size() > 0) {
                            binding.messageRecyclerView.setAdapter(new ExpansisAdapter(response.body().getList()));
                            binding.msgCount.setText(String.valueOf(response.body().getList().size()));
                        } else {
                            binding.noDatafound.setVisibility(View.VISIBLE);
                            binding.countLayout.setVisibility(View.GONE);
                        }
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }
                    binding.noDatafound.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                    binding.countLayout.setVisibility(View.GONE);
                    return;
                }
                binding.noDatafound.setVisibility(View.VISIBLE);
                binding.progressbar.setVisibility(View.GONE);
                binding.countLayout.setVisibility(View.GONE);
                Toast.makeText(getContext(), "" + getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<NotificationListBrandWise> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                binding.noDatafound.setVisibility(View.VISIBLE);
                binding.countLayout.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Time out", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText("Message Centre");
    }


}




























