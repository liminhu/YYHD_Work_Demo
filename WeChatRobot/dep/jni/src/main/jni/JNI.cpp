#include "Utils.h"



JNIEXPORT jbyteArray JNICALL jniDoProcessCheat(JNIEnv *env, jclass clazz, jint flag, jint arg1,
                                         jstring arg2) {
    LogD("my_<%s> -- test:%s", __FUNCTION__, "111");
    const char *dname = NULL;
    dname = env->GetStringUTFChars(arg2, 0);
	char *data=doProcessCheat(flag, arg1, (char *) dname);
	env->ReleaseStringUTFChars(arg2, dname);
	jbyteArray array=env->NewByteArray(strlen(data)+1);
	env->SetByteArrayRegion(array, 0, strlen(data), (signed char*)data);
	return array;
}




static const JNINativeMethod gHookMethods[] = {
       {"doProcessCheat", "(IILjava/lang/String;)[B", (void *) jniDoProcessCheat},
};

extern "C" __attribute__ ((visibility("default"))) jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
	#ifdef NDK_DEBUG
	LogD(" my_<%s> JNI --- begin  %s %s", __FUNCTION__, __DATE__, __TIME__);
	#endif
	JNIEnv* env = NULL;
	if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
		return -1;
	}
	jclass clazz;
	clazz = env->FindClass("com/gameassist/plugin/nativeutil/NativeUtils");
	if (clazz == NULL) {
		return -1;
	}
	
	if (env->RegisterNatives(clazz, gHookMethods, sizeof(gHookMethods) / sizeof(gHookMethods[0])) < 0) {
		return -1;
	}
	return JNI_VERSION_1_4;
}
