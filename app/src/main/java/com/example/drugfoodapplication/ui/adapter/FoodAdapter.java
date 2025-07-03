package com.example.drugfoodapplication.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.drugfoodapplication.R;
import com.example.drugfoodapplication.data.entity.Food;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    public interface OnFoodActionListener {
        void onFoodClick(Food food);
        void onFavoriteClick(Food food);
        void onFoodLongClick(Food food, int position);
    }

    private List<Food> foodList;
    private OnFoodActionListener listener;

    public FoodAdapter(List<Food> foodList, OnFoodActionListener listener) {
        this.foodList = foodList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.foodName.setText(food.name);
        holder.foodCategory.setText(food.category);
        holder.foodDescription.setText(food.description);
        holder.foodCalories.setText(food.calories + " kcal");

        // Favori ikonu durumu
        holder.favoriteIcon.setImageResource(food.favorite ?
                R.drawable.ic_favorite_filled : R.drawable.ic_favorite_outline);

        // Tıklama
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onFoodClick(food);
        });
        // Uzun tıklama
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) listener.onFoodLongClick(food, position);
            return true;
        });
        // Favori
        holder.favoriteIcon.setOnClickListener(v -> {
            if (listener != null) listener.onFavoriteClick(food);
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void setFoodList(List<Food> foods) {
        this.foodList = foods;
        notifyDataSetChanged();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodCategory, foodDescription, foodCalories;
        ImageView favoriteIcon;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodCategory = itemView.findViewById(R.id.food_category);
            foodDescription = itemView.findViewById(R.id.food_description);
            foodCalories = itemView.findViewById(R.id.food_calories);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
        }
    }
}
