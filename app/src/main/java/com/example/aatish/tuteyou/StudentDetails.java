package com.example.aatish.tuteyou;

/**
 * Created by Aatish on 27/03/2018.
 */

public class StudentDetails {

    public static class Student {
        private String Name;
        private String Place;
        private String Class;

        public Student(){

        }

        public Student(String Name, String Place, String Class) {
            this.Name = Name;
            this.Place = Place;
            this.Class = Class;
        }

        public String getName1() {
            return Name;
        }

        public String getPlace() {
            return Place;
        }

        public String getClass1() {
            return Class;
        }
    }
}
