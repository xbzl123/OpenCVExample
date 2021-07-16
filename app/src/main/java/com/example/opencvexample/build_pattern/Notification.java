package com.example.opencvexample.build_pattern;

public class Notification {
    private String title;
    private String content;
    private int id;
    private int color;

    public Notification(Builder builder) {
        title = builder.title;
        content = builder.content;
        id = builder.id;
        color = builder.color;
    }

    public static class Builder {
       private String title;
       private String content;
       private int id;
       private int color;

        public Builder setColor(int color) {
            this.color = color;
            return this;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }

    @Override
    public String toString() {
        return "Notification{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", id=" + id +
                ", color=" + color +
                '}';
    }
}
