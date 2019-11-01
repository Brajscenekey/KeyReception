package com.key.keyreception.activity.model;

import java.util.List;

public class HelpModal  {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * question : How can i create post for searching artist.
         * answer : This is some of the best advice anyone ever gave me about web design (thanks Cameron). A lot of designers start off a design by focusing on the header. Often times what's inside the page is what makes it look good; the header is supplementary. Try leaving the header alone for awhile and working on some elements in the body, you'll be surprised at how much easier it is to design a page once you've got a solid body going. The next time you're designing a header with no body imagine yourself adjusting a tie in front of the mirror, but being completely nude. (yikes)
         */

        private String id;
        private String question;
        private String answer;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }
}
