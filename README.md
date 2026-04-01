# YOLO-Vehicle-Android

[![License](https://img.shields.io/github/license/TooBeyond/YOLO-Vehicle-Android)](LICENSE)
[![Android](https://img.shields.io/badge/Android-8.0%2B-brightgreen)]()
[![Platform](https://img.shields.io/badge/Platform-Android-brightgreen)]()
[![NCNN](https://img.shields.io/badge/NCNN-Latest-blue)]()
[![YOLOv5](https://img.shields.io/badge/YOLOv5-v5.0-orange)]()

**Android 端高性能实时车辆检测与多维特征识别系统**

本项目是一个基于 **ncnn** 推理引擎与 **YOLOv5** 架构的移动端视觉方案。它集成了实时车辆目标检测、全颜色车牌识别、特殊车辆（如危险品运输车）监控以及多种传感器数据对接。通过底层 C++ 优化与 OpenCV Native 加速，实现了在 Android 端侧的高帧率、低延迟推理。

[English](./README_EN.md) | [中文](./README.md)

---

## 🚀 核心特性 (Features)

- 🚗 **实时目标检测** - 基于 YOLOv5 深度优化的移动端模型，实时识别多种车型。
- 🎯 **全颜色车牌识别** - 支持蓝、黄、绿、白、黑五种颜色车牌的精准定位与字符识别。
- 🔄 **自动透视矫正** - 针对倾斜或非正视角度拍摄的车牌，利用 OpenCV 进行透视变换（Perspective Transform）矫正。
- ⚠️ **特殊目标监控** - 针对危险品运输车辆等特定交通目标进行实时预警。
- ⚡ **端侧推理加速** - 采用腾讯 ncnn 框架，充分利用移动端 CPU/GPU 算力，无需联网即可运行。
- 🏷️ **多模态数据** - 集成二维码/条码扫码识别以及 RFID 射频标签读取支持。

---

## 🏗️ 系统架构 (Architecture)

```mermaid
graph TB
    subgraph 客户端逻辑层 (Client Layer)
        A[Camera/Gallery] --> B[OpenCV 图像预处理]
        B --> C{ncnn 推理引擎}
        C --> D[YOLOv5 目标定位]
        D --> E[车牌 ROI 提取]
        E --> F[透视变换矫正]
        F --> G[CRNN 字符识别]
        G --> H[结果展示与 UI 渲染]
    end
    
    subgraph 模型资产 (Model Assets)
        I[YOLOv5s Detection] -.-> C
        J[CRNN Recognition] -.-> G
        K[Color Classifier] -.-> G
    end
    
    subgraph 底层加速 (Native Backend)
        L[Vulkan/CPU Parallel] -.-> C
        M[OpenCV Android SDK] -.-> B
    end

## 🛠️ 技术栈

| 层级 | 技术 |
|------|------|
| **框架** | Android Native |
| **AI 推理** | ncnn (腾讯开源) |
| **检测模型** | YOLOv5 |
| **识别模型** | CRNN + CTC |
| **图像处理** | OpenCV Android |
| **构建工具** | Gradle + CMake |

## 📋 环境要求

- Android Studio Arctic Fox+
- NDK 24.0.8215888
- CMake 3.10.2+
- Android SDK 28+

## 🚦 快速开始

### 1. 克隆项目
```bash
git clone https://github.com/TooBeyond/YOLO-Vehicle-Android.git
cd YOLO-Vehicle-Android
```

### 2. 配置 NDK
在 `local.properties` 中配置：
```properties
ndk.dir=/path/to/ndk/24.0.8215888
sdk.dir=/path/to/android-sdk
```

### 3. 编译运行
```bash
./gradlew assembleDebug
```

### 4. 安装 APK
生成的 APK 位于 `app/build/outputs/apk/debug/`

## 📁 项目结构

```
YOLO-Vehicle-Android/
├── app/
│   ├── src/main/
│   │   ├── java/com/tencent/yolov5ncnn/
│   │   │   ├── MainActivity.java      # 主界面
│   │   │   ├── PlateRecognition.java  # 车牌识别入口
│   │   │   ├── YoloV5Ncnn.java         # YOLO 检测核心
│   │   │   ├── library/                # 识别算法库
│   │   │   └── AlgorithmUtil/          # 算法工具
│   │   ├── assets/                     # AI 模型文件
│   │   │   ├── best.bin                # 检测模型
│   │   │   ├── crnn.bin                # 识别模型
│   │   │   └── plate_rec_color.bin     # 颜色模型
│   │   ├── res/                        # 资源文件
│   │   └── jni/                        # JNI 底层实现
│   └── build.gradle
├── sdk/                    # SDK 封装
├── gradle/                 # Gradle 配置
└── README.md
```

## 📊 模型说明

| 模型 | 大小 | 用途 |
|------|------|------|
| `best.bin` | 6.6MB | 车牌/车辆检测 |
| `crnn.bin` | 7.4MB | 字符识别 |
| `plate_rec_color.bin` | 2.6MB | 颜色识别 |

## ⚡ 性能

- 检测速度：~30ms/帧 (骁龙 865)
- 识别准确率：>95%
- 支持分辨率：1920x1080 以下

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

⭐ Star 支持一下，让更多人看到这个项目！
