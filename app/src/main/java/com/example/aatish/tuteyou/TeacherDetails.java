package com.example.aatish.tuteyou;

/**
 * Created by Aatish on 28/03/2018.
 */

public class TeacherDetails {

    public static class Teacher {
        private String Name;
        private String Contact;
        private String Qualification;
        private String Money;
        private String Cash;

        public Teacher(){

        }

        public Teacher(String Name, String Qualification, String Money, String Contact,String Cash) {
            this.Name = Name;
            this.Qualification = Qualification;
            this.Money = Money;
            this.Contact= Contact;
            this.Cash= Cash;
        }

        public String getCash() {
            return Cash;
        }

        public String getName() {
            return Name;
        }

        public String getQualification() {
            return Qualification;
        }

        public String getMoney() {
            return Money;
        }

        public String getContact() {
            return Contact;
        }
    }
}
