package com.example.drugfoodapplication.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.drugfoodapplication.R;
import com.example.drugfoodapplication.data.entity.Food;
import com.example.drugfoodapplication.ui.adapter.FoodAdapter;
import com.example.drugfoodapplication.ui.viewmodel.FoodViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FoodFragment extends Fragment {
    private List<Food> allFoods = new ArrayList<>();

    private final Map<String, List<String>> foodMap = new HashMap<String, List<String>>() {{
        put("Meyveler", Arrays.asList("Elma", "Muz"));
        put("Sebzeler", Arrays.asList("Brokoli", "Havuç"));
        put("Tahıllar", Arrays.asList("Yulaf", "Quinoa"));
        put("Protein", Arrays.asList("Tavuk Göğsü", "Somon"));
        put("Süt Ürünleri", Arrays.asList("Yoğurt", "Süt"));
        put("Yağlar", Arrays.asList("Zeytinyağı", "Avokado"));
    }};
    private final Map<String, Integer> caloriesMap = new HashMap<String, Integer>() {{
        put("Elma", 52); put("Muz", 89);
        put("Brokoli", 34); put("Havuç", 41);
        put("Yulaf", 389); put("Quinoa", 368);
        put("Tavuk Göğsü", 165); put("Somon", 208);
        put("Yoğurt", 59); put("Süt", 42);
        put("Zeytinyağı", 884); put("Avokado", 160);
    }};
    private final Map<String, String> descriptionMap = new HashMap<String, String>() {{
        put("Elma", "Yüksek fiber, vitamin C");
        put("Muz", "Potasyum, vitamin B6");
        put("Brokoli", "Vitamin K, C, folik asit");
        put("Havuç", "Beta karoten, vitamin A");
        put("Yulaf", "Fiber, protein, magnezyum");
        put("Quinoa", "Protein, fiber, demir");
        put("Tavuk Göğsü", "Yüksek protein, az yağ");
        put("Somon", "Omega-3, protein");
        put("Yoğurt", "Probiyotik, kalsiyum");
        put("Süt", "Kalsiyum, protein");
        put("Zeytinyağı", "Tekli doymamış yağ");
        put("Avokado", "Sağlıklı yağlar, fiber");
    }};
    private static final String[] CATEGORIES = {"Tümü", "Meyveler", "Sebzeler", "Tahıllar", "Protein", "Süt Ürünleri", "Yağlar", "Favoriler"};
    private String selectedCategory = "Tümü";
    private String userEmail;

    private TextInputEditText searchEditText;
    private ChipGroup categoryChipGroup;
    private RecyclerView foodRecyclerView;
    private FloatingActionButton addFoodFab;

    private FoodAdapter foodAdapter;

    private List<Food> filteredFoods = new ArrayList<>();

    private FoodViewModel foodViewModel;

    public static FoodFragment newInstance(String userEmail) {
        FoodFragment fragment = new FoodFragment();
        Bundle args = new Bundle();
        args.putString("user_email", userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userEmail = getArguments().getString("user_email");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);
        allFoods.clear(); // Tekrar tekrar eklenmesin diye
        allFoods.add(new Food("Elma", "Meyveler", "Yüksek fiber, vitamin C", 52, false));
        allFoods.add(new Food("Muz", "Meyveler", "Potasyum, vitamin B6", 89, false));
        allFoods.add(new Food("Brokoli", "Sebzeler", "Vitamin K, C, folik asit", 34, false));
        allFoods.add(new Food("Havuç", "Sebzeler", "Beta karoten, vitamin A", 41, false));
        allFoods.add(new Food("Yulaf", "Tahıllar", "Fiber, protein, magnezyum", 389, false));
        allFoods.add(new Food("Quinoa", "Tahıllar", "Protein, fiber, demir", 368, false));
        allFoods.add(new Food("Tavuk Göğsü", "Protein", "Yüksek protein, az yağ", 165, false));
        allFoods.add(new Food("Somon", "Protein", "Omega-3, protein", 208, false));
        allFoods.add(new Food("Yoğurt", "Süt Ürünleri", "Probiyotik, kalsiyum", 59, false));
        allFoods.add(new Food("Süt", "Süt Ürünleri", "Kalsiyum, protein", 42, false));
        allFoods.add(new Food("Zeytinyağı", "Yağlar", "Tekli doymamış yağ", 884, false));
        allFoods.add(new Food("Avokado", "Yağlar", "Sağlıklı yağlar, fiber", 160, false));


        searchEditText = view.findViewById(R.id.search_edit_text);
        categoryChipGroup = view.findViewById(R.id.category_chip_group);
        foodRecyclerView = view.findViewById(R.id.food_recycler_view);
        addFoodFab = view.findViewById(R.id.add_food_fab);

        setupCategoryFilter();

        foodAdapter = new FoodAdapter(filteredFoods, new FoodAdapter.OnFoodActionListener() {
            @Override
            public void onFoodClick(Food food) {
                // Besin detaylarını göster
                Toast.makeText(getContext(), "Besin: " + food.name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFavoriteClick(Food food) {
                food.favorite = !food.favorite;
                foodViewModel.updateFood(food);
            }

            @Override
            public void onFoodLongClick(Food food, int position) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Besini sil")
                        .setMessage(food.name + " silinsin mi?")
                        .setPositiveButton("Evet", (dialog, which) -> {
                            foodViewModel.deleteFood(food);
                        })
                        .setNegativeButton("Hayır", null)
                        .show();
            }
        });

        foodRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        foodRecyclerView.setAdapter(foodAdapter);

        // ViewModel ve canlı veri
        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        foodViewModel.getFoodsForUser(userEmail).observe(getViewLifecycleOwner(), foods -> {
            allFoods.clear();
            allFoods.addAll(foods);
            filterFoods(searchEditText.getText().toString());
        });

        // Arama
        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFoods(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
            }
        });

        // Ekle FAB
        addFoodFab.setOnClickListener(v -> showAddFoodDialog());

        return view;
    }

    private void setupCategoryFilter() {
        categoryChipGroup.removeAllViews();
        for (String category : CATEGORIES) {
            Chip chip = new Chip(getContext());
            chip.setText(category);
            chip.setCheckable(true);
            chip.setChecked(category.equals("Tümü"));
            chip.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    for (int i = 0; i < categoryChipGroup.getChildCount(); i++) {
                        Chip otherChip = (Chip) categoryChipGroup.getChildAt(i);
                        if (!otherChip.equals(chip)) otherChip.setChecked(false);
                    }
                    selectedCategory = category;
                    filterFoods(searchEditText.getText().toString());
                }
            });
            categoryChipGroup.addView(chip);
        }
    }

    private void filterFoods(String query) {
        filteredFoods.clear();
        for (Food food : allFoods) {
            boolean matchesSearch = query.isEmpty() || food.name.toLowerCase().contains(query.toLowerCase());
            boolean matchesCategory = selectedCategory.equals("Tümü") || food.category.equals(selectedCategory);
            if (matchesSearch && matchesCategory) {
                filteredFoods.add(food);
            }
        }
        foodAdapter.notifyDataSetChanged();
    }

    private void showAddFoodDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_add_food, null);

        Spinner spinnerCategory = dialogView.findViewById(R.id.spinner_food_category);
        Spinner spinnerFood = dialogView.findViewById(R.id.spinner_food_name);
        EditText editCalories = dialogView.findViewById(R.id.edit_calories);
        EditText editDescription = dialogView.findViewById(R.id.edit_description);

        List<String> categories = new ArrayList<>(foodMap.keySet());
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Başlangıçta boş adapter ile başla
        ArrayAdapter<String> foodAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, new ArrayList<>());
        foodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFood.setAdapter(foodAdapter);

        // Kategori seçilince ilgili besinler gelsin
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categories.get(position);
                List<String> foods = foodMap.get(selectedCategory);
                foodAdapter.clear();
                if (foods != null) foodAdapter.addAll(foods);
                foodAdapter.notifyDataSetChanged();

                // Besin ve diğer alanları temizle
                spinnerFood.setSelection(0);
                editCalories.setText("");
                editDescription.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Besin seçilince kalori ve açıklama dolsun
        spinnerFood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFood = (String) spinnerFood.getSelectedItem();
                if (selectedFood != null) {
                    editCalories.setText(String.valueOf(caloriesMap.get(selectedFood)));
                    editDescription.setText(descriptionMap.get(selectedFood));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Yeni Besin Ekle")
                .setView(dialogView)
                .setPositiveButton("Ekle", (dialog, which) -> {
                    String selectedCategory = spinnerCategory.getSelectedItem() != null ? spinnerCategory.getSelectedItem().toString() : "";
                    String selectedFood = spinnerFood.getSelectedItem() != null ? spinnerFood.getSelectedItem().toString() : "";
                    String calories = editCalories.getText().toString();
                    String description = editDescription.getText().toString();

                    if (selectedCategory.isEmpty() || selectedFood.isEmpty()) {
                        Toast.makeText(getContext(), "Kategori ve besin seçmelisin!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Room'a ekleme
                    Food food = new Food(
                            selectedFood,            // name
                            selectedCategory,        // category
                            description,             // description
                            calories.isEmpty() ? 0 : Integer.parseInt(calories), // calories
                            false,                   // favorite
                            userEmail                // userEmail
                    );


                    foodViewModel.insertFood(food);
                })
                .setNegativeButton("İptal", null)
                .show();
    }
}


