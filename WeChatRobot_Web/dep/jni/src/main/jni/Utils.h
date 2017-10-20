//
// Created by Hulimin on 2017/9/4.
//

#ifndef WECHATROBOT_UTILS_H
#define WECHATROBOT_UTILS_H

#include <jni.h>
#include <android/log.h>
#include <dirent.h>
#include <stdlib.h>
#include <stdarg.h>
#include <errno.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <time.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <dlfcn.h>
#include <elf.h>


#define LOGD __android_log_print
#define LogD(fmt, ...)    {LOGD(ANDROID_LOG_DEBUG, "gameassist", fmt, ##__VA_ARGS__);printf(fmt,##__VA_ARGS__);}

char* doProcessCheat(int flag, int arg1, char *arg2);


#endif //WECHATROBOT_UTILS_H
