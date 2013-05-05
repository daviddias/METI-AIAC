#include <jni.h>
#include <stdio.h>



/*
 * Class:     AESCMiddleware
 * Method:    initFromC
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_AESCMiddleware_initFromC
  (JNIEnv *a, jobject b, jint c, jint d){
	printf("I'm INIT");
}

/*
 * Class:     AESCMiddleware
 * Method:    updateFromC
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_AESCMiddleware_updateFromC
  (JNIEnv *a, jobject b, jbyteArray c){
	printf("I'm UPDATE");
}

/*
 * Class:     AESCMiddleware
 * Method:    doFinalFromC
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_AESCMiddleware_doFinalFromC
  (JNIEnv *a, jobject b, jbyteArray c){
	printf("I'm DOFINAL");
}
