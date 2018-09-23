package madsoft.com.form;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;


import com.klinker.android.sliding.SlidingActivity;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;



public class SlidingThemeActivity extends SlidingActivity {

    private Connector connector;

    private String href;
    private Toolbar toolbar;
    private CacheSystem cacheSystem;
    private String filename;
    private String loaderInput, dialogInput;
    private TextView htmlTextView;
    private TextView title;
    private TextView content;

    @Override
    public void init(Bundle savedInstanceState) {
        setTitle(R.string.loading);

        setPrimaryColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark)
        );
        

        setContent(R.layout.activity_theme);


        this.makeContent(savedInstanceState);


    }


    public void makeContent(Bundle savedInstanceState) {
        href = Assets.ROOT + getIntent().getStringExtra(Assets.CONTENT);

        filename = getIntent().getStringExtra(Assets.FILENAME);

        cacheSystem = new CacheSystem(this);

        setTitle(filename);

        htmlTextView = findViewById(R.id.activity_theme);
        htmlTextView.setLinksClickable(true);

        title = findViewById(R.id.diaog_title);
        content = findViewById(R.id.diaog_text);


        // htmlTextView.setMovementMethod(LinkMovementMethod.getInstance());
        // htmlTextView.setMovementMethod(LinkMovementMethod.getInstance());


        connector = new Connector();

        if (cacheSystem.checkFile(filename)) {
            //   htmlTextView.setHtml(cacheSystem.load(filename), new HtmlHttpImageGetter(htmlTextView, null, false));

            ProgressBar progressBar = findViewById(R.id.progressBar);

            progressBar.setVisibility(View.GONE);

        } else
            new SlidingThemeActivity.ParseTask().execute(href);


    }


    private class ParseTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... arg) {


            try {

                Document document = Jsoup.connect(arg[0]).get();

                madsoft.com.form.Parser parser = new madsoft.com.form.Parser(document);

                loaderInput = parser.parseContent();

                parser = null;

                return true;


            } catch (Exception exp) {
                exp.printStackTrace();
                return false;
            }


        }


        @Override
        protected void onPostExecute(Boolean downloaded) {

            if (downloaded) {

                ProgressBar progressBar = findViewById(R.id.progressBar);

                progressBar.setVisibility(View.GONE);

                if (loaderInput != null) {

                    // if(Assets.DOWNLOADFLAG) Сделать для этого кнопку на самой активности
                    // cacheSystem.write(loaderInput, filename);


                    htmlTextView.setText(linkifyHtml(loaderInput, Linkify.ALL));

                    htmlTextView.setMovementMethod(LinkMovementMethod.getInstance());


                } else {
                    toastMaker("Страница будет загружена при подлкючении к сети.");
                }
            }
        }


        private void makeLinkClickable(final SpannableStringBuilder strBuilder, final URLSpan span) {
            final int start = strBuilder.getSpanStart(span);
            final int end = strBuilder.getSpanEnd(span);
            int flags = strBuilder.getSpanFlags(span);
            if (span.getURL().contains("shorts")) {
                ClickableSpan clickable = new ClickableSpan() {
                    public void onClick(View view) {
                        // Do something with span.getURL() to handle the link click...
                        new ParseDialogTask().execute(Assets.THEME_PATH + span.getURL());
                    }
                };
                strBuilder.setSpan(clickable, start, end, flags);
                strBuilder.removeSpan(span);
            }
        }

        private Spannable linkifyHtml(String html, int linkifyMask) {
            Spanned text = Html.fromHtml(html);
            URLSpan[] currentSpans = text.getSpans(0, text.length(), URLSpan.class);

            SpannableStringBuilder buffer = new SpannableStringBuilder(text);
            // Linkify.addLinks(buffer, linkifyMask);

            for (URLSpan span : currentSpans) {
                makeLinkClickable(buffer, span);
            }
            return buffer;
        }

        private void toastMaker(String text) {

            Toast toast = Toast.makeText(getApplicationContext(),
                    text,
                    Toast.LENGTH_SHORT);
            toast.show();

        }


    }


    public void dialogMaker(String titleS, String contentS) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_term, null);
        title = dialogView.findViewById(R.id.diaog_title);
        content = dialogView.findViewById(R.id.diaog_title);
        title.setText(titleS);
        content.setText(contentS);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();



    }

    private class ParseDialogTask extends AsyncTask<String, Void, Boolean> {
        String titleS;

        @Override
        protected Boolean doInBackground(String... arg) {


            try {

                Document document = Jsoup.connect(arg[0]).get();

                titleS = document.title();

                madsoft.com.form.Parser parser = new madsoft.com.form.Parser(document);

                dialogInput = parser.parseContent();

                parser = null;

                return true;


            } catch (Exception exp) {
                exp.printStackTrace();
                return false;
            }


        }




        @Override
        protected void onPostExecute(Boolean downloaded) {

            if (downloaded) {

                if (dialogInput != null && titleS != null) {


                    dialogMaker(dialogInput, titleS);

                } else {

                }
            }
        }
    }
}



