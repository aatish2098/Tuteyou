package com.example.aatish.tuteyou;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Aatish on 29/03/2018.
 */

public class TeacherList extends ArrayAdapter<TeacherDetails.Teacher> {

    private Activity context;
    private List<TeacherDetails.Teacher> teacherList;
    public TeacherList(Activity context, List<TeacherDetails.Teacher> teacherList) {
        super(context, R.layout.activity_student_home, teacherList);
        this.context = context;
        this.teacherList = teacherList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_student_home, null, true);

        /*TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewNumber = (TextView) listViewItem.findViewById(R.id.textViewNumber);
        TextView textViewQual= (TextView) listViewItem.findViewById(R.id.textViewQual);
        TextView textViewwages= (TextView) listViewItem.findViewById(R.id.textViewwages);
        TextView textViewType= (TextView) listViewItem.findViewById(R.id.textViewType);*/

        TeacherDetails.Teacher teacher = teacherList.get(position);
        /*textViewName.setText(teacher.getName());
        textViewNumber.setText(teacher.getContact());
        textViewQual.setText(teacher.getQualification());
        textViewwages.setText(teacher.getCash());
        textViewType.setText(teacher.getMoney());*/


        return listViewItem;
    }
}
