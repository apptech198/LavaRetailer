package com.apptech.myapplication.fragment.message_centre;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.myapplication.adapter.MessageAdapter;
import com.apptech.myapplication.databinding.MessageCentreFragmentBinding;
import com.apptech.myapplication.modal.message.MessageList;

import java.util.ArrayList;
import java.util.List;


public class MessageCentreFragment extends Fragment {

    private MessageCentreViewModel mViewModel;
    MessageCentreFragmentBinding binding;
    List<MessageList> messageLists = new ArrayList<>();

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

        fakeMessage();
        binding.messageRecyclerView.setAdapter(new MessageAdapter(messageLists));

    }


    private void fakeMessage() {
        messageLists.add(new MessageList("What is Lorem Ipsum", "01/09/2020 3:02 pm", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic", false));
        messageLists.add(new MessageList("Why do we use it", "01/09/2020 3:02 pm", "rem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search f", false));
        messageLists.add(new MessageList("What is Lorem Ipsum", "01/09/2020 3:02 pm", "e majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't an", false));
        messageLists.add(new MessageList("What is Lorem Ipsum", "01/09/2020 3:02 pm", "during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.\n" +
                "\n" +
                "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form,", false));
        messageLists.add(new MessageList("What is Lorem Ipsum", "01/09/2020 3:02 pm", "over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 ", false));

    }

}




























