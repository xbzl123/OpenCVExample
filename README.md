# OpenCVExample
你可以在下面的链接里面看到程序运行的部分效果。
https://github.com/xbzl123/MyMusicPlayer/issues/1#issue-2856136587

使用方式
先在project的build.gradle里面增加
buildscript {
repositories {
maven {
url 'https://s01.oss.sonatype.org/service/local/repositories/iogithubxbzl123-1000/content/'
}}}
后在app的build.gradle里面引用
implementation 'io.github.xbzl123:picturerecognition:0.0.1'
