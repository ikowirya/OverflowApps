package stackexchange.tlab.com.tlaboverflow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import stackexchange.tlab.com.tlaboverflow.adapter.ListItemAdapter;
import stackexchange.tlab.com.tlaboverflow.model.Items;
import stackexchange.tlab.com.tlaboverflow.model.Schema;
import stackexchange.tlab.com.tlaboverflow.network.ApiClient;
import stackexchange.tlab.com.tlaboverflow.network.ApiInterface;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    @BindView(R.id.inputTag)
    EditText inputTag;
    @BindView(R.id.btnRangeDateEnd)
    ImageView btnRangeDateEnd;
    @BindView(R.id.txtNothing)
    TextView txtNothing;
    @BindView(R.id.listRv)
    RecyclerView listRv;
    ApiInterface apiInterface;
    RecyclerView.LayoutManager mLayoutManager;
    ListItemAdapter listItemAdapter;
    String tagged ="";
    String pagesize="10";
    String min="";
    String max="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        mLayoutManager = new LinearLayoutManager(this);
        listRv.setLayoutManager(mLayoutManager);

        getItemData(tagged,pagesize);

        inputTag.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    tagged = inputTag.getText().toString();
                    getItemData(tagged,pagesize);
                    return true;
                }
                return false;
            }
        });

        btnRangeDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RangeDateActivity.class));
            }
        });


    }

    @Override
    protected void onResume() {
        getItemData(tagged,pagesize);
        super.onResume();
    }

    public void getItemData(String tagged, String pagesize){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref",0);
        min = preferences.getString("timestampStart",null);
        max = preferences.getString("timestampEnd",null);
        Call<Schema> users = apiInterface.getSearch(pagesize,"desc",min,
                max,"activity",tagged,"stackoverflow");
        users.enqueue(new Callback<Schema>() {
            @Override
            public void onResponse(Call<Schema> call, retrofit2.Response<Schema> response) {
                if (response.isSuccessful()) {

                    List<Items> itemsList = response.body().getItemsList();
                    if (itemsList.size()>0)
                    {
                        listItemAdapter = new ListItemAdapter(itemsList);
                        listRv.setAdapter(listItemAdapter);
                        listRv.setVisibility(View.VISIBLE);
                        txtNothing.setVisibility(View.INVISIBLE);
                    }
                    else {
                        txtNothing.setVisibility(View.VISIBLE);
                        listRv.setVisibility(View.INVISIBLE);
                    }


                } else {
                    txtNothing.setVisibility(View.VISIBLE);
                    listRv.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Schema> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.toString());
                t.printStackTrace();
                Snackbar.make(findViewById(R.id.main_layout), "Try Again.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        Spinner shareSpinner = (Spinner) menu.findItem(R.id.spinPage).getActionView();
        List<String> categories = new ArrayList<String>();
        categories.add(0,"10");
        categories.add(1,"20");
        categories.add(2,"30");
        categories.add(3,"50");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.layout_spinner, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.layout_cheked_spinner);


        // attaching data adapter to spinner
        shareSpinner.setAdapter(dataAdapter);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref",0);
        shareSpinner.setSelection(preferences.getInt("spinnerSelection",0));
        shareSpinner.setOnItemSelectedListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_sign_out:
                signOut();
                break;
            default:
                break;
        }

        return true;
    }

    private void signOut() {

        SharedPreferences preferences = MainActivity.this.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();

        mAuth.signOut();
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        startActivity(new Intent(MainActivity.this, SplashActivity.class));
                        finish();
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item

        pagesize = parent.getItemAtPosition(position).toString();
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref",0);
        SharedPreferences.Editor editor = preferences.edit();
        int selectedPosition = parent.getSelectedItemPosition();
        editor.putInt("spinnerSelection", selectedPosition);
        editor.commit();

        getItemData(tagged,pagesize);
        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + pagesize, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


}
