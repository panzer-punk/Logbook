package info.logos.form.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import info.logos.form.Network.Objects.Answer;
import info.logos.form.Network.Objects.Question;
import info.logos.form.R;

public class AnswersRecyclerViewAdapter extends RecyclerView.Adapter<AnswersRecyclerViewAdapter.AnswerViewHolder> {

    private List<Answer> answers;

    public AnswersRecyclerViewAdapter() {
        answers = new ArrayList<>(4);
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_layout, parent, false);
        AnswerViewHolder answerViewHolder = new AnswerViewHolder(v);
        return answerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        holder.textView.setText(answers.get(position).getAnswer());
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    private void inflateAnswers(List<Answer> answers){

      this.answers.addAll(answers);

    }

    public void setAnswers(List<Answer> answers){

        this.answers.clear();
        inflateAnswers(answers);
        notifyDataSetChanged();

    }

    public void setQuestion(Question question){

        this.answers.clear();
        inflateAnswers(question.getAnswers());
        notifyDataSetChanged();

    }


    class AnswerViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;

        public AnswerViewHolder(@NonNull View view) {
            super(view);
            textView = view.findViewById(R.id.answer_text);
        }


    }


}
