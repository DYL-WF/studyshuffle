package net.sytes.studyshuffle.models;

import java.util.ArrayList;
import java.util.List;

public class StudentList {
    private List<String> list;
    
    public StudentList(){
        list = new ArrayList<String>();
    }

    public void addStudent(String student){
        list.add(student);
    }

    public void setList(List<String> list){
        this.list = list;
    }
    public List<String> getList(){
        return list;
    }
}
