package com.projetos.redes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projetos.redes.R;
import com.projetos.redes.adapters.MessagesAdapter;

public class FragmentMessages extends Fragment {
    private RecyclerView view;
    private MessagesAdapter adapter;

    public static FragmentMessages newInstance() {

        Bundle args = new Bundle();

        FragmentMessages fragment = new FragmentMessages();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_messages, container, false);

        view = v.findViewById(R.id.messages);
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MessagesAdapter(getContext());
        view.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
