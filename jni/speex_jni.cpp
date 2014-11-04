#include <jni.h>

#include <string.h>
#include <unistd.h>

#include <speex/speex.h>

static int codec_open = 0;

static int dec_frame_size;
static int enc_frame_size;

static SpeexBits ebits, dbits;
void *enc_state;
void *dec_state;

static JavaVM *gJavaVM;

#ifdef __cplusplus
extern "C" {
#endif
extern int speexenc(char *, char *);
extern int speexdec(char *, char *);
#ifdef __cplusplus
}
#endif

extern "C" JNIEXPORT jint JNICALL Java_com_xikang_family_common_Speex_speexenc(
		JNIEnv *env, jobject obj, jstring inFile, jstring outFile) {

	char * sinFile = (char *) env->GetStringUTFChars(inFile, 0);
	char * soutFile = (char *) env->GetStringUTFChars(outFile, 0);

	return (jint) speexenc(sinFile, soutFile);
}

extern "C" JNIEXPORT jint JNICALL Java_com_xikang_family_common_Speex_speexdec(
		JNIEnv *env, jobject obj, jstring inFile, jstring outFile) {

	char * sinFile = (char *) env->GetStringUTFChars(inFile, 0);
	char * soutFile = (char *) env->GetStringUTFChars(outFile, 0);

	return (jint) speexdec(sinFile, soutFile);
}

