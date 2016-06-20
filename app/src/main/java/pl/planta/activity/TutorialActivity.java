package pl.planta.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;

import pl.planta.R;
import pl.planta.adapters.CustomListTutorial;

/**
 * Created by panko on 20.06.2016.
 */
public class TutorialActivity extends Activity {

    private ListView mListView;
    private String[] itemName = {
            "Komputer",
            "Hak",
            "Magazyn",
            "Piec",
            "Rurociąg",
            "Kopalnia",
            "Fabryki",
            "Mieszkania"
    };
    private String[] itemDesc = {
            "Im wiekszy poziom ma komputer w centrum dowodzenia, tym więcej prądu uda Ci się uzyskać.",
            "Im wyższy poziom haka, tym mniej węgla potrzebujesz by wyprodukować prąd.",
            "Im wyższy poziom magazynu, tym więcej surowców możesz w nim pomieścić.",
            "Im wyższy poziom pieca, tym mniej wody potrzebujesz by wyprodukować prąd.",
            "Im wyższy poziom rurociągu, tym więcej wody jest dostarczane do elektrowni.",
            "Im wyższy poziom kopalni, tym więcej węgla jest dostarczane do elektrowni.",
            "Im wyższy poziom fabryk w mieście, tym zwieksza się poziom zapotrzebowania na prąd oraz wzrasta poziom pieniędzy",
            "Im wyższy poziom zabudowania mieszkalnego, tym więcej podatków za prąd jest pobierane."
    };
    private Integer[] itemImage = {
            R.drawable.computer,
            R.drawable.hook,
            R.drawable.door,
            R.drawable.furnace,
            R.drawable.pipeline,
            R.drawable.entrance,
            R.drawable.industry,
            R.drawable.residence
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tutorial);

        CustomListTutorial customListTutorial = new CustomListTutorial(this, itemName, itemDesc, itemImage);
        mListView = (ListView) findViewById(R.id.list);
        mListView.setAdapter(customListTutorial);
    }
}
