# NWU NET

## 它可以用来做什么：

完成 **西北大学校园网** 的信息认证(针对南校区, 由wenet承包)。 解决电脑终端进入不了认证界面、移动客户端需要频繁认证的情况。

当然, 你可以将其嵌入到其他程序中作为子模块。

## 它的文件结构是什么：

```
.
├── LICENSE
├── NWUNET
│   ├── app
│   │   ├── build
│   │   ├── build.gradle
│   │   ├── libs
│   │   ├── proguard-rules.pro
│   │   ├── release
│   │   ├── sampledata
│   │   └── src
│   ├── build
│   │   ├── intermediates
│   │   └── kotlin
│   ├── build.gradle
│   ├── gradle
│   │   └── wrapper
│   ├── gradle.properties
│   ├── gradlew
│   ├── gradlew.bat
│   ├── local.properties
│   └── settings.gradle
├── nwunet_android_V2_1.apk
├── nwunet_pc.py
└── README.md
```

你可能需要关注以下文件或文件夹:

  - nwunet_android_V2_1.apk: 移动客户端apk安装包
  - nwunet_pc.py: 运行于电脑终端的python格式文件
  - NWUNET: 包含构建整个安卓项目的源代码。 若你想改进移动端内容, 推荐将该文件夹整个下载至本地并用Android Studio打开, 在更改完成之后重新构建。

## 它的使用方法是什么:

  - 移动客户端 (安卓) : 

    你可以很轻松的将顶层目录下的 *nwunet_android_V2_1.apk* 文件下载至移动端, 然后进行安装, 像使用其它APP一样使用该软件

  - 电脑终端 (PC): 

    想要在终端上进行校园网身份认证, 请将顶层目录下的 *nwunet_pc.py* 文件下载到本地。 之后你需要完成三个步骤
    
    - 安装python运行环境
    - 安装两个第三方库:  psutil, requests
      > python安装第三方库十分简便, 若不懂请自行Google
    - 使用文本编辑器进入程序, 完善个人认证信息。 具体位置在代码中的**第九行与第十行**, 请将其修改为自己的学号和密码。 程序中提供有注释, 其它个性定制请自行修改程序。
    
    在此之后你可以将该程序放置到桌面上, 每次双击即可完成认证。理论上同时支持Windows与Linux。

  - 同时你也可以使用kotlin来在PC端完成校园网的认证, 而不是python ~~因为移动版是用kotlin来进行开发的~~。 移动端相比于PC版, 其运行速度比python快, 并且它使用了异步请求框架。 但完成这一步需要你自己进入NWUNET项目文件夹中将其具体内容抽离出来, 这可能会有一定难度, 如果你执着于提高的那部分性能的话。 当然你可以发issue来联系作者协同帮助, 作者未将其抽调出来, 可能是因为没有继续完善的动力 ~~或者懒~~。

## 作者

  愧对西大学子身份的蒟蒻本科生

## 授权许可

无偿开源, 请遵循[GNU GPLv3](https://www.gnu.org/licenses/gpl-3.0.html)开源许可