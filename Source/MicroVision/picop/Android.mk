LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := serial_port
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_LDLIBS := \
	-llog \

LOCAL_SRC_FILES := \
	./src/main/jni/empty.c \
	./src/main/jni/picop_interfaces_def_SerialPort.c \

LOCAL_C_INCLUDES += ./src/main/jni

include $(BUILD_SHARED_LIBRARY)
