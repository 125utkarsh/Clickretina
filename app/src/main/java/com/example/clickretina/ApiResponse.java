package com.example.clickretina;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {
    @SerializedName("choices")
    private Choice[] choices;

    public Choice[] getChoices() {
        return choices;
    }

    public void setChoices(Choice[] choices) {
        this.choices = choices;
    }

    public static class Choice {
        @SerializedName("message")
        private Message message;

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }
    }

    public static class Message {
        @SerializedName("content")
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
