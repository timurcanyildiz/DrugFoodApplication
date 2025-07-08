package com.example.drugfoodapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drugfoodapplication.R;
import com.example.drugfoodapplication.data.entity.Food;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<Food> foods;
    private OnFoodClickListener listener;
    private Context context;

    public interface OnFoodClickListener {
        void onFavoriteClick(Food food);
        void onDeleteClick(Food food);
    }

    public FoodAdapter(List<Food> foods, OnFoodClickListener listener) {
        this.foods = foods;
        this.listener = listener;
    }

    public void updateFoods(List<Food> newFoods) {
        this.foods.clear();
        this.foods.addAll(newFoods);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foods.get(position);
        holder.bind(food);
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {

        private ImageView foodIcon;
        private TextView foodName;
        private TextView foodCategory;
        private TextView foodPortion;
        private TextView foodCalories;
        private TextView foodDescription;
        private ImageButton favoriteButton;
        private ImageButton deleteButton;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            foodIcon = itemView.findViewById(R.id.foodIcon);
            foodName = itemView.findViewById(R.id.foodName);
            foodCategory = itemView.findViewById(R.id.foodCategory);
            foodPortion = itemView.findViewById(R.id.foodPortion);
            foodCalories = itemView.findViewById(R.id.foodCalories);
            foodDescription = itemView.findViewById(R.id.foodDescription);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(Food food) {
            foodName.setText(food.getName());
            foodCategory.setText(food.getCategory());
            foodPortion.setText(food.getPortion());
            foodCalories.setText(food.getCalories() + " kcal");
            foodDescription.setText(food.getDescription());

            // Set food icon based on category
            setFoodIcon(food.getCategory());

            // Set favorite button state
            if (food.isFavorite()) {
                favoriteButton.setImageResource(R.drawable.ic_favorite_filled);
            } else {
                favoriteButton.setImageResource(R.drawable.ic_favorite_outline);
            }

            // Set click listeners
            favoriteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFavoriteClick(food);
                }
            });

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(food);
                }
            });
        }

        private void setFoodIcon(String category) {
            int iconResource;

            switch (category) {
                case "Meyveler":
                    iconResource = R.drawable.ic_fruit;
                    break;
                case "Sebzeler":
                    iconResource = R.drawable.ic_vegetable;
                    break;
                case "Tahıllar":
                    iconResource = R.drawable.ic_grain;
                    break;
                case "Protein":
                    iconResource = R.drawable.ic_protein;
                    break;
                case "Süt Ürünleri":
                    iconResource = R.drawable.ic_dairy;
                    break;
                case "Yağlar":
                    iconResource = R.drawable.ic_oil;
                    break;
                default:
                    iconResource = R.drawable.ic_food_default;
                    break;
            }

            foodIcon.setImageResource(iconResource);
        }
    }
}