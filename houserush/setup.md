## 运行项目必须安装项
1. 安装git。 检查是否系统已经安装了git，使用命令: `git --version`，如果已安装，会显示git版本号。如果没有安装，see [git下载安装指南](https://git-scm.com/downloads) 。
2. 安装docker CommunityEdition，根据你的操作系统从以下链接查看下载安装指南：
   * [Mac (macOS)](https://docs.docker.com/docker-for-mac/install/)
   * [Microsoft Windows 10](https://docs.docker.com/docker-for-windows/install/)
   * Linux：
      + [CentOS](https://docs.docker.com/install/linux/docker-ce/centos/)
      + [Debian](https://docs.docker.com/install/linux/docker-ce/debian/)
      + [Fedora](https://docs.docker.com/install/linux/docker-ce/fedora/)
      + [Ubuntu](https://docs.docker.com/install/linux/docker-ce/ubuntu/)     
   安装前可能需要注册docker账号。如果仍有问题，see [docker 安装指南](https://docs.docker.com/install/)  
   如果是windows系统，你需要时 Windows 10 64-bit: Pro, Enterprise, or Education。 其他windows版本 see [Docker Toolbox](https://docs.docker.com/docker-for-windows/install/)
3. 安装docker-compose。如果你的系统是Mac or Windows，docker-compose已经一并安装了。请用`docker-compose --version`命令确认。  
对于linux系统, 请执行以下两条命令:
```bash
sudo curl -L "https://github.com/docker/compose/releases/download/1.24.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

sudo chmod +x /usr/local/bin/docker-compose
```
如果安全仍有问题，请检查dependency packages，see [docker-compose 安装指南](https://docs.docker.com/compose/install/) 中linux的tab。

## 可选安装项

项目主要部分由java开发，如果想修改源代码并重新编译运行，请安装并安装 [JDK](https://www.oracle.com/technetwork/java/javase/downloads/index.html) , 并准备一个JAVA IDE，如: [IDEA](https://www.jetbrains.com/idea/) 。

## 如果安装上述依赖项失败
   如果安装 git/docker 遇到问题，没有时间去解决。也不用气馁，请仍然带电脑到现场，我们有简化版本的Demo可以供大家运行体验。
