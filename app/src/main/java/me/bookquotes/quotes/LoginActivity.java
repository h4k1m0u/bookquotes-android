package me.bookquotes.quotes;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static me.bookquotes.quotes.R.id.login;

/**
 * Basic authentication with retrofit:
 * https://futurestud.io/tutorials/android-basic-authentication-with-retrofit
 * https://stackoverflow.com/q/45190997/2228912
 */

public class LoginActivity extends AppCompatActivity {
    private EditText inputUsername;
    private EditText inputPassword;
    private Button btnLogin;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // check if already authenticated (saved token)
        mPrefs = getSharedPreferences("prefs", 0);
        String t = mPrefs.getString("token", null);
        if (t != null) {
            // go directly to accounting activity
            Intent intent = new Intent(this, QuotesActivity.class);
            intent.putExtra("token", t);
            startActivity(intent);
        }

        // set layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // add actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // button click listener
        inputUsername = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get username and password
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // token authentication
                Retrofit retrofit = Util.getBuilder();
                LoginAPI api = retrofit.create(LoginAPI.class);
                Call<Token> call = api.login(username, password);
                call.enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        if (response.code() == 200) {
                            // save token in the preferences
                            Token token = response.body();
                            String t = token.getToken();
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString("token", t);
                            editor.commit();

                            // go to next activity
                            Intent intent = new Intent(LoginActivity.this, QuotesActivity.class);
                            intent.putExtra("token", t);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        String message = t.getMessage();
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu
        getMenuInflater().inflate(R.menu.login_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.register_user:
                // open register user dialog
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.user_register);
                dialog.show();

                // register user
                final EditText usernameEditText = (EditText) dialog.findViewById(R.id.new_username);
                final EditText passwordEditText = (EditText) dialog.findViewById(R.id.new_password);
                Button addButton = (Button) dialog.findViewById(R.id.register);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get username and password
                        final String username = usernameEditText.getText().toString().trim();
                        final String password = passwordEditText.getText().toString().trim();

                        // registration
                        Retrofit retrofit = Util.getBuilder();
                        RegisterAPI api = retrofit.create(RegisterAPI.class);
                        Call<User> callRegister = api.register(username, password);
                        callRegister.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.code() == 201) {
                                    // token authentication using created user
                                    Retrofit retrofit = Util.getBuilder();
                                    LoginAPI api = retrofit.create(LoginAPI.class);
                                    Call<Token> callLogin = api.login(username, password);
                                    callLogin.enqueue(new Callback<Token>() {
                                        @Override
                                        public void onResponse(Call<Token> call, Response<Token> response) {
                                            if (response.code() == 200) {
                                                // save token in the preferences
                                                Token token = response.body();
                                                String t = token.getToken();
                                                SharedPreferences.Editor editor = mPrefs.edit();
                                                editor.putString("token", t);
                                                editor.commit();

                                                // go to next activity
                                                Intent intent = new Intent(LoginActivity.this, QuotesActivity.class);
                                                intent.putExtra("token", t);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Token> call, Throwable t) {
                                            String message = t.getMessage();
                                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                String message = t.getMessage();
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                break;
        }

        return true;
    }
}