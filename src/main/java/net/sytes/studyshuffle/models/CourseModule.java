package net.sytes.studyshuffle.models;

import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "modules")
public class CourseModule {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;


  @Column(name="start_time")
  private Time start_time;

  @Column(name="end_time")
  private Time end_time;

  @Column(name="weekday")
  private int weekday;

  public CourseModule() {

  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
  public void setStartTime(Time start_time) {
    this.start_time = start_time;
  }  
  public void setEndTime(Time end_time) {
    this.end_time = end_time;
  }  

  public void setWeekday(int weekday) {
    this.weekday = weekday;
  }  

  public int getWeekday() {
    return this.weekday;
  }  
  
  public Time getStartTime() {
    return this.start_time;
  }  
  public Time getEndTime(Time end_time) {
    return this.end_time;
  }  
}