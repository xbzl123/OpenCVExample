# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_CXX_STANDARD 14)
message("CMAKE_CXX_STANDARD = ${CMAKE_CXX_STANDARD}")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")
#头文件配置
include_directories(include)

#编译头文件
file(GLOB my_source_path ${CMAKE_SOURCE_DIR}/*.cpp ${CMAKE_SOURCE_DIR}/*.c)
file(GLOB bzip ${CMAKE_SOURCE_DIR}/bzip/*.c)

add_library(OpenCV SHARED
        ${my_source_path}
        ${bzip}
        )

#add_library(lib_RSNet SHARED IMPORTED)
#set_target_properties(lib_RSNet PROPERTIES IMPORTED_LOCATION
#        ${distribution_DIR}/RSNet/Lib/${ANDROID_ABI}/libRSNet.so)
add_library(lib_opencv SHARED IMPORTED)
set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI}/libopencv_java4.so)


add_library(avutil
        SHARED
        IMPORTED )
set_target_properties(avutil
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/../jniLibs/armeabi-v7a/libavutil.so )

add_library(swresample
        SHARED
        IMPORTED )
set_target_properties(swresample
        PROPERTIES IMPORTED_LOCATION
         ${CMAKE_SOURCE_DIR}/../jniLibs/armeabi-v7a/libswresample.so )
add_library(avcodec
        SHARED
        IMPORTED )
set_target_properties(avcodec
        PROPERTIES IMPORTED_LOCATION
         ${CMAKE_SOURCE_DIR}/../jniLibs/armeabi-v7a/libavcodec.so )
add_library(avfilter
        SHARED
        IMPORTED)
set_target_properties(avfilter
        PROPERTIES IMPORTED_LOCATION
         ${CMAKE_SOURCE_DIR}/../jniLibs/armeabi-v7a/libavfilter.so )
add_library(swscale
        SHARED
        IMPORTED)
set_target_properties(swscale
        PROPERTIES IMPORTED_LOCATION
         ${CMAKE_SOURCE_DIR}/../jniLibs/armeabi-v7a/libswscale.so )
add_library(avdevice
        SHARED
        IMPORTED)
set_target_properties(avdevice
        PROPERTIES IMPORTED_LOCATION
         ${CMAKE_SOURCE_DIR}/../jniLibs/armeabi-v7a/libavdevice-57.so )
add_library(avformat
        SHARED
        IMPORTED)
set_target_properties(avformat
        PROPERTIES IMPORTED_LOCATION
         ${CMAKE_SOURCE_DIR}/../jniLibs/armeabi-v7a/libavformat.so )
find_library( # Sets the name of the path variable.
              log-lib
              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )
find_library(jnigraphics-lib jnigraphics)
find_library(android-lib android)

target_link_libraries( # Specifies the target library.
                       OpenCV
                       # Links the target library to the log library
                       # included in the NDK.
        avutil swresample avcodec avfilter swscale avdevice avformat
                       ${log-lib}
                       ${jnigraphics-lib}
                       ${android-lib}
                        android
                        z
                       lib_opencv
                        OpenSLES)