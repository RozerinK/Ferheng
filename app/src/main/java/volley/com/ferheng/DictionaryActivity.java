package volley.com.ferheng;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class DictionaryActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {


public static SQLiteDatabase database;
    SharedPreferences sharedPreferences ;
    Dictionary dictionary;
    AutoCompleteTextView searchview;
    Button favouriteButton;
    TextView meaning;
    String[] arraySpinner  = null;
    Spinner langSpinner = null;
    int zazaki =0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBaseHelper helper = new DataBaseHelper(getApplicationContext(), "dictionaryZazaki.db", "volley.com.ferheng");
        database = helper.openDataBase();
        dictionary = new Dictionary(database);
        setContentView(R.layout.activity_dictionary);
        initView();

    }

    public ArrayList TrDictionaryData() {
        ArrayList<String> TrWords = new ArrayList<String>();

        Cursor data = database.rawQuery("Select * from tr order by name", null);

        while (data.moveToNext()) {
            TrWords.add(data.getString(data.getColumnIndex("name")));
        }

        return TrWords;
    }

    public ArrayList ZazakiDictionaryData() {
        ArrayList<String> ZazakiWords = new ArrayList<String>();

        Cursor data = database.rawQuery("Select * from zazaki order by name", null);

        while (data.moveToNext()) {
            ZazakiWords.add(data.getString(data.getColumnIndex("name")));
        }

        return ZazakiWords;
    }


    public void initView() {
        favouriteButton = (Button) findViewById(R.id.activity_dictionary_favouritebutton);
      /*  pageTitleComponent = (TextView) findViewById(R.id.wordoftheday);
        favouriteButton.setVisibility(View.INVISIBLE);
        favouriteButton.setOnClickListener(this);
        meaningOfWordComponent = (TextView) findViewById(R.id.meaning);
        wordComponent = (TextView) findViewById(R.id.word);
        meaningOfWordComponent.setMovementMethod(new ScrollingMovementMethod());
        */

       // context = getBaseContext();
       // assetmanager = getAssets();

      //  typeFacePothana = Typeface.createFromAsset(assetmanager, "Pothana2000.ttf");
        //typeFaceOpenSans = Typeface.createFromAsset(assetmanager, "OpenSans_Semibold.ttf");

        //meaningOfWordComponent.setTypeface(typeFacePothana);
        //pageTitleComponent.setTypeface(typeFaceOpenSans);


        favouriteButton = (Button)findViewById(R.id.activity_dictionary_favouritebutton);
        meaning = (TextView)findViewById(R.id.activity_dictionary_meaning);

        searchview = (AutoCompleteTextView) findViewById(R.id.activity_dictionary_searchview);
        //searchview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, TrDictionaryData()));
        searchview.setPadding(10, 0, 0, 0);
        searchview.setHint("type turkish");

        arraySpinner = new String[] {  "Dil seçiniz", "Zazaki-Turkish", "Turkish-Zazaki"};
        langSpinner = (Spinner) findViewById(R.id.activity_dictionary_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        langSpinner.setAdapter(adapter);

        initEvent();

  /*  @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String word = searchview.getText().toString();
       String word1 = dictionary.getTrMeaning(word);
        meaning.setText(word1);



    }*/

    };

        public void initEvent(){

            langSpinner.setOnItemSelectedListener(this);
            searchview.setOnItemClickListener(this);
        }





    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       zazaki = position;
        searchview.setThreshold(1);
        if(zazaki == 1)
            searchview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ZazakiDictionaryData()));
        else if(zazaki==2 )
            searchview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, TrDictionaryData()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(zazaki == 1)
        searchForZazaki();
        else if(zazaki==2 )
            searchForTurkish();
    }

    public void searchForZazaki(){


        String word = searchview.getText().toString();
        String word1 = dictionary.getTrMeaning(word);
        meaning.setText(word1);
    }

    public void setTurkishDatabase(){


    }
    public void searchForTurkish(){


        String word = searchview.getText().toString();
        String word1 = dictionary.getZazakiMeaning(word);
        meaning.setText(word1);
    }

}
