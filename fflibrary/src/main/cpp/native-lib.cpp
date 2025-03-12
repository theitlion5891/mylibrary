#include <jni.h>
#include <string>


extern "C"
JNIEXPORT jstring JNICALL
Java_com_fantafeat_util_MCrypt_stringFromJNIKey(JNIEnv *env, jobject thiz) {
    std::string key = "VbkD@2019@DAY!Navri";
    return env->NewStringUTF(key.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_fantafeat_Session_MyApp_getImageBase(JNIEnv *env, jclass clazz) {
    std::string key = "https://ffcdn.fantafeat.com/ff_api/myAppImages/";//https://ff.lybcdn.net/itisme/
    return env->NewStringUTF(key.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_fantafeat_util_ApiManager_getBASEURL(JNIEnv *env, jclass clazz) {
    std::string key = "https://api.fantafeat.com/ff_api/";
    return env->NewStringUTF(key.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_fantafeat_util_ApiManager_getDashBASEURL(JNIEnv *env, jclass clazz) {
    std::string key = "https://ffdash.fantafeat.com/ff_api/";//ff-uat-app-server.ezydots.com/ff_api/
    return env->NewStringUTF(key.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_fantafeat_util_ApiManager_getV3BASEURL(JNIEnv *env, jclass clazz) {
    std::string key = "https://ffwebappfqdn06.fantafeat.com/ff_api/V3/";
    return env->NewStringUTF(key.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_fantafeat_util_ApiManager_getV3DASHBASEURL(JNIEnv *env, jclass clazz) {
    std::string key = "https://ffdash.fantafeat.com/ff_api/V3/";//ff-uat-app-server.ezydots.com
    return env->NewStringUTF(key.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_fantafeat_util_ApiManager_getLudoBaseURL(JNIEnv *env, jclass clazz){
    std::string key = "https://gamesapi01.ezydots.com/gamesApi/";// http://43.205.60.250/gamesApi/    http://3.108.103.153/ff-ludo-api/
    // UAT:- http://3.111.74.57/ludo/   LIVE: - http://3.108.103.153/ff-ludo-api/
    return env->NewStringUTF(key.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fantafeat_util_ApiManager_getImageBaseURL(JNIEnv *env, jclass clazz) {
    std::string key = "https://ffcdn.fantafeat.com/ff_api/myAppImages/";// https://ff.lybcdn.net/itisme/  "https://cdn.fantafeat.com/itisme/";
    return env->NewStringUTF(key.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_fantafeat_util_MCrypt_getType(JNIEnv *env, jobject thiz) {
    std::string type = "SHA-224";
    return env->NewStringUTF(type.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_fantafeat_util_MCrypt_getType2(JNIEnv *env, jobject thiz) {
    std::string type = "SHA-384";
    return env->NewStringUTF(type.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_fantafeat_util_MCrypt_getBB(JNIEnv *env, jobject thiz) {
    std::string type = "B!!B";
    return env->NewStringUTF(type.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_fantafeat_util_MCrypt_getVar(JNIEnv *env, jobject thiz) {
    std::string type = "V@RN";
    return env->NewStringUTF(type.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_fantafeat_util_ApiManager_getUpdateURL(JNIEnv *env, jclass clazz) {
    std::string url = "https://api.fantafeat.com/ff_api/API/check_app.php/";
    return env->NewStringUTF(url.c_str());
}

