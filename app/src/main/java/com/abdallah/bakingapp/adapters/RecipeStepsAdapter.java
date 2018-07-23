package com.abdallah.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdallah.bakingapp.R;
import com.abdallah.bakingapp.models.recipe.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Step RecyclerView adapter. This adapter highlights the selected item while in two pane mode.
 * But doesn't highlight it in single pane mode.
 */
public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {

    private static final String TAG = RecipeStepsAdapter.class.getSimpleName();

    private List<Step> steps;
    private boolean isTwoPane;
    private final ItemClickListener itemClickListener;

    private int selectedPos = 0;

    public RecipeStepsAdapter(List<Step> steps, boolean isTwoPane, ItemClickListener itemClickListener) {
        this.steps = steps;
        this.isTwoPane = isTwoPane;
        this.itemClickListener = itemClickListener;
    }

    public RecipeStepsAdapter(List<Step> steps, int selectedPos, boolean isTwoPane
            , ItemClickListener itemClickListener) {
        this.steps = steps;
        this.selectedPos = selectedPos;
        this.isTwoPane = isTwoPane;
        this.itemClickListener = itemClickListener;
    }

    public RecipeStepsAdapter(boolean isTwoPane, ItemClickListener itemClickListener) {
        this.isTwoPane = isTwoPane;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step, parent, false);

        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Step currentStep = steps.get(position);
        Context ctx = holder.itemView.getContext();

        holder.stepIdTextView.setText(ctx.getString(R.string.recipe_step_id_text, currentStep.getId()));
        holder.shortDescriptionTextView.setText(currentStep.getShortDescription());
        if (!currentStep.hasVideo()) holder.hasVideoImageView.setVisibility(View.GONE);
        else holder.hasVideoImageView.setVisibility(View.VISIBLE);

        if (isTwoPane) holder.itemView.setSelected(selectedPos == position);
    }

    @Override
    public int getItemCount() {
        if (steps == null) return 0;
        return steps.size();
    }

    public void swapSteps(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_step_id) TextView stepIdTextView;
        @BindView(R.id.tv_short_description) TextView shortDescriptionTextView;
        @BindView(R.id.iv_has_video) ImageView hasVideoImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (isTwoPane) {
                notifyItemChanged(selectedPos);
                selectedPos = getLayoutPosition();
                notifyItemChanged(selectedPos);
            }

            int clickedItemIndex = getAdapterPosition();
            itemClickListener.onRecyclerViewItemClicked(clickedItemIndex);
        }
    }

    /**
     * Used in handling items clicks.
     */
    public interface ItemClickListener {
        void onRecyclerViewItemClicked(int clickedItemIndex);
    }
}
