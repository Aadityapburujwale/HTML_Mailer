package com.htmlmailer;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MainActivity extends AppCompatActivity {

    EditText userName_editText;
    EditText password_editText;
    EditText recipient_Email_editText;
    EditText title_editText;
    EditText message_editText;
    Button openGoogleSetting_button;
    CheckBox done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName_editText = findViewById(R.id.enter_email);
        password_editText = findViewById(R.id.enter_password);
        recipient_Email_editText = findViewById(R.id.enter_Remail);
        title_editText = findViewById(R.id.enter_title);
        message_editText = findViewById(R.id.enter_message);
        openGoogleSetting_button = findViewById(R.id.open_setting_btn);
        done = findViewById(R.id.done_checkBox);

        openGoogleSetting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openSetting = new Intent(Intent.ACTION_VIEW, Uri.parse("https://myaccount.google.com/lesssecureapps"));
                startActivity(openSetting);
            }
        });

    }

    public void sendMail(View view) {

        final String userName = userName_editText.getText().toString();
        final String password = password_editText.getText().toString();
        String recipient_Email = recipient_Email_editText.getText().toString();
        String title = title_editText.getText().toString();
        String message = message_editText.getText().toString();

        if (anyOneEmpty(userName, password, message, title, recipient_Email))
            sendEmail(userName, password, message, title, recipient_Email);
    }

    boolean anyOneEmpty(String userName, String password, String message, String title, String recipient_Email) {

        if (userName.matches("")) {
            userName_editText.setError("Please Enter A Email Id");
            return false;
        }
        if (password.matches("")) {
            password_editText.setError("Please Enter Password");
            return false;
        }
        if (message.matches("")) {
            message_editText.setError("Please Enter A Message you want to send (In Html)");
            return false;
        }
        if (title.matches("")) {
            title_editText.setError("Please Enter A Title/Subject Of Email");
            return false;
        }
        if (recipient_Email.matches("")) {
            recipient_Email_editText.setError("Please Enter A Recipient Email Id");
            return false;
        }
        if (!done.isChecked()) {
            Toast.makeText(getApplicationContext(), "Please Turn On The Setting From Google Ac. and click On Done .", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    void sendEmail(String userName, String password, String textMail, String title, String recipient_Email) {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        Session session = javax.mail.Session.getInstance(properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, password);
                    }
                });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userName));
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(recipient_Email));
            message.setSubject(title);
            message.setContent(textMail, "text/html");
            Transport.send(message);
            Toast.makeText(getApplicationContext(), "Email Send Successfully.", Toast.LENGTH_LONG).show();
        } catch (MessagingException e) {
            Toast.makeText(getApplicationContext(), "Something Went Wrong.", Toast.LENGTH_LONG).show();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


    // Show_HTML_Preview Button
    public void show_html_preview(View view) {

        Dialog htmlPreview = new Dialog(this);
        htmlPreview.requestWindowFeature(Window.FEATURE_NO_TITLE);
        htmlPreview.setContentView(R.layout.html_viewer);
        TextView close = htmlPreview.findViewById(R.id.close);
        String message = message_editText.getText().toString();

        if (message.length() > 0) {
            WebView htmlView = htmlPreview.findViewById(R.id.webview);
            htmlView.loadDataWithBaseURL(null, message, "text/html", "utf-8", null);
            htmlPreview.show();
        } else {
            message_editText.setError("Please Enter A Message you want to send (In Html)");
        }


        // Close Button
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                htmlPreview.dismiss();
            }
        });

    }
}