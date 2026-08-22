# yuv

set(YUV_DIR "${THIRDPARTY_DIR}/libyuv")

set(YUV_SOURCES
  "${YUV_DIR}/source/compare.cc"
  "${YUV_DIR}/source/compare_common.cc"
  "${YUV_DIR}/source/compare_gcc.cc"
  "${YUV_DIR}/source/compare_win.cc"
  "${YUV_DIR}/source/convert.cc"
  "${YUV_DIR}/source/convert_argb.cc"
  "${YUV_DIR}/source/convert_from.cc"
  "${YUV_DIR}/source/convert_from_argb.cc"
  "${YUV_DIR}/source/convert_jpeg.cc"
  "${YUV_DIR}/source/convert_to_argb.cc"
  "${YUV_DIR}/source/convert_to_i420.cc"
  "${YUV_DIR}/source/cpu_id.cc"
  "${YUV_DIR}/source/mjpeg_decoder.cc"
  "${YUV_DIR}/source/mjpeg_validate.cc"
  "${YUV_DIR}/source/planar_functions.cc"
  "${YUV_DIR}/source/rotate.cc"
  "${YUV_DIR}/source/rotate_any.cc"
  "${YUV_DIR}/source/rotate_argb.cc"
  "${YUV_DIR}/source/rotate_common.cc"
  "${YUV_DIR}/source/rotate_gcc.cc"
  "${YUV_DIR}/source/rotate_lsx.cc"
  "${YUV_DIR}/source/rotate_win.cc"
  "${YUV_DIR}/source/row_any.cc"
  "${YUV_DIR}/source/row_common.cc"
  "${YUV_DIR}/source/row_gcc.cc"
  "${YUV_DIR}/source/row_lasx.cc"
  "${YUV_DIR}/source/row_lsx.cc"
  "${YUV_DIR}/source/row_rvv.cc"
  "${YUV_DIR}/source/row_win.cc"
  "${YUV_DIR}/source/scale.cc"
  "${YUV_DIR}/source/scale_any.cc"
  "${YUV_DIR}/source/scale_argb.cc"
  "${YUV_DIR}/source/scale_common.cc"
  "${YUV_DIR}/source/scale_gcc.cc"
  "${YUV_DIR}/source/scale_lsx.cc"
  "${YUV_DIR}/source/scale_rgb.cc"
  "${YUV_DIR}/source/scale_rvv.cc"
  "${YUV_DIR}/source/scale_uv.cc"
  "${YUV_DIR}/source/scale_win.cc"
  "${YUV_DIR}/source/video_common.cc"
)

if(${ANDROID_ABI} STREQUAL "armeabi-v7a")
  list(APPEND YUV_SOURCES
    "${YUV_DIR}/source/compare_neon.cc"
    "${YUV_DIR}/source/rotate_neon.cc"
    "${YUV_DIR}/source/row_neon.cc"
    "${YUV_DIR}/source/scale_neon.cc"
  )
elseif(${ANDROID_ABI} STREQUAL "arm64-v8a")
  list(APPEND YUV_SOURCES
    "${YUV_DIR}/source/compare_neon64.cc"
    "${YUV_DIR}/source/rotate_neon64.cc"
    "${YUV_DIR}/source/row_neon64.cc"
    "${YUV_DIR}/source/scale_neon64.cc"
  )
endif()

add_library(yuv STATIC
  ${YUV_SOURCES}
)

if(${ANDROID_ABI} STREQUAL "armeabi-v7a")
  target_compile_definitions(yuv PRIVATE LIBYUV_NEON=1)
  target_compile_options(yuv PRIVATE -mfpu=neon)
endif()

target_include_directories(yuv PUBLIC
  "${YUV_DIR}/include"
)

target_compile_options(yuv PRIVATE
  -fexceptions
  -fno-unwind-tables -fno-asynchronous-unwind-tables
  -Wnon-virtual-dtor -Woverloaded-virtual
  -Wno-unused-parameter
)