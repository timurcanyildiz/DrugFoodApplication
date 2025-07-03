package com.example.drugfoodapplication.ui.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.example.drugfoodapplication.R;

import java.util.*;

public class InteractionsFragment extends Fragment {

    private static final String ARG_USER_EMAIL = "user_email";
    private String userEmail;

    private Spinner drugSpinner, foodSpinner;
    private Button checkInteractionBtn;
    private LinearLayout interactionsContainer;
    private TextView noInteractionText;

    // Kendi oluşturduğun ilaçlar (isim - etken madde)
    private final LinkedHashMap<String, String> drugMap = new LinkedHashMap<String, String>() {{
        put("Ventolin", "Salbutamol");
        put("Flixotide", "Fluticasone");
        put("Glucophage", "Metformin");
        put("Insulatard", "Insulin");
        put("Beloc", "Metoprolol");
        put("Aprovel", "Irbesartan");
        put("Claritin", "Loratadine");
        put("Zyrtec", "Cetirizine");
    }};

    // Besinler (isim - açıklama)
    private final LinkedHashMap<String, String> foodMap = new LinkedHashMap<String, String>() {{
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

    // Örnek etkileşim veritabanı (ilaç-isim -> besin-isim -> etkileşim)
    private final Map<String, Map<String, InteractionResult>> interactionDb = new HashMap<String, Map<String, InteractionResult>>() {{
        // VENTOLIN
        put("Ventolin", new HashMap<String, InteractionResult>() {{
            put("Yoğurt", new InteractionResult("DÜŞÜK", "Yoğurt ile Ventolin arasında bilinen bir olumsuz etkileşim yoktur.", "🟢", "Güvenli"));
            put("Elma", new InteractionResult("DÜŞÜK", "Ventolin ile elma arasında önemli bir etkileşim bildirilmemiştir.", "🟢", "Güvenli"));
            put("Brokoli", new InteractionResult("DÜŞÜK", "Brokoli ile Ventolin birlikte kullanılabilir.", "🟢", "Güvenli"));
        }});
        // FLIXOTIDE
        put("Flixotide", new HashMap<String, InteractionResult>() {{
            put("Süt", new InteractionResult("ORTA", "Süt, bazı astım ilaçlarının emilimini azaltabilir. Tüketim miktarına dikkat edin.", "🟡", "Dikkatli Olun"));
            put("Avokado", new InteractionResult("DÜŞÜK", "Flixotide ile avokado arasında bilinen ciddi bir etkileşim yoktur.", "🟢", "Güvenli"));
        }});
        // GLUCOPHAGE
        put("Glucophage", new HashMap<String, InteractionResult>() {{
            put("Yulaf", new InteractionResult("DÜŞÜK", "Yulaf ile Glucophage güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Muz", new InteractionResult("DÜŞÜK", "Muzun Glucophage ile önemli bir etkileşimi yoktur.", "🟢", "Güvenli"));
        }});
        // INSULATARD
        put("Insulatard", new HashMap<String, InteractionResult>() {{
            put("Yoğurt", new InteractionResult("DÜŞÜK", "Yoğurt ile insülin arasında bilinen bir olumsuz etkileşim yoktur.", "🟢", "Güvenli"));
            put("Havuç", new InteractionResult("DÜŞÜK", "Havuç ve insülin birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Muz", new InteractionResult("ORTA", "Muzun şeker oranı yüksektir. Dikkatli tüketin ve kan şekerinizi takip edin.", "🟡", "Dikkatli Olun"));
        }});
        // BELOC
        put("Beloc", new HashMap<String, InteractionResult>() {{
            put("Zeytinyağı", new InteractionResult("DÜŞÜK", "Beloc ile zeytinyağı arasında bilinen bir etkileşim yoktur.", "🟢", "Güvenli"));
            put("Avokado", new InteractionResult("DÜŞÜK", "Beloc ile avokado güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Somon", new InteractionResult("DÜŞÜK", "Somon ile Beloc arasında önemli bir etkileşim yoktur.", "🟢", "Güvenli"));
        }});
        // APROVEL
        put("Aprovel", new HashMap<String, InteractionResult>() {{
            put("Muz", new InteractionResult("ORTA", "Muz, potasyum içerir. Aprovel ile birlikte fazla muz tüketimi potasyum düzeyini yükseltebilir.", "🟡", "Dikkatli Olun"));
            put("Yulaf", new InteractionResult("DÜŞÜK", "Aprovel ile yulaf arasında bilinen bir etkileşim yoktur.", "🟢", "Güvenli"));
        }});
        // CLARITIN
        put("Claritin", new HashMap<String, InteractionResult>() {{
            put("Süt", new InteractionResult("DÜŞÜK", "Süt ile Claritin arasında önemli bir etkileşim yoktur.", "🟢", "Güvenli"));
            put("Yoğurt", new InteractionResult("DÜŞÜK", "Yoğurt ve Claritin birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Brokoli", new InteractionResult("DÜŞÜK", "Claritin ve brokoli arasında etkileşim yoktur.", "🟢", "Güvenli"));
        }});
        // ZYRTEC
        put("Zyrtec", new HashMap<String, InteractionResult>() {{
            put("Avokado", new InteractionResult("DÜŞÜK", "Avokado ile Zyrtec arasında bilinen bir etkileşim yoktur.", "🟢", "Güvenli"));
            put("Süt", new InteractionResult("DÜŞÜK", "Süt ile Zyrtec güvenle kullanılabilir.", "🟢", "Güvenli"));
            put("Havuç", new InteractionResult("DÜŞÜK", "Zyrtec ve havuç arasında etkileşim yoktur.", "🟢", "Güvenli"));
        }});
    }};

    // Kendi veri sınıfımız (bu Fragmnet içinde sadece görsel ve test için)
    private static class InteractionResult {
        String riskLevel, description, riskIcon, recommendation;
        InteractionResult(String riskLevel, String description, String riskIcon, String recommendation) {
            this.riskLevel = riskLevel;
            this.description = description;
            this.riskIcon = riskIcon;
            this.recommendation = recommendation;
        }
    }

    public static InteractionsFragment newInstance(String userEmail) {
        InteractionsFragment fragment = new InteractionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_EMAIL, userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userEmail = getArguments().getString(ARG_USER_EMAIL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interactions, container, false);

        // XML'den view'ları al
        drugSpinner = view.findViewById(R.id.drug_spinner);
        foodSpinner = view.findViewById(R.id.food_spinner);
        checkInteractionBtn = view.findViewById(R.id.check_interaction_btn);
        interactionsContainer = view.findViewById(R.id.interactions_container);
        noInteractionText = view.findViewById(R.id.no_interaction_text);

        // Spinner'lara veri yükle
        List<String> drugList = new ArrayList<>();
        drugList.add("İlaç seçin...");
        drugList.addAll(drugMap.keySet());

        List<String> foodList = new ArrayList<>();
        foodList.add("Besin seçin...");
        foodList.addAll(foodMap.keySet());

        ArrayAdapter<String> drugAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, drugList);
        ArrayAdapter<String> foodAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, foodList);

        drugSpinner.setAdapter(drugAdapter);
        foodSpinner.setAdapter(foodAdapter);

        // Buton ilk başta kapalı
        checkInteractionBtn.setEnabled(false);

        // Spinner dinleyicileri
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                checkInteractionBtn.setEnabled(
                        drugSpinner.getSelectedItemPosition() > 0 &&
                                foodSpinner.getSelectedItemPosition() > 0
                );
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        };
        drugSpinner.setOnItemSelectedListener(spinnerListener);
        foodSpinner.setOnItemSelectedListener(spinnerListener);

        checkInteractionBtn.setOnClickListener(v -> checkInteraction());

        return view;
    }

    private void checkInteraction() {
        int drugPos = drugSpinner.getSelectedItemPosition();
        int foodPos = foodSpinner.getSelectedItemPosition();
        if (drugPos == 0 || foodPos == 0) {
            Toast.makeText(getContext(), "Lütfen ilaç ve besin seçin.", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedDrug = (String) drugSpinner.getSelectedItem();
        String selectedFood = (String) foodSpinner.getSelectedItem();

        InteractionResult result = null;
        if (interactionDb.containsKey(selectedDrug)) {
            Map<String, InteractionResult> foodInteractions = interactionDb.get(selectedDrug);
            if (foodInteractions.containsKey(selectedFood)) {
                result = foodInteractions.get(selectedFood);
            }
        }

        showInteractionResult(selectedDrug, selectedFood, result);
    }

    private void showInteractionResult(String drug, String food, InteractionResult result) {
        interactionsContainer.removeAllViews();

        if (result != null) {
            noInteractionText.setVisibility(View.GONE);

            CardView cardView = new CardView(getContext());
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(16, 16, 16, 16);
            cardView.setLayoutParams(cardParams);
            cardView.setCardElevation(8);
            cardView.setRadius(12);

            LinearLayout contentLayout = new LinearLayout(getContext());
            contentLayout.setOrientation(LinearLayout.VERTICAL);
            contentLayout.setPadding(24, 24, 24, 24);

            TextView title = new TextView(getContext());
            title.setText(String.format("%s ↔ %s", drug, food));
            title.setTextSize(18);
            title.setTextColor(getResources().getColor(android.R.color.black));
            title.setTypeface(null, android.graphics.Typeface.BOLD);
            contentLayout.addView(title);

            // Risk Seviyesi
            LinearLayout riskLayout = new LinearLayout(getContext());
            riskLayout.setOrientation(LinearLayout.HORIZONTAL);
            riskLayout.setPadding(0, 12, 0, 12);

            TextView riskIcon = new TextView(getContext());
            riskIcon.setText(result.riskIcon);
            riskIcon.setTextSize(20);
            riskIcon.setPadding(0, 0, 12, 0);

            TextView riskText = new TextView(getContext());
            riskText.setText(String.format("Risk: %s - %s", result.riskLevel, result.recommendation));
            riskText.setTextSize(14);
            riskText.setTextColor(getRiskColor(result.riskLevel));
            riskText.setTypeface(null, Typeface.BOLD);

            riskLayout.addView(riskIcon);
            riskLayout.addView(riskText);
            contentLayout.addView(riskLayout);

            TextView description = new TextView(getContext());
            description.setText(result.description);
            description.setTextSize(14);
            description.setTextColor(getResources().getColor(android.R.color.black));
            description.setPadding(0, 8, 0, 0);
            contentLayout.addView(description);

            cardView.addView(contentLayout);
            interactionsContainer.addView(cardView);

        } else {
            noInteractionText.setVisibility(View.VISIBLE);
            noInteractionText.setText(String.format(
                    "%s ile %s arasında bilinen bir etkileşim yok. Yine de doktorunuza danışınız.",
                    drug, food
            ));
        }
    }

    private int getRiskColor(String riskLevel) {
        switch (riskLevel) {
            case "YÜKSEK":
                return getResources().getColor(android.R.color.holo_red_dark);
            case "ORTA":
                return getResources().getColor(android.R.color.holo_orange_dark);
            case "DÜŞÜK":
                return getResources().getColor(android.R.color.holo_green_dark);
            default:
                return getResources().getColor(android.R.color.black);
        }
    }
}
