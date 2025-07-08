// FoodFragment.java
package com.example.drugfoodapplication.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drugfoodapplication.R;
import com.example.drugfoodapplication.ui.adapter.FoodAdapter;
import com.example.drugfoodapplication.data.database.AppDatabase;
import com.example.drugfoodapplication.data.dao.FoodDao;
import com.example.drugfoodapplication.data.entity.Food;
import com.example.drugfoodapplication.ui.viewmodel.FoodViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

public class FoodFragment extends Fragment {

    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddFood;
    private TextView totalCaloriesText;

    private FoodAdapter foodAdapter;
    private FoodViewModel foodViewModel;
    private String currentCategory = "Tümü";

    // Besin kategorileri ve alt kategorileri
    private HashMap<String, List<String>> foodCategories;
    private HashMap<String, String> foodDescriptions;
    private HashMap<String, Integer> foodCalories;
    public static FoodFragment newInstance(String userEmail) {
        FoodFragment fragment = new FoodFragment();
        Bundle args = new Bundle();
        args.putString("user_email", userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewModel initialization
        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);

        initializeFoodData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        initViews(view);
        setupTabLayout();
        setupRecyclerView();
        setupFab();
        observeViewModel();

        return view;
    }

    private void initViews(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        fabAddFood = view.findViewById(R.id.fabAddFood);
        totalCaloriesText = view.findViewById(R.id.totalCaloriesText);
    }

    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Tümü"));
        tabLayout.addTab(tabLayout.newTab().setText("Meyveler"));
        tabLayout.addTab(tabLayout.newTab().setText("Sebzeler"));
        tabLayout.addTab(tabLayout.newTab().setText("Tahıllar"));
        tabLayout.addTab(tabLayout.newTab().setText("Protein"));
        tabLayout.addTab(tabLayout.newTab().setText("Süt Ürünleri"));
        tabLayout.addTab(tabLayout.newTab().setText("Yağlar"));
        tabLayout.addTab(tabLayout.newTab().setText("Favoriler"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentCategory = tab.getText().toString();
                foodViewModel.setCategory(currentCategory);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupRecyclerView() {
        foodAdapter = new FoodAdapter(new ArrayList<>(), new FoodAdapter.OnFoodClickListener() {
            @Override
            public void onFavoriteClick(Food food) {
                foodViewModel.toggleFavorite(food);
            }

            @Override
            public void onDeleteClick(Food food) {
                foodViewModel.deleteFood(food);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(foodAdapter);
    }

    private void observeViewModel() {
        foodViewModel.getFilteredFoods().observe(getViewLifecycleOwner(), foods -> {
            if (foods != null) {
                foodAdapter.updateFoods(foods);
            }
        });

        foodViewModel.getTotalCalories().observe(getViewLifecycleOwner(), totalCalories -> {
            if (totalCalories != null) {
                totalCaloriesText.setText("Toplam Kalori: " + totalCalories + " kcal");
            }
        });
    }

    private void setupFab() {
        fabAddFood.setOnClickListener(v -> showAddFoodDialog());
    }

    private void showAddFoodDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_add_food);

        Spinner categorySpinner = dialog.findViewById(R.id.categorySpinner);
        Spinner foodSpinner = dialog.findViewById(R.id.foodSpinner);
        Spinner portionSpinner = dialog.findViewById(R.id.portionSpinner);
        TextView descriptionText = dialog.findViewById(R.id.descriptionText);
        TextView caloriesText = dialog.findViewById(R.id.caloriesText);
        Button addButton = dialog.findViewById(R.id.addButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);

        // Kategori spinner setup
        List<String> categories = new ArrayList<>(foodCategories.keySet());
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Porsiyon spinner setup
        String[] portions = {"1 porsiyon", "0.5 porsiyon", "1.5 porsiyon", "2 porsiyon"};
        ArrayAdapter<String> portionAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, portions);
        portionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        portionSpinner.setAdapter(portionAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categories.get(position);
                List<String> foods = foodCategories.get(selectedCategory);

                ArrayAdapter<String> foodAdapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, foods);
                foodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                foodSpinner.setAdapter(foodAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        foodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFood = (String) parent.getItemAtPosition(position);
                String description = foodDescriptions.get(selectedFood);
                Integer calories = foodCalories.get(selectedFood);

                descriptionText.setText(description != null ? description : "Açıklama bulunamadı");
                updateCaloriesText(caloriesText, calories, portionSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        portionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (foodSpinner.getSelectedItem() != null) {
                    String selectedFood = foodSpinner.getSelectedItem().toString();
                    Integer calories = foodCalories.get(selectedFood);
                    String portion = portionSpinner.getSelectedItem().toString();
                    updateCaloriesText(caloriesText, calories, portion);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        addButton.setOnClickListener(v -> {
            String category = categorySpinner.getSelectedItem().toString();
            String foodName = foodSpinner.getSelectedItem().toString();
            String portion = portionSpinner.getSelectedItem().toString();
            String description = descriptionText.getText().toString();

            Integer baseCalories = foodCalories.get(foodName);
            int totalCalories = calculateTotalCalories(baseCalories, portion);

            String userEmail = foodViewModel.getUserEmail();
            Food food = new Food(foodName, category, description, portion, totalCalories, userEmail, false);

            foodViewModel.insertFood(food);
            Toast.makeText(getContext(), "Besin eklendi", Toast.LENGTH_SHORT).show();

            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void updateCaloriesText(TextView caloriesText, Integer baseCalories, String portion) {
        if (baseCalories != null) {
            int totalCalories = calculateTotalCalories(baseCalories, portion);
            caloriesText.setText(totalCalories + " kalori");
        }
    }

    private int calculateTotalCalories(Integer baseCalories, String portion) {
        if (baseCalories == null) return 0;

        double multiplier = 1.0;
        if (portion.contains("0.5")) {
            multiplier = 0.5;
        } else if (portion.contains("1.5")) {
            multiplier = 1.5;
        } else if (portion.contains("2")) {
            multiplier = 2.0;
        }

        return (int) (baseCalories * multiplier);
    }

    private void initializeFoodData() {
        foodCategories = new HashMap<>();
        foodDescriptions = new HashMap<>();
        foodCalories = new HashMap<>();

        // Meyveler
        List<String> fruits = new ArrayList<>();
        fruits.add("Elma");
        fruits.add("Muz");
        fruits.add("Portakal");
        fruits.add("Üzüm");
        foodCategories.put("Meyveler", fruits);

        // Sebzeler
        List<String> vegetables = new ArrayList<>();
        vegetables.add("Brokoli");
        vegetables.add("Havuç");
        vegetables.add("Domates");
        vegetables.add("Salatalık");
        foodCategories.put("Sebzeler", vegetables);

        // Tahıllar
        List<String> grains = new ArrayList<>();
        grains.add("Pirinç");
        grains.add("Bulgur");
        grains.add("Makarna");
        grains.add("Ekmek");
        foodCategories.put("Tahıllar", grains);

        // Protein
        List<String> proteins = new ArrayList<>();
        proteins.add("Tavuk Göğsü");
        proteins.add("Somon");
        proteins.add("Yumurta");
        proteins.add("Kırmızı Et");
        foodCategories.put("Protein", proteins);

        // Süt Ürünleri
        List<String> dairy = new ArrayList<>();
        dairy.add("Süt");
        dairy.add("Yoğurt");
        dairy.add("Peynir");
        dairy.add("Tereyağı");
        foodCategories.put("Süt Ürünleri", dairy);

        // Yağlar
        List<String> oils = new ArrayList<>();
        oils.add("Zeytinyağı");
        oils.add("Ayçiçek Yağı");
        oils.add("Fındık");
        oils.add("Badem");
        foodCategories.put("Yağlar", oils);

        // Açıklamalar
        foodDescriptions.put("Elma", "Yüksek lif, vitamin C");
        foodDescriptions.put("Muz", "Potasyum, B6 vitamini");
        foodDescriptions.put("Portakal", "Vitamin C, folat");
        foodDescriptions.put("Üzüm", "Antioksidan, doğal şeker");

        foodDescriptions.put("Brokoli", "Vitamin K, C, lif");
        foodDescriptions.put("Havuç", "Beta karoten, vitamin A");
        foodDescriptions.put("Domates", "Liken, vitamin C");
        foodDescriptions.put("Salatalık", "Düşük kalori, yüksek su");

        foodDescriptions.put("Pirinç", "Karbonhidrat, enerji");
        foodDescriptions.put("Bulgur", "Lif, protein, B vitamini");
        foodDescriptions.put("Makarna", "Karbonhidrat, enerji");
        foodDescriptions.put("Ekmek", "Karbonhidrat, B vitamini");

        foodDescriptions.put("Tavuk Göğsü", "Yüksek protein, az yağ");
        foodDescriptions.put("Somon", "Omega-3, protein");
        foodDescriptions.put("Yumurta", "Tam protein, kolin");
        foodDescriptions.put("Kırmızı Et", "Protein, demir, B12");

        foodDescriptions.put("Süt", "Kalsiyum, protein");
        foodDescriptions.put("Yoğurt", "Probiyotik, kalsiyum");
        foodDescriptions.put("Peynir", "Kalsiyum, protein");
        foodDescriptions.put("Tereyağı", "Yağ, vitamin A");

        foodDescriptions.put("Zeytinyağı", "Tekli doymamış yağ");
        foodDescriptions.put("Ayçiçek Yağı", "Vitamin E, çokli doymamış yağ");
        foodDescriptions.put("Fındık", "Protein, sağlıklı yağ");
        foodDescriptions.put("Badem", "Vitamin E, magnezyum");

        // Kalori değerleri (100g için)
        foodCalories.put("Elma", 52);
        foodCalories.put("Muz", 89);
        foodCalories.put("Portakal", 47);
        foodCalories.put("Üzüm", 62);

        foodCalories.put("Brokoli", 34);
        foodCalories.put("Havuç", 41);
        foodCalories.put("Domates", 18);
        foodCalories.put("Salatalık", 16);

        foodCalories.put("Pirinç", 130);
        foodCalories.put("Bulgur", 83);
        foodCalories.put("Makarna", 131);
        foodCalories.put("Ekmek", 265);

        foodCalories.put("Tavuk Göğsü", 165);
        foodCalories.put("Somon", 208);
        foodCalories.put("Yumurta", 155);
        foodCalories.put("Kırmızı Et", 250);

        foodCalories.put("Süt", 42);
        foodCalories.put("Yoğurt", 59);
        foodCalories.put("Peynir", 113);
        foodCalories.put("Tereyağı", 717);

        foodCalories.put("Zeytinyağı", 884);
        foodCalories.put("Ayçiçek Yağı", 884);
        foodCalories.put("Fındık", 628);
        foodCalories.put("Badem", 579);
    }
}