package com.example.rxjavatestapp;

import android.os.Bundle;
import android.widget.EditText;

import com.example.rxjavatestapp.network.ApiFunctions.ApiFail;
import com.example.rxjavatestapp.network.ApiFunctions.ApiSuccess;
import com.example.rxjavatestapp.network.ApiFunctions.HttpErrorResponse;
import com.example.rxjavatestapp.network.api.Api;
import com.example.rxjavatestapp.network.responce.UserSearchResponce;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements RecyclerInterface {
    private EditText idEtEditText;
    private RecyclerView idRvUserNameRecycler;
    private RecyclerAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String subString;
    private String mainString;

    private static final String TAG = "MainActivity";
    private static final String SESSION_ID = "t8n78sQGc8";
    private static final int COUNT = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainString = "";
        subString = "";

        idEtEditText = findViewById(R.id.idEtEditText);

        //RecyclerView Setup
        idRvUserNameRecycler = findViewById(R.id.idRvUserNameRecycler);
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), this);
        layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, true);
        idRvUserNameRecycler.setHasFixedSize(false);
        idRvUserNameRecycler.setLayoutManager(layoutManager);
        idRvUserNameRecycler.setAdapter(recyclerAdapter);


        RxTextView.textChanges(idEtEditText)
                .skip(1)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CharSequence>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        mainString = charSequence.toString();
                        if (mainString.contains("@") && !(mainString.lastIndexOf(" ") > mainString.lastIndexOf("@"))) {
                            subString = mainString.substring(mainString.lastIndexOf("@") + 1);
                        } else {
                            subString = "";
                        }
                        if (subString.length() > 0) {
                            ApiCall(subString);
                        } else {
                            recyclerAdapter.clearUserList();
                        }
                    }
                });
    }

    public void ApiCall(String subString) {
        Api.userTagApi().userSearch(SESSION_ID, COUNT, subString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ApiSuccess<UserSearchResponce>() {
                    @Override
                    public void call(UserSearchResponce userSearchResponce) {
                        if (userSearchResponce.getOpStatus().equalsIgnoreCase("success")) {
                            if (userSearchResponce.getData().getCount() > 0) {
                                recyclerAdapter.updateUserList(userSearchResponce.getData().getUsers());
                                int lastPosition = userSearchResponce.getData().getUsers().size() - 1;
                                idRvUserNameRecycler.scrollToPosition(lastPosition);
                            } else {
                                recyclerAdapter.clearUserList();
                            }
                        }
                    }
                }, new ApiFail() {
                    @Override
                    public void httpStatus(HttpErrorResponse response) {
                    }

                    @Override
                    public void noNetworkError() {
                    }

                    @Override
                    public void unknownError(Throwable throwable) {
                    }
                });
    }

    @Override
    public void selectItem(String userName) {
        recyclerAdapter.clearUserList();
        String replaceString = userName.concat(" ");
        idEtEditText.setText(idEtEditText.getText().toString().replace(subString, replaceString));
        idEtEditText.setSelection(idEtEditText.getText().length());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}