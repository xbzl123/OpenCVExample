package com.example.opencvexample.create_pattern.buider_pattern;
class JButton {
}
class Dailog{
    private String title;
    private String content;
    private JButton button;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }
}
class DailogBuilder{
    public static Dailog createDailog(){
        Dailog dailog = new Dailog();
        dailog.setTitle("test");
        dailog.setContent("this a test!");
        dailog.setButton(new JButton());
        return dailog;
    }
}

class Demo {
    public static void main(String[] args) {
        Dailog dailog = DailogBuilder.createDailog();
        System.out.println(dailog.getContent());
        System.gc();
    }
}
