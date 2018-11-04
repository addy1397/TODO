package com.a.addy.todo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private Context mContext;
    private List<Task> taskList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description, date;
        public ImageButton buttonCopy,buttonDelete;

        public ImageView imageViewPriority;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.cardTitleTextView);
            description = (TextView) view.findViewById(R.id.cardDescriptionTextView);
            date = (TextView) view.findViewById(R.id.cardCompletedTextView);
            buttonCopy = (ImageButton) view.findViewById(R.id.cardCopy);
            buttonDelete = (ImageButton) view.findViewById(R.id.cardDelete);
            imageViewPriority = (ImageView) view.findViewById(R.id.cardPriorityImageView);
        }
    }


    public TaskAdapter(Context mContext, List<Task> taskList) {
        this.mContext = mContext;
        this.taskList = taskList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_task, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Task task = taskList.get(position);
        holder.title.setText(task.getTitle());
        holder.description.setText(task.getDescription());
        holder.date.setText("TO BE COMPLETED BY : " + task.getDate());
        switch(task.getImportance())
        {
            case 1:holder.imageViewPriority.setImageResource(R.color.colorRed); break;
            case 2:holder.imageViewPriority.setImageResource(R.color.colorYellow);break;
            case 3:holder.imageViewPriority.setImageResource(R.color.colorGreen);break;
        }

        holder.buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("TODO", task.getTitle());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(v.getContext(),"Copied to clipboard", Toast.LENGTH_LONG).show();
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Deleted Note",Toast.LENGTH_SHORT).show();
                String titleRemove=task.getTitle();
                String descriptionRemove=task.getDescription();

                int pos = -1;
                for(int i=0;i<taskList.size();i++)
                    if(taskList.get(i).title.equals(titleRemove) && taskList.get(i).description.equals(descriptionRemove))
                        pos=i;
                if(pos != -1)
                    taskList.remove(pos);

                mContext.getSharedPreferences("TASKS",MODE_PRIVATE).edit().clear().commit();

                SharedPreferences sharedPreferences = mContext.getSharedPreferences("TASKS",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(taskList);
                editor.putString("taskList",json);
                editor.apply();

                Intent intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

}