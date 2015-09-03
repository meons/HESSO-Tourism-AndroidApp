package com.dsv.tourism.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.dsv.tourism.R;
import com.dsv.tourism.model.Recommendation;

import java.util.List;

/**
 * Created by Vince on 27.08.2015.
 */
public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.AnswerViewHolder> {

    private List<Recommendation> answersList;
    private OnItemClickListener mItemClickListener;

    public RecommendationAdapter(List<Recommendation> answersList) {
        this.answersList = answersList;
    }

    @Override
    public int getItemCount() {
        return answersList.size();
    }

    @Override
    public void onBindViewHolder(AnswerViewHolder contactViewHolder, int i) {
        Recommendation r = answersList.get(i);

        contactViewHolder.vName.setText(r.getmName());
    }

    @Override
    public AnswerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.row_list_recommendation, viewGroup, false);

        itemView.startAnimation(AnimationUtils.loadAnimation(viewGroup.getContext(), R.anim.abc_slide_in_bottom));
        itemView.setVisibility(View.VISIBLE);

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

        public AnswerViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txtName);

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