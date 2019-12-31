package com.example.myapplication.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SigninFragment extends Fragment {

    private SigninViewModel SigninViewModel;
    private FirebaseAuth mAuth;
    private TextView UserName;
    private TextView UserEmail;
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button btn_login ;
    private FirebaseUser user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SigninViewModel =
                ViewModelProviders.of(this).get(SigninViewModel.class);
        View root = inflater.inflate(R.layout.fragment_signin,container,false);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        //connect
        mAuth = FirebaseAuth.getInstance();
        accountEdit = root.findViewById(R.id.signup_email);
        passwordEdit = root.findViewById(R.id.signup_password);
        btn_login = root.findViewById(R.id.btn_login);
        UserName = root.findViewById(R.id.UserName);
        UserEmail = root.findViewById(R.id.UserEmail);
        //btn_click
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if(!account.equals("") && !password.equals("")){
                    mAuth.signInWithEmailAndPassword(account,password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        toastMessage("登入成功");
                                        if (mAuth.getCurrentUser() != null)
                                            ((MainActivity)getActivity()).setNavigate(mAuth.getCurrentUser().getDisplayName(), mAuth.getCurrentUser().getEmail());
                                    }
                                }
                            });
                }
                else {
                    if(account.isEmpty()){
                        toastMessage("請輸入帳號");
                    }
                    else{
                        toastMessage("請輸入密碼");
                    }
                }
            }
        });
        return root;
    }
    //add a toast to show when successfully signed in
    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }
}




