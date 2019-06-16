package microautomation.attendencesystem.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import microautomation.attendencesystem.Model.Attendence;
import microautomation.attendencesystem.R;

public class attendence_list_adapter extends RecyclerView.Adapter<attendence_list_adapter.attendence_list_viewholder>{
    ArrayList<Attendence> attendences;
    Context context;

    public attendence_list_adapter(ArrayList<Attendence> attendences, Context context) {
        this.attendences = attendences;
        this.context = context;
    }

    @NonNull
    @Override
    public attendence_list_viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendence_list_layout,viewGroup,false);
        return new attendence_list_viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull attendence_list_viewholder attendence_list_viewholder, int i) {
      attendence_list_viewholder.date.setText(attendences.get(i).getDate());
      if(attendences.get(i).getAttendence()) {
          attendence_list_viewholder.attendence.setText("Present");
      }else{
          attendence_list_viewholder.attendence.setText("Absent");
      }
    }

    @Override
    public int getItemCount() {
        return attendences.size();
    }
class attendence_list_viewholder extends RecyclerView.ViewHolder{
TextView date,attendence;
    public attendence_list_viewholder(@NonNull View itemView) {
        super(itemView);
        date=itemView.findViewById(R.id.date);
        attendence=itemView.findViewById(R.id.attendence);
    }
}
}
