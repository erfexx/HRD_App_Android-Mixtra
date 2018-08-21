package com.mit.mobile.absencehrd.Helper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mit.mobile.absencehrd.Model.Comment;
import com.mit.mobile.absencehrd.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private List<Comment> commentList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, title, comment, date;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            name =  view.findViewById(R.id.name);
            comment =  view.findViewById(R.id.comment);
            date =  view.findViewById(R.id.date);
        }
    }

    public CommentAdapter(List<Comment> commentList)
    {
        this.commentList = commentList;
    }



    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.MyViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.title.setText(comment.getTitle());
        holder.comment.setText(comment.getComment());
        holder.date.setText(comment.getCreated_at());
        holder.name.setText(comment.getCommenter());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
