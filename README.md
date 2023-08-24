# OpenCVExample

使用方式
先在project的build.gradle里面增加
buildscript {
repositories {
maven {
url 'https://s01.oss.sonatype.org/service/local/repositories/iogithubxbzl123-1000/content/'
}}}
后在app的build.gradle里面引用
implementation 'io.github.xbzl123:picturerecognition:0.0.1'
