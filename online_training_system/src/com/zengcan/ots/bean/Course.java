package com.zengcan.ots.bean;

import java.util.Objects;

/**
 * 一个普通的java类，这个java类可以封装零散的数据。代表了一个课程对象。
 */
public class Course {
    // 课程编号
    private String course_number;
    // 修改前的课程名称
    private String course_name1;
    // 修改后的课程名称
    private String course_name;
    // 课程类型
    private String course_type;
    // 文件路径
    private String file_path;
    // 开课时间（如：2023-06-08 至 2023-09-30）
    private String class_start_time;

    public Course() {
    }

    public String getCourse_number() {
        return course_number;
    }

    public void setCourse_number(String course_number) {
        this.course_number = course_number;
    }

    public String getCourse_name1() {
        return course_name1;
    }

    public void setCourse_name1(String course_name1) {
        this.course_name1 = course_name1;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_type() {
        return course_type;
    }

    public void setCourse_type(String course_type) {
        this.course_type = course_type;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getClass_start_time() {
        return class_start_time;
    }

    public void setClass_start_time(String class_start_time) {
        this.class_start_time = class_start_time;
    }

    @Override
    public String toString() {
        return "Course{" +
                "course_number='" + course_number + '\'' +
                ", course_name1='" + course_name1 + '\'' +
                ", course_name='" + course_name + '\'' +
                ", course_type='" + course_type + '\'' +
                ", file_path='" + file_path + '\'' +
                ", class_start_time='" + class_start_time + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(course_number, course.course_number) && Objects.equals(course_name1, course.course_name1) && Objects.equals(course_name, course.course_name) && Objects.equals(course_type, course.course_type) && Objects.equals(file_path, course.file_path) && Objects.equals(class_start_time, course.class_start_time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course_number, course_name1, course_name, course_type, file_path, class_start_time);
    }
}
