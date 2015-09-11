package com.dsv.tourism.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dsv.tourism.R;
import com.dsv.tourism.model.Answer;

import java.util.List;

/**
 * Created by Vince on 27.08.2015.
 */
public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

    private List<Answer> answersList;
    private OnItemClickListener mItemClickListener;

    public AnswerAdapter(List<Answer> answersList) {
        this.answersList = answersList;
    }

    @Override
    public int getItemCount() {
        return answersList.size();
    }

    @Override
    public void onBindViewHolder(AnswerViewHolder contactViewHolder, int i) {
        Answer a = answersList.get(i);


        contactViewHolder.vName.setText(a.getmText());

        if(a.getmDescription() == null || a.getmDescription().length() == 0 || a.getmDescription().equals(""))
        {
            contactViewHolder.vSurname.setVisibility(View.GONE);
        } else {
            contactViewHolder.vSurname.setVisibility(View.VISIBLE);
            contactViewHolder.vSurname.setText(a.getmDescription());
        }
    }

    @Override
    public AnswerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.row_list_anser, viewGroup, false);

        return new AnswerViewHolder(itemView);
    }

    public void SetOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView vName;
        protected TextView vSurname;

        public AnswerViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txtName);
            vSurname = (TextView)  v.findViewById(R.id.txtSurname);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}