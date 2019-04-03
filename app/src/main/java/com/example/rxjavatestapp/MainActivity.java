package com.example.rxjavatestapp;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rxjavatestapp.network.ApiFunctions.ApiFail;
import com.example.rxjavatestapp.network.ApiFunctions.ApiSuccess;
import com.example.rxjavatestapp.network.ApiFunctions.HttpErrorResponse;
import com.example.rxjavatestapp.network.api.Api;
import com.example.rxjavatestapp.network.responce.User;
import com.example.rxjavatestapp.network.responce.UserSearchResponce;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements RecyclerInterface {
    private EditText idEtEditText;
    private Button idBtSendButton;
    private TextView idTvCommentBox;
    private RecyclerView idRvUserNameRecycler;
    private RecyclerAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String subString;
    private String mainString;
    private HashMap<String, User> userTagList;
    private int subStringStart;
    private int subStringEnd;

    private static final String TAG = "MainActivity";
    private static final String SESSION_ID = "URxgM0zh";
    private static final int COUNT = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainString = "";
        subString = "";
        userTagList = new HashMap<>();

        idEtEditText = findViewById(R.id.idEtEditText);
        idTvCommentBox = findViewById(R.id.idTvCommentBox);
        idBtSendButton = findViewById(R.id.idBtSendButton);
        idBtSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentString = idEtEditText.getText().toString();
                String[] separated = commentString.split(" ");
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                for (final String aSeparated : separated) {
                    if (aSeparated.contains("@")) {
                        if (aSeparated.indexOf("@") == 0 && aSeparated.length() > 2) {
                            SpannableString spannableString = new SpannableString(aSeparated);
                            ClickableSpan termsClickableSpan = new ClickableSpan() {
                                @Override
                                public void onClick(@NonNull View textView) {
                                    User user = userTagList.get(aSeparated.substring(1));
                                    if (user != null) {
                                        Log.d(TAG, "TestLog: |-/: " + user.getUsername());
                                        Log.d(TAG, "TestLog: |-/: " + user.getUserId());
                                        Log.d(TAG, "TestLog: |-/: " + user.getUserFavorited());
                                        Log.d(TAG, "TestLog: |-/: " + user.getProfilePicUrl());
                                    }
                                    Toast.makeText(getApplicationContext(), aSeparated, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void updateDrawState(@NonNull TextPaint drawState) {
                                    super.updateDrawState(drawState);
                                    drawState.setUnderlineText(false);
                                }
                            };
                            spannableString.setSpan(termsClickableSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spannableString.setSpan(new ForegroundColorSpan(getColor(R.color.colorAccent)), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spannableStringBuilder.append(spannableString);
                        } else {
                            spannableStringBuilder.append(aSeparated);
                        }
                    } else {
                        spannableStringBuilder.append(aSeparated);
                    }
                    spannableStringBuilder.append(" ");
                }
                idTvCommentBox.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
                idTvCommentBox.setMovementMethod(LinkMovementMethod.getInstance());
                idTvCommentBox.setHighlightColor(Color.TRANSPARENT);
                idEtEditText.setText("");
            }
        });

        //RecyclerView Setup
        idRvUserNameRecycler = findViewById(R.id.idRvUserNameRecycler);
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), this);
        layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, true);
        idRvUserNameRecycler.setHasFixedSize(false);
        idRvUserNameRecycler.setLayoutManager(layoutManager);
        idRvUserNameRecycler.setAdapter(recyclerAdapter);

        RxTextView.textChanges(idEtEditText)
                .skip(1)
                .debounce(300, TimeUnit.MILLISECONDS)
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
                        String firstHalf = mainString.substring(0, idEtEditText.getSelectionStart());
                        String secondHalf = mainString.substring(idEtEditText.getSelectionStart());
                        if (mainString.contains("@")) {
                            if (firstHalf.lastIndexOf("@") > firstHalf.lastIndexOf(" ")) {
                                subStringStart = firstHalf.lastIndexOf("@") + 1;
                                subStringEnd = idEtEditText.getSelectionStart();
                                if (mainString.length() == idEtEditText.getSelectionStart()) {
                                    subString = mainString.substring(subStringStart);
                                    ApiCall(subString);
                                } else {
                                    if (secondHalf.contains(" ")) {
                                        subStringEnd = idEtEditText.getSelectionStart() + secondHalf.indexOf(" ");
                                    }
                                    subString = mainString.substring(subStringStart, subStringEnd);
                                    ApiCall(subString);
                                }
                            } else {
                                recyclerAdapter.clearUserList();
                            }
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
    public void selectItem(User user) {
        String userName = user.getUsername();
        userTagList.put(userName, user);
        recyclerAdapter.clearUserList();
        String newSubString;
        if (!(mainString.substring(subStringEnd).indexOf(" ") == 0)) {
            newSubString = userName.concat(" ");
        } else {
            newSubString = userName;
        }
        String newString = mainString.substring(0, subStringStart) + newSubString + mainString.substring(subStringEnd);
        idEtEditText.setText(newString);
        idEtEditText.setSelection(idEtEditText.getText().length());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
