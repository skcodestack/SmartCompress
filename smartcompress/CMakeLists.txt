# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

include_directories(src/main/cpp/include)

# ----------------------------------------------------------------------------------
# 查找log 的动态库，找到后路径赋给log-lib
# 从哪里找？ ${NDK}\platforms\android-21\arch-arm\usr\lib\liblog.so
find_library( # Sets the name of the path variable.
        log-lib
        log)
find_library(graphics jnigraphics)

# 设置变量
# CMAKE_CXX_FLAGS c++参数，会传给编译器
# CMAKE_C_FLAGS c参数，会传给编译器
# 给CMAKE_CXX_FLAGS变量加上参数
# ${CMAKE_SOURCE_DIR} 当前文件目录
# ${ANDROID_ABI}   cpu架构目录
# -L 添加动态库路径
# set(CMAKE_CXX_FLAGS  "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}" )

add_library(    libjpeg
        SHARED
        IMPORTED )


set_target_properties(  libjpeg
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libjpeg.so)


# 源文件少
add_library( # Sets the name of the library.
        compressImage

        # Sets the library as a shared library.
        SHARED
        # Provides a relative path to your source file(s).
        src/main/cpp/compressImage.cpp)

# 源文件多
# file(GLOB source src/main/cpp/*.cpp /src/main/cpp/a/*.c)
# add_library( native-lib SHARED ${source})


# 链接动态库  -l Test
target_link_libraries(
        compressImage
        ${log-lib}
        ${graphics}
        libjpeg
        #${libjpeg}
        )