package info.logos.form.Fragment;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.logos.form.Adapter.AnswersRecyclerViewAdapter;
import info.logos.form.Network.Objects.Answer;
import info.logos.form.Network.Objects.Quiz;
import info.logos.form.R;

public class QuizFragment extends DialogFragment {
    public static final String TAG = "example_dialog";

    private ImageButton quitButton;
    RecyclerView.LayoutManager rLayoutManager;
    private AnswersRecyclerViewAdapter answersRecyclerViewAdapter;
    protected TextView quizTitle;
    private RecyclerView recyclerView;

    public QuizFragment(String path) {

    new GetQuiz().execute(path);

    }

    public static QuizFragment display(FragmentManager fragmentManager, String path) {
        QuizFragment exampleDialog = new QuizFragment(path);
        exampleDialog.show(fragmentManager, TAG);
        return exampleDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.quiz_page, container, false);

        recyclerView = view.findViewById(R.id.quiz_answers);
        answersRecyclerViewAdapter = new AnswersRecyclerViewAdapter();
        rLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(rLayoutManager);
        recyclerView.setAdapter(answersRecyclerViewAdapter);
        quitButton = view.findViewById(R.id.quit_button);
        quizTitle = view.findViewById(R.id.quiz_title);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    class GetQuiz extends AsyncTask<String, Void, Quiz> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Quiz doInBackground(String... params) {
            URL url;
            URLConnection request;
            Quiz quiz = null;
            BufferedReader in = null;
            StringBuilder stringBuilder = new StringBuilder();
            try {
                url = new URL(params[0]);
                request = url.openConnection();
                request.connect();
                in = new BufferedReader(new InputStreamReader(
                        request.getInputStream()));

                String inputLine = in.readLine();

                while (inputLine != null){
                    stringBuilder.append(inputLine);
                    inputLine = in.readLine();
                }
                Gson gson = new Gson();
                quiz = gson.fromJson(stringBuilder.toString(), Quiz.class);

            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return quiz;
        }

        @Override
        protected void onPostExecute(Quiz result) {
            super.onPostExecute(result);
            Collections.shuffle(result.getQuestions());
            quizTitle.setText(result.getQuestions().get(0).getQuestion());
            List<Answer> toShuffle = result.getQuestions().get(0).getAnswers();
            Collections.shuffle(toShuffle);
            answersRecyclerViewAdapter.setAnswers(toShuffle);
        }
    }
}

