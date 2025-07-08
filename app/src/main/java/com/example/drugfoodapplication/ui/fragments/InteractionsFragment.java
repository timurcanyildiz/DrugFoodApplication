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

    // Besinler (isim - açıklama) - Food Fragment'teki tüm besinler
    private final LinkedHashMap<String, String> foodMap = new LinkedHashMap<String, String>() {{
        // Meyveler
        put("Elma", "Yüksek lif, vitamin C");
        put("Muz", "Potasyum, B6 vitamini");
        put("Portakal", "Vitamin C, folat");
        put("Üzüm", "Antioksidan, doğal şeker");

        // Sebzeler
        put("Brokoli", "Vitamin K, C, lif");
        put("Havuç", "Beta karoten, vitamin A");
        put("Domates", "Liken, vitamin C");
        put("Salatalık", "Düşük kalori, yüksek su");

        // Tahıllar
        put("Pirinç", "Karbonhidrat, enerji");
        put("Bulgur", "Lif, protein, B vitamini");
        put("Makarna", "Karbonhidrat, enerji");
        put("Ekmek", "Karbonhidrat, B vitamini");

        // Protein
        put("Tavuk Göğsü", "Yüksek protein, az yağ");
        put("Somon", "Omega-3, protein");
        put("Yumurta", "Tam protein, kolin");
        put("Kırmızı Et", "Protein, demir, B12");

        // Süt Ürünleri
        put("Süt", "Kalsiyum, protein");
        put("Yoğurt", "Probiyotik, kalsiyum");
        put("Peynir", "Kalsiyum, protein");
        put("Tereyağı", "Yağ, vitamin A");

        // Yağlar
        put("Zeytinyağı", "Tekli doymamış yağ");
        put("Ayçiçek Yağı", "Vitamin E, çokli doymamış yağ");
        put("Fındık", "Protein, sağlıklı yağ");
        put("Badem", "Vitamin E, magnezyum");
    }};

    // Örnek etkileşim veritabanı (ilaç-isim -> besin-isim -> etkileşim)
    private final Map<String, Map<String, InteractionResult>> interactionDb = new HashMap<String, Map<String, InteractionResult>>() {{

        // ====== VENTOLIN (Salbutamol) ======
        put("Ventolin", new HashMap<String, InteractionResult>() {{
            // Meyveler
            put("Elma", new InteractionResult("DÜŞÜK", "Ventolin ile elma arasında önemli bir etkileşim bildirilmemiştir.", "🟢", "Güvenli"));
            put("Muz", new InteractionResult("DÜŞÜK", "Muz ile Ventolin güvenle kullanılabilir.", "🟢", "Güvenli"));
            put("Portakal", new InteractionResult("DÜŞÜK", "Portakal ve Ventolin arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Üzüm", new InteractionResult("DÜŞÜK", "Üzüm ile Ventolin birlikte tüketilebilir.", "🟢", "Güvenli"));

            // Sebzeler
            put("Brokoli", new InteractionResult("DÜŞÜK", "Brokoli ile Ventolin birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Havuç", new InteractionResult("DÜŞÜK", "Havuç ve Ventolin arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Domates", new InteractionResult("DÜŞÜK", "Domates ile Ventolin güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Salatalık", new InteractionResult("DÜŞÜK", "Salatalık ve Ventolin arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Tahıllar
            put("Pirinç", new InteractionResult("DÜŞÜK", "Pirinç ile Ventolin arasında bilinen etkileşim yoktur.", "🟢", "Güvenli"));
            put("Bulgur", new InteractionResult("DÜŞÜK", "Bulgur ve Ventolin birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Makarna", new InteractionResult("DÜŞÜK", "Makarna ile Ventolin güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Ekmek", new InteractionResult("DÜŞÜK", "Ekmek ve Ventolin arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Protein
            put("Tavuk Göğsü", new InteractionResult("DÜŞÜK", "Tavuk göğsü ile Ventolin arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Somon", new InteractionResult("DÜŞÜK", "Somon ve Ventolin birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Yumurta", new InteractionResult("DÜŞÜK", "Yumurta ile Ventolin güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Kırmızı Et", new InteractionResult("DÜŞÜK", "Kırmızı et ve Ventolin arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Süt Ürünleri
            put("Süt", new InteractionResult("ORTA", "Süt, Ventolin'in emilimini hafifçe etkileyebilir. İlaç ile süt arasında 1 saat bekleyin.", "🟡", "Dikkatli Olun"));
            put("Yoğurt", new InteractionResult("DÜŞÜK", "Yoğurt ile Ventolin arasında bilinen bir olumsuz etkileşim yoktur.", "🟢", "Güvenli"));
            put("Peynir", new InteractionResult("DÜŞÜK", "Peynir ve Ventolin birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Tereyağı", new InteractionResult("DÜŞÜK", "Tereyağı ile Ventolin arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Yağlar
            put("Zeytinyağı", new InteractionResult("DÜŞÜK", "Zeytinyağı ile Ventolin arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Ayçiçek Yağı", new InteractionResult("DÜŞÜK", "Ayçiçek yağı ve Ventolin birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Fındık", new InteractionResult("DÜŞÜK", "Fındık ile Ventolin güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Badem", new InteractionResult("DÜŞÜK", "Badem ve Ventolin arasında etkileşim yoktur.", "🟢", "Güvenli"));
        }});

        // ====== FLIXOTIDE (Fluticasone) ======
        put("Flixotide", new HashMap<String, InteractionResult>() {{
            // Meyveler
            put("Elma", new InteractionResult("DÜŞÜK", "Elma ile Flixotide arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Muz", new InteractionResult("DÜŞÜK", "Muz ve Flixotide birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Portakal", new InteractionResult("ORTA", "Portakal C vitamini nedeniyle kortizon etkisini hafifçe artırabilir. Aşırı tüketimden kaçının.", "🟡", "Dikkatli Olun"));
            put("Üzüm", new InteractionResult("DÜŞÜK", "Üzüm ile Flixotide güvenle tüketilebilir.", "🟢", "Güvenli"));

            // Sebzeler
            put("Brokoli", new InteractionResult("DÜŞÜK", "Brokoli ve Flixotide arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Havuç", new InteractionResult("DÜŞÜK", "Havuç ile Flixotide birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Domates", new InteractionResult("DÜŞÜK", "Domates ve Flixotide arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Salatalık", new InteractionResult("DÜŞÜK", "Salatalık ile Flixotide güvenle tüketilebilir.", "🟢", "Güvenli"));

            // Tahıllar
            put("Pirinç", new InteractionResult("DÜŞÜK", "Pirinç ile Flixotide arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Bulgur", new InteractionResult("DÜŞÜK", "Bulgur ve Flixotide birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Makarna", new InteractionResult("DÜŞÜK", "Makarna ile Flixotide güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Ekmek", new InteractionResult("DÜŞÜK", "Ekmek ve Flixotide arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Protein
            put("Tavuk Göğsü", new InteractionResult("DÜŞÜK", "Tavuk göğsü ile Flixotide arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Somon", new InteractionResult("DÜŞÜK", "Somon ve Flixotide birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Yumurta", new InteractionResult("DÜŞÜK", "Yumurta ile Flixotide güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Kırmızı Et", new InteractionResult("DÜŞÜK", "Kırmızı et ve Flixotide arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Süt Ürünleri
            put("Süt", new InteractionResult("ORTA", "Süt, bazı astım ilaçlarının emilimini azaltabilir. Tüketim miktarına dikkat edin.", "🟡", "Dikkatli Olun"));
            put("Yoğurt", new InteractionResult("ORTA", "Yoğurt kalsiyumu nedeniyle emilimi etkileyebilir. İlaç ile arasında 2 saat bekleyin.", "🟡", "Dikkatli Olun"));
            put("Peynir", new InteractionResult("ORTA", "Peynir yüksek kalsiyum içeriği nedeniyle dikkatli tüketilmelidir.", "🟡", "Dikkatli Olun"));
            put("Tereyağı", new InteractionResult("DÜŞÜK", "Tereyağı ile Flixotide arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Yağlar
            put("Zeytinyağı", new InteractionResult("DÜŞÜK", "Zeytinyağı ile Flixotide arasında bilinen ciddi bir etkileşim yoktur.", "🟢", "Güvenli"));
            put("Ayçiçek Yağı", new InteractionResult("DÜŞÜK", "Ayçiçek yağı ve Flixotide birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Fındık", new InteractionResult("DÜŞÜK", "Fındık ile Flixotide güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Badem", new InteractionResult("DÜŞÜK", "Badem ve Flixotide arasında etkileşim yoktur.", "🟢", "Güvenli"));
        }});

        // ====== GLUCOPHAGE (Metformin) ======
        put("Glucophage", new HashMap<String, InteractionResult>() {{
            // Meyveler
            put("Elma", new InteractionResult("DÜŞÜK", "Elma lifli yapısı nedeniyle kan şekerini dengeler. Glucophage ile uyumludur.", "🟢", "Güvenli"));
            put("Muz", new InteractionResult("ORTA", "Muz şeker içeriği yüksektir. Kan şekerinizi takip edin ve porsiyon kontrolü yapın.", "🟡", "Dikkatli Olun"));
            put("Portakal", new InteractionResult("DÜŞÜK", "Portakal C vitamini ve lif içeriği ile Glucophage ile uyumludur.", "🟢", "Güvenli"));
            put("Üzüm", new InteractionResult("ORTA", "Üzüm doğal şeker içerir. Kan şeker düzeyinizi kontrol edin.", "🟡", "Dikkatli Olun"));

            // Sebzeler
            put("Brokoli", new InteractionResult("DÜŞÜK", "Brokoli düşük karbonhidrat ve yüksek lif içerir. Glucophage ile idealdir.", "🟢", "Güvenli"));
            put("Havuç", new InteractionResult("DÜŞÜK", "Havuç beta karoten içerir ve Glucophage ile güvenlidir.", "🟢", "Güvenli"));
            put("Domates", new InteractionResult("DÜŞÜK", "Domates düşük şeker içerir ve Glucophage ile uyumludur.", "🟢", "Güvenli"));
            put("Salatalık", new InteractionResult("DÜŞÜK", "Salatalık çok düşük karbonhidrat içerir. Glucophage ile idealdir.", "🟢", "Güvenli"));

            // Tahıllar
            put("Pirinç", new InteractionResult("ORTA", "Pirinç yüksek karbonhidrat içerir. Porsiyon kontrolü yapın ve kan şekerinizi takip edin.", "🟡", "Dikkatli Olun"));
            put("Bulgur", new InteractionResult("DÜŞÜK", "Bulgur lif açısından zengindir ve Glucophage ile uyumludur.", "🟢", "Güvenli"));
            put("Makarna", new InteractionResult("ORTA", "Makarna yüksek karbonhidrat içerir. Tam tahıl tercih edin ve porsiyon kontrolü yapın.", "🟡", "Dikkatli Olun"));
            put("Ekmek", new InteractionResult("ORTA", "Ekmek kan şekerini yükseltebilir. Tam tahıl ekmek tercih edin.", "🟡", "Dikkatli Olun"));

            // Protein
            put("Tavuk Göğsü", new InteractionResult("DÜŞÜK", "Tavuk göğsü protein açısından zengindir ve Glucophage ile idealdir.", "🟢", "Güvenli"));
            put("Somon", new InteractionResult("DÜŞÜK", "Somon omega-3 ve protein içerir. Glucophage ile mükemmel uyumdadır.", "🟢", "Güvenli"));
            put("Yumurta", new InteractionResult("DÜŞÜK", "Yumurta yüksek protein, düşük karbonhidrat içerir. Glucophage ile idealdir.", "🟢", "Güvenli"));
            put("Kırmızı Et", new InteractionResult("DÜŞÜK", "Kırmızı et protein açısından zengindir ve Glucophage ile uyumludur.", "🟢", "Güvenli"));

            // Süt Ürünleri
            put("Süt", new InteractionResult("DÜŞÜK", "Süt protein ve kalsiyum içerir. Glucophage ile güvenlidir.", "🟢", "Güvenli"));
            put("Yoğurt", new InteractionResult("DÜŞÜK", "Yoğurt probiyotik ve protein içerir. Glucophage ile idealdir.", "🟢", "Güvenli"));
            put("Peynir", new InteractionResult("DÜŞÜK", "Peynir yüksek protein, düşük karbonhidrat içerir. Glucophage ile uyumludur.", "🟢", "Güvenli"));
            put("Tereyağı", new InteractionResult("DÜŞÜK", "Tereyağı karbonhidrat içermez ve Glucophage ile güvenlidir.", "🟢", "Güvenli"));

            // Yağlar
            put("Zeytinyağı", new InteractionResult("DÜŞÜK", "Zeytinyağı sağlıklı yağ içerir ve Glucophage ile mükemmel uyumdadır.", "🟢", "Güvenli"));
            put("Ayçiçek Yağı", new InteractionResult("DÜŞÜK", "Ayçiçek yağı vitamin E içerir ve Glucophage ile uyumludur.", "🟢", "Güvenli"));
            put("Fındık", new InteractionResult("DÜŞÜK", "Fındık sağlıklı yağ ve protein içerir. Glucophage ile güvenlidir.", "🟢", "Güvenli"));
            put("Badem", new InteractionResult("DÜŞÜK", "Badem protein ve sağlıklı yağ içerir. Glucophage ile idealdir.", "🟢", "Güvenli"));
        }});

        // ====== INSULATARD (Insulin) ======
        put("Insulatard", new HashMap<String, InteractionResult>() {{
            // Meyveler
            put("Elma", new InteractionResult("DÜŞÜK", "Elma lif içeriği nedeniyle kan şekerini yavaş yükseltir. İnsülin ile uyumludur.", "🟢", "Güvenli"));
            put("Muz", new InteractionResult("YÜKSEK", "Muz yüksek şeker içerir. İnsülin dozunuzu ayarlayın ve kan şekerinizi yakından takip edin.", "🔴", "Çok Dikkatli Olun"));
            put("Portakal", new InteractionResult("ORTA", "Portakal orta derecede şeker içerir. Porsiyon kontrolü yapın ve kan şekerinizi takip edin.", "🟡", "Dikkatli Olun"));
            put("Üzüm", new InteractionResult("YÜKSEK", "Üzüm yüksek doğal şeker içerir. İnsülin dozunu ayarlayın ve sık kontrol yapın.", "🔴", "Çok Dikkatli Olun"));

            // Sebzeler
            put("Brokoli", new InteractionResult("DÜŞÜK", "Brokoli çok düşük karbonhidrat içerir ve insülin ile idealdir.", "🟢", "Güvenli"));
            put("Havuç", new InteractionResult("DÜŞÜK", "Havuç düşük glisemik indekse sahiptir ve insülin ile güvenlidir.", "🟢", "Güvenli"));
            put("Domates", new InteractionResult("DÜŞÜK", "Domates düşük şeker içerir ve insülin ile uyumludur.", "🟢", "Güvenli"));
            put("Salatalık", new InteractionResult("DÜŞÜK", "Salatalık minimal karbonhidrat içerir. İnsülin ile mükemmeldir.", "🟢", "Güvenli"));

            // Tahıllar
            put("Pirinç", new InteractionResult("YÜKSEK", "Pirinç yüksek karbonhidrat içerir. İnsülin dozunu ayarlayın ve kan şekerinizi sık kontrol edin.", "🔴", "Çok Dikkatli Olun"));
            put("Bulgur", new InteractionResult("ORTA", "Bulgur kompleks karbonhidrat içerir. Porsiyon kontrolü yapın ve kan şekerinizi takip edin.", "🟡", "Dikkatli Olun"));
            put("Makarna", new InteractionResult("YÜKSEK", "Makarna yüksek karbonhidrat içerir. İnsülin dozunu ayarlayın ve sık kontrol yapın.", "🔴", "Çok Dikkatli Olun"));
            put("Ekmek", new InteractionResult("YÜKSEK", "Ekmek kan şekerini hızla yükseltir. İnsülin dozunu ayarlayın ve yakın takip yapın.", "🔴", "Çok Dikkatli Olun"));

            // Protein
            put("Tavuk Göğsü", new InteractionResult("DÜŞÜK", "Tavuk göğsü kan şekerini etkilemez ve insülin ile güvenlidir.", "🟢", "Güvenli"));
            put("Somon", new InteractionResult("DÜŞÜK", "Somon protein ve omega-3 içerir. İnsülin ile mükemmel uyumdadır.", "🟢", "Güvenli"));
            put("Yumurta", new InteractionResult("DÜŞÜK", "Yumurta kan şekerini etkilemez ve insülin ile idealdir.", "🟢", "Güvenli"));
            put("Kırmızı Et", new InteractionResult("DÜŞÜK", "Kırmızı et kan şekerini etkilemez ve insülin ile güvenlidir.", "🟢", "Güvenli"));

            // Süt Ürünleri
            put("Süt", new InteractionResult("ORTA", "Süt laktoz içerir. Kan şekerinizi kontrol edin ve porsiyon sınırlaması yapın.", "🟡", "Dikkatli Olun"));
            put("Yoğurt", new InteractionResult("DÜŞÜK", "Şekersiz yoğurt probiyotik içerir ve insülin ile uyumludur.", "🟢", "Güvenli"));
            put("Peynir", new InteractionResult("DÜŞÜK", "Peynir minimal karbonhidrat içerir ve insülin ile güvenlidir.", "🟢", "Güvenli"));
            put("Tereyağı", new InteractionResult("DÜŞÜK", "Tereyağı karbonhidrat içermez ve insülin ile güvenlidir.", "🟢", "Güvenli"));

            // Yağlar
            put("Zeytinyağı", new InteractionResult("DÜŞÜK", "Zeytinyağı kan şekerini etkilemez ve insülin ile mükemmeldir.", "🟢", "Güvenli"));
            put("Ayçiçek Yağı", new InteractionResult("DÜŞÜK", "Ayçiçek yağı kan şekerini etkilemez ve insülin ile güvenlidir.", "🟢", "Güvenli"));
            put("Fındık", new InteractionResult("DÜŞÜK", "Fındık düşük karbonhidrat içerir ve insülin ile uyumludur.", "🟢", "Güvenli"));
            put("Badem", new InteractionResult("DÜŞÜK", "Badem protein ve sağlıklı yağ içerir. İnsülin ile idealdir.", "🟢", "Güvenli"));
        }});

        // ====== BELOC (Metoprolol) ======
        put("Beloc", new HashMap<String, InteractionResult>() {{
            // Meyveler
            put("Elma", new InteractionResult("DÜŞÜK", "Elma ile Beloc arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Muz", new InteractionResult("ORTA", "Muz potasyum içerir. Beloc ile birlikte kan basıncını daha fazla düşürebilir.", "🟡", "Dikkatli Olun"));
            put("Portakal", new InteractionResult("DÜŞÜK", "Portakal ve Beloc arasında önemli etkileşim yoktur.", "🟢", "Güvenli"));
            put("Üzüm", new InteractionResult("DÜŞÜK", "Üzüm ile Beloc güvenle kullanılabilir.", "🟢", "Güvenli"));

            // Sebzeler
            put("Brokoli", new InteractionResult("DÜŞÜK", "Brokoli ve Beloc arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Havuç", new InteractionResult("DÜŞÜK", "Havuç ile Beloc güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Domates", new InteractionResult("DÜŞÜK", "Domates ve Beloc arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Salatalık", new InteractionResult("DÜŞÜK", "Salatalık ile Beloc birlikte kullanılabilir.", "🟢", "Güvenli"));

            // Tahıllar
            put("Pirinç", new InteractionResult("DÜŞÜK", "Pirinç ile Beloc arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Bulgur", new InteractionResult("DÜŞÜK", "Bulgur ve Beloc birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Makarna", new InteractionResult("DÜŞÜK", "Makarna ile Beloc güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Ekmek", new InteractionResult("DÜŞÜK", "Ekmek ve Beloc arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Protein
            put("Tavuk Göğsü", new InteractionResult("DÜŞÜK", "Tavuk göğsü ile Beloc arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Somon", new InteractionResult("DÜŞÜK", "Somon ve Beloc arasında önemli bir etkileşim yoktur.", "🟢", "Güvenli"));
            put("Yumurta", new InteractionResult("DÜŞÜK", "Yumurta ile Beloc güvenle kullanılabilir.", "🟢", "Güvenli"));
            put("Kırmızı Et", new InteractionResult("DÜŞÜK", "Kırmızı et ve Beloc arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Süt Ürünleri
            put("Süt", new InteractionResult("DÜŞÜK", "Süt ile Beloc arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Yoğurt", new InteractionResult("DÜŞÜK", "Yoğurt ve Beloc birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Peynir", new InteractionResult("DÜŞÜK", "Peynir ile Beloc güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Tereyağı", new InteractionResult("DÜŞÜK", "Tereyağı ve Beloc arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Yağlar
            put("Zeytinyağı", new InteractionResult("DÜŞÜK", "Zeytinyağı ile Beloc arasında bilinen bir etkileşim yoktur.", "🟢", "Güvenli"));
            put("Ayçiçek Yağı", new InteractionResult("DÜŞÜK", "Ayçiçek yağı ve Beloc birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Fındık", new InteractionResult("DÜŞÜK", "Fındık ile Beloc güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Badem", new InteractionResult("DÜŞÜK", "Badem ile Beloc güvenle kullanılabilir.", "🟢", "Güvenli"));
        }});

        // ====== APROVEL (Irbesartan) ======
        put("Aprovel", new HashMap<String, InteractionResult>() {{
            // Meyveler
            put("Elma", new InteractionResult("DÜŞÜK", "Elma ile Aprovel arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Muz", new InteractionResult("YÜKSEK", "Muz yüksek potasyum içerir. Aprovel ile birlikte hiperkalemi riskini artırabilir.", "🔴", "Çok Dikkatli Olun"));
            put("Portakal", new InteractionResult("ORTA", "Portakal potasyum içerir. Aprovel ile birlikte potasyum düzeyinizi takip edin.", "🟡", "Dikkatli Olun"));
            put("Üzüm", new InteractionResult("DÜŞÜK", "Üzüm ile Aprovel güvenle kullanılabilir.", "🟢", "Güvenli"));

            // Sebzeler
            put("Brokoli", new InteractionResult("DÜŞÜK", "Brokoli ve Aprovel arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Havuç", new InteractionResult("DÜŞÜK", "Havuç ile Aprovel güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Domates", new InteractionResult("ORTA", "Domates potasyum içerir. Aprovel ile birlikte potasyum seviyelerinizi kontrol edin.", "🟡", "Dikkatli Olun"));
            put("Salatalık", new InteractionResult("DÜŞÜK", "Salatalık ile Aprovel birlikte kullanılabilir.", "🟢", "Güvenli"));

            // Tahıllar
            put("Pirinç", new InteractionResult("DÜŞÜK", "Pirinç ile Aprovel arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Bulgur", new InteractionResult("DÜŞÜK", "Bulgur ve Aprovel birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Makarna", new InteractionResult("DÜŞÜK", "Makarna ile Aprovel güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Ekmek", new InteractionResult("DÜŞÜK", "Ekmek ve Aprovel arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Protein
            put("Tavuk Göğsü", new InteractionResult("DÜŞÜK", "Tavuk göğsü ile Aprovel arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Somon", new InteractionResult("DÜŞÜK", "Somon ve Aprovel birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Yumurta", new InteractionResult("DÜŞÜK", "Yumurta ile Aprovel güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Kırmızı Et", new InteractionResult("DÜŞÜK", "Kırmızı et ve Aprovel arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Süt Ürünleri
            put("Süt", new InteractionResult("DÜŞÜK", "Süt ile Aprovel arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Yoğurt", new InteractionResult("DÜŞÜK", "Yoğurt ve Aprovel birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Peynir", new InteractionResult("DÜŞÜK", "Peynir ile Aprovel güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Tereyağı", new InteractionResult("DÜŞÜK", "Tereyağı ve Aprovel arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Yağlar
            put("Zeytinyağı", new InteractionResult("DÜŞÜK", "Zeytinyağı ile Aprovel arasında bilinen bir etkileşim yoktur.", "🟢", "Güvenli"));
            put("Ayçiçek Yağı", new InteractionResult("DÜŞÜK", "Ayçiçek yağı ve Aprovel birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Fındık", new InteractionResult("DÜŞÜK", "Fındık ile Aprovel güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Badem", new InteractionResult("DÜŞÜK", "Badem ile Aprovel güvenle kullanılabilir.", "🟢", "Güvenli"));
        }});

        // ====== CLARITIN (Loratadine) ======
        put("Claritin", new HashMap<String, InteractionResult>() {{
            // Meyveler
            put("Elma", new InteractionResult("DÜŞÜK", "Elma ile Claritin arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Muz", new InteractionResult("DÜŞÜK", "Muz ve Claritin birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Portakal", new InteractionResult("DÜŞÜK", "Portakal ile Claritin güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Üzüm", new InteractionResult("DÜŞÜK", "Üzüm ve Claritin arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Sebzeler
            put("Brokoli", new InteractionResult("DÜŞÜK", "Claritin ve brokoli arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Havuç", new InteractionResult("DÜŞÜK", "Havuç ile Claritin birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Domates", new InteractionResult("DÜŞÜK", "Domates ve Claritin güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Salatalık", new InteractionResult("DÜŞÜK", "Salatalık ile Claritin arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Tahıllar
            put("Pirinç", new InteractionResult("DÜŞÜK", "Pirinç ile Claritin arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Bulgur", new InteractionResult("DÜŞÜK", "Bulgur ve Claritin birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Makarna", new InteractionResult("DÜŞÜK", "Makarna ile Claritin güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Ekmek", new InteractionResult("DÜŞÜK", "Ekmek ve Claritin arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Protein
            put("Tavuk Göğsü", new InteractionResult("DÜŞÜK", "Tavuk göğsü ile Claritin arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Somon", new InteractionResult("DÜŞÜK", "Somon ve Claritin birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Yumurta", new InteractionResult("DÜŞÜK", "Yumurta ile Claritin güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Kırmızı Et", new InteractionResult("DÜŞÜK", "Kırmızı et ve Claritin arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Süt Ürünleri
            put("Süt", new InteractionResult("DÜŞÜK", "Süt ile Claritin arasında önemli bir etkileşim yoktur.", "🟢", "Güvenli"));
            put("Yoğurt", new InteractionResult("DÜŞÜK", "Yoğurt ve Claritin birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Peynir", new InteractionResult("DÜŞÜK", "Peynir ile Claritin güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Tereyağı", new InteractionResult("DÜŞÜK", "Tereyağı ve Claritin arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Yağlar
            put("Zeytinyağı", new InteractionResult("DÜŞÜK", "Zeytinyağı ile Claritin arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Ayçiçek Yağı", new InteractionResult("DÜŞÜK", "Ayçiçek yağı ve Claritin birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Fındık", new InteractionResult("DÜŞÜK", "Fındık ile Claritin güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Badem", new InteractionResult("DÜŞÜK", "Badem ile Claritin güvenle kullanılabilir.", "🟢", "Güvenli"));
        }});

        // ====== ZYRTEC (Cetirizine) ======
        put("Zyrtec", new HashMap<String, InteractionResult>() {{
            // Meyveler
            put("Elma", new InteractionResult("DÜŞÜK", "Elma ile Zyrtec arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Muz", new InteractionResult("DÜŞÜK", "Muz ve Zyrtec birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Portakal", new InteractionResult("DÜŞÜK", "Portakal ile Zyrtec güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Üzüm", new InteractionResult("DÜŞÜK", "Üzüm ve Zyrtec arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Sebzeler
            put("Brokoli", new InteractionResult("DÜŞÜK", "Brokoli ile Zyrtec arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Havuç", new InteractionResult("DÜŞÜK", "Zyrtec ve havuç arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Domates", new InteractionResult("DÜŞÜK", "Domates ile Zyrtec güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Salatalık", new InteractionResult("DÜŞÜK", "Salatalık ve Zyrtec arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Tahıllar
            put("Pirinç", new InteractionResult("DÜŞÜK", "Pirinç ile Zyrtec arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Bulgur", new InteractionResult("DÜŞÜK", "Bulgur ve Zyrtec birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Makarna", new InteractionResult("DÜŞÜK", "Makarna ile Zyrtec güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Ekmek", new InteractionResult("DÜŞÜK", "Ekmek ve Zyrtec arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Protein
            put("Tavuk Göğsü", new InteractionResult("DÜŞÜK", "Tavuk göğsü ile Zyrtec arasında etkileşim yoktur.", "🟢", "Güvenli"));
            put("Somon", new InteractionResult("DÜŞÜK", "Somon ve Zyrtec birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Yumurta", new InteractionResult("DÜŞÜK", "Yumurta ile Zyrtec güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Kırmızı Et", new InteractionResult("DÜŞÜK", "Kırmızı et ve Zyrtec arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Süt Ürünleri
            put("Süt", new InteractionResult("DÜŞÜK", "Süt ile Zyrtec güvenle kullanılabilir.", "🟢", "Güvenli"));
            put("Yoğurt", new InteractionResult("DÜŞÜK", "Yoğurt ve Zyrtec birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Peynir", new InteractionResult("DÜŞÜK", "Peynir ile Zyrtec güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Tereyağı", new InteractionResult("DÜŞÜK", "Tereyağı ve Zyrtec arasında etkileşim yoktur.", "🟢", "Güvenli"));

            // Yağlar
            put("Zeytinyağı", new InteractionResult("DÜŞÜK", "Zeytinyağı ile Zyrtec arasında bilinen bir etkileşim yoktur.", "🟢", "Güvenli"));
            put("Ayçiçek Yağı", new InteractionResult("DÜŞÜK", "Ayçiçek yağı ve Zyrtec birlikte kullanılabilir.", "🟢", "Güvenli"));
            put("Fındık", new InteractionResult("DÜŞÜK", "Fındık ile Zyrtec güvenle tüketilebilir.", "🟢", "Güvenli"));
            put("Badem", new InteractionResult("DÜŞÜK", "Badem ile Zyrtec güvenle kullanılabilir.", "🟢", "Güvenli"));
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
