package com.example.opencvexample.structrue_pattern.adapter_pattern;
//适配器模式
interface Player{
    void play(String type,String path);
}
interface VideoPlayer{
    void playVideo(String path);
}

interface MusicPlayer{
    void playMusic(String path);
}
class MyVideoPlayer implements VideoPlayer{
    @Override
    public void playVideo(String path) {
        System.out.println("play video"+path);

    }
}
class MyMusicPlayer implements MusicPlayer{

    @Override
    public void playMusic(String path) {
        System.out.println("play music"+path);
    }
}
//具体实现者通过不同类型去调用不同的播放器
class MyPlayer implements Player{
    private MyMusicPlayer musicPlayer;
    private MyVideoPlayer videoPlayer;

    @Override
    public void play(String playType,String path) {
        if(musicPlayer == null){
            musicPlayer = new MyMusicPlayer();
        }
        if(videoPlayer == null){
            videoPlayer = new MyVideoPlayer();
        }
        if(playType.equals("avi")){
            videoPlayer.playVideo(path);
        }else if(playType.equals("mp3")){
            musicPlayer.playMusic(path);
        }
    }
}

class Demo {
    public static void main(String[] args) {
        MyPlayer myPlayer = new MyPlayer();
        myPlayer.play("avi"," test avi");
        myPlayer.play("mp3"," test mp3");
    }
}
