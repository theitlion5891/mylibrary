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
Java_com_fantafeat_util_ApiManager_getOpinioBaseUrl(JNIEnv *env, jclass clazz) {
    std::string hello = "https://bb11webappfqdn09.ezydots.com/bb11_redesign/opinion-api/rest-api/v1/";
    return env->NewStringUTF(hello.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_fantafeat_util_ApiManager_getOpinioDashBaseUrl(JNIEnv *env, jclass clazz) {
    std::string hello = "http://52.66.75.142/bb11_redesign/opinion-api/rest-api/v1/";//
    //std::string hello = "http://13.234.30.150/bb11_redesign/opinion-api/rest-api/v1/";//
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_fantafeat_util_ApiManager_getImageBaseURL(JNIEnv *env, jclass clazz) {
    std::string key = "https://ffcdn.fantafeat.com/ff_api/myAppImages/";// https://ff.lybcdn.net/itisme/  "https://cdn.fantafeat.com/itisme/";
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
Java_com_fantafeat_Session_MyApp_getPhonePeMID(JNIEnv *env, jobject thiz) {
    std::string url = "FANTAFEATUAT";//BB11UAT

    return env->NewStringUTF(url.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_fantafeat_Activity_PaymentActivity_getMID(JNIEnv *env, jobject thiz) {
    std::string mid = "hzLprx24106020261512";//"ovPduz37010723197692";
    return env->NewStringUTF(mid.c_str());
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
Java_com_fantafeat_Activity_PaymentActivity_getPaytmHost(JNIEnv *env, jobject thiz) {
    std::string host = "https://securegw.paytm.in/";//"ovPduz37010723197692";
    return env->NewStringUTF(host.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_fantafeat_Activity_PaymentActivity_getAPPID(JNIEnv *env, jobject thiz) {
    std::string url = "89463e016845c436a0346308236498";
    return env->NewStringUTF(url.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_fantafeat_util_ApiManager_getUpdateURL(JNIEnv *env, jclass clazz) {
    std::string url = "https://api.fantafeat.com/ff_api/API/check_app.php/";
    return env->NewStringUTF(url.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_fantafeat_Session_MyApp_getSocketServer(JNIEnv *env, jobject thiz) {
    //std::string url = "http://65.1.2.61:2021/";//UAT /*http://192.168.100.106:5891/"http://13.233.248.201:5891/";*/
    std::string url = "https://topscore.ezydots.com";//LIVE
    //std::string url = "http://13.233.248.201:5891/";
    return env->NewStringUTF(url.c_str());
}
