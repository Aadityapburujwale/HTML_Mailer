package com.htmlmailer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import javax.mail.Session;
import java.util.Properties;
import javax.mail.PasswordAuthentication;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMail(View view) {

        EditText userName_editText = findViewById(R.id.enter_email);
        EditText password_editText = findViewById(R.id.enter_password);
        EditText recipient_Email_editText = findViewById(R.id.enter_Remail);
        EditText title_editText = findViewById(R.id.enter_title);
        EditText message_editText = findViewById(R.id.enter_message);

        final String userName = userName_editText.getText().toString();
        final String password = password_editText.getText().toString();

        String recipient_Email = recipient_Email_editText.getText().toString();
        String title = title_editText.getText().toString();
        String message = message_editText.getText().toString();

        if(anyOneEmpty(userName,password,message,title,recipient_Email))
            sendEmail(userName,password,message,title,recipient_Email);
    }

    boolean anyOneEmpty(String userName,String password, String textMail , String title, String recipient_Email){

        EditText userName_editText = findViewById(R.id.enter_email);
        EditText password_editText = findViewById(R.id.enter_password);
        EditText recipient_Email_editText = findViewById(R.id.enter_Remail);
        EditText title_editText = findViewById(R.id.enter_title);
        EditText message_editText = findViewById(R.id.enter_message);
        CheckBox done = findViewById(R.id.done_checkBox);

        if(userName.matches("")){
            userName_editText.setError("Please Enter A Email Id");
            Toast.makeText(getApplicationContext(),"Please Enter A Email Id",Toast.LENGTH_LONG).show();
            return false;
        }
        if(password.matches("")){
            password_editText.setError("Please Enter Password");
            Toast.makeText(getApplicationContext(),"Please Enter Password",Toast.LENGTH_LONG).show();
            return false;
        }
        if(textMail.matches("")){
            message_editText.setError("Please Enter A Message you want to send (In Html)");
            Toast.makeText(getApplicationContext(),"Please Enter A Message you want to send (In Html)",Toast.LENGTH_LONG).show();
            return false;
        }
        if(title.matches("")){
            title_editText.setError("Please Enter A Title/Subject Of Email");
            Toast.makeText(getApplicationContext(),"Please Enter A Title/Subject Of Email",Toast.LENGTH_LONG).show();
            return false;
        }
        if(recipient_Email.matches("")){
            recipient_Email_editText.setError("Please Enter A Recipient Email Id");
            Toast.makeText(getApplicationContext(),"Please Enter A Recipient Email Id",Toast.LENGTH_LONG).show();
            return false;
        }
        if(!done.isChecked()){
            Toast.makeText(getApplicationContext(),"Please Turn On The Setting From Google Ac. and click On Done .",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    void sendEmail(String userName,String password, String textMail , String title, String recipient_Email){

        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        Session session = javax.mail.Session.getInstance(properties,
                new Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(userName,password);
                    }
                });
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userName));
            message.setRecipients(MimeMessage.RecipientType.TO,InternetAddress.parse(recipient_Email));
            message.setSubject(title);
            message.setContent(textMail,"text/html");
            Transport.send(message);
            Toast.makeText(getApplicationContext(),"Email Send Successfully.",Toast.LENGTH_LONG).show();
        }catch (MessagingException e){
            Toast.makeText(getApplicationContext(),"Something Went Wrong.",Toast.LENGTH_LONG).show();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void openGoogleSetting(View view) {
        Intent goToTheSetting = new Intent(Intent.ACTION_VIEW, Uri.parse("https://myaccount.google.com/lesssecureapps"));
        startActivity(goToTheSetting);
    }
}