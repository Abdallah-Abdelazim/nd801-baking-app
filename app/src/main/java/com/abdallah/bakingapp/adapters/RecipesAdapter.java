package com.abdallah.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abdallah.bakingapp.R;
import com.abdallah.bakingapp.models.recipe.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private static final String TAG = RecipesAdapter.class.getSimpleName();

    private List<Recipe> recipes;
    private final ItemClickListener itemClickListener;

    public RecipesAdapter(List<Recipe> recipes, ItemClickListener itemClickListener) {
        this.recipes = recipes;
        this.itemClickListener = itemClickListener;
    }

    public RecipesAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe currentRecipe = recipes.get(position);
        Context ctx = holder.itemView.getContext();

        holder.recipeNameTextView.setText(currentRecipe.getName());
        holder.recipeServingsTextView.setText(
                ctx.getString(R.string.recipe_servings_text, currentRecipe.getServings()));
    }

    @Override
    public int getItemCount() {
        if (recipes == null) return 0;
        return recipes.size();
    }

    public void swapRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_recipe_name) TextView recipeNameTextView;
        @BindView(R.id.tv_recipe_servings) TextView recipeServingsTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedItemIndex = getAdapterPosition();
            itemClickListener.onRecyclerViewItemClick(clickedItemIndex);
        }
    }

    /**
     * Used in handling items clicks.
     */
    public interface ItemClickListener {
        void onRecyclerViewItemClick(int clickedItemIndex);
    }
}
