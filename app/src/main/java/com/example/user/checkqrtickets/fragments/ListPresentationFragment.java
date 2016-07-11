package com.example.user.checkqrtickets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.checkqrtickets.R;
import com.example.user.checkqrtickets.activities.SearchQRCode;
import com.example.user.checkqrtickets.asynctasks.AsyncRepresentation;
import com.example.user.checkqrtickets.entities.Json;
import com.example.user.checkqrtickets.entities.Representation;
import com.example.user.checkqrtickets.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by User on 21.06.2016.
 */
public class ListPresentationFragment extends Fragment {
    private RecyclerView mListPresentationRV;
    private List<Representation> mListRepresentations;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_presentation, container, false);

        mListPresentationRV = (RecyclerView) view.findViewById(R.id.list_presentation_recycler_view);
        mListPresentationRV.setHasFixedSize(true);
        mListPresentationRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        mListRepresentations = new ArrayList<>();
        updateListRepresentation();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSubtitle();
    }

    private List<Representation> getListRepresentation() {
        AsyncRepresentation asyncTask = new AsyncRepresentation();
        List<Representation> listRepresentations = null;

        try {
            String jsonArray = asyncTask.execute().get();
            if(jsonArray != null) {
                listRepresentations = Json.getListRepresentation(jsonArray);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return listRepresentations;
    }

    private void updateListRepresentation() {
        if(!mListRepresentations.isEmpty())
            mListRepresentations.clear();

        mListRepresentations = getListRepresentation();
        if(mListRepresentations == null) {
                Toast.makeText(getActivity(), getString(R.string.msg_error_get_data_from_server), Toast.LENGTH_SHORT).show();
        }
        else {
            for(Representation rep : mListRepresentations) {
                if(!rep.isActive() || !Utils.isDateNow(rep.getDate())) {
                    mListRepresentations.remove(rep);
                }
            }
            if(mListRepresentations.isEmpty()) {Toast.makeText(getActivity(),  getString(R.string.msg_list_represent_is_empty), Toast.LENGTH_SHORT).show();}
                else {
                    ListRepresentationAdapter adapter = new ListRepresentationAdapter(mListRepresentations);
                   mListPresentationRV.setAdapter(adapter);
                }
        }
        ListRepresentationAdapter adapter = new ListRepresentationAdapter(mListRepresentations);
        mListPresentationRV.setAdapter(adapter);
    }

    private void updateSubtitle() {
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(getResources().getQuantityString(R.plurals.subtitle_count_representation, mListRepresentations.size(), mListRepresentations.size()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_representation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.item_update_list_representation : {
                updateListRepresentation();
                updateSubtitle();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private class ListRepresentationAdapter extends RecyclerView.Adapter<RepresentationViewHolder> {

        private List<Representation> mListRepresentations;

        public ListRepresentationAdapter(List<Representation> listRepresentations) {
            mListRepresentations = listRepresentations;
        }


        @Override
        public RepresentationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = (View) LayoutInflater.from(getActivity()).inflate(R.layout.card_view, parent, false);
            return new RepresentationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RepresentationViewHolder holder, int position) {
            Representation representation = mListRepresentations.get(position);
            holder.bindViewHolder(representation);
        }

        @Override
        public int getItemCount() {
            return mListRepresentations.size();
        }
    }

    public class RepresentationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mNameRepresent, mStartTime;

        private Representation mRepresentation;

        public RepresentationViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mNameRepresent = (TextView) itemView.findViewById(R.id.name_represent);
            mStartTime = (TextView)itemView.findViewById(R.id.start_time_represent);
        }

        protected  void  bindViewHolder(Representation representation) {
            mRepresentation = representation;
            mNameRepresent.setText(representation.getName());
            mStartTime.setText(Utils.getTime(representation.getDate()));
        }

        @Override
        public void onClick(View v) {
            Intent intent = SearchQRCode.newIntent(getActivity(), mRepresentation.getId());
            startActivity(intent);
        }
    }


}
