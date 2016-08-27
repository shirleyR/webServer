# webServer
A simple http web server 
 - 使用线程池处理客户端请求
 - XML 解析（web.xml）中的Servlet
 - PipeInput/PipeOutput
  ![](https://www.ibm.com/developerworks/cn/java/j-nioserver/figure1.gif)
 - NIO Selector 
 
### 顺带复习NIO BIO AIO
- BIO，同步阻塞式IO，简单理解：一个连接一个线程
- NIO，同步非阻塞IO，简单理解：一个请求一个线程
- AIO，异步非阻塞IO，简单理解：一个有效请求一个线程
- NIO本身是基于事件驱动思想来完成的，其主要想解决的是BIO的大并发问题：在使用同步I/O的网络应用中，如果要同时处理多个客户端请求，或是在客户端要同时和多个服务器进行通讯，就必须使用多线程来处理。也就是说，将每一个客户端请求分配给一个线程来单独处理。
  也就是说BIO是一个请求一个线程，NIO是在一个线程上注册请求
### Servlet 
Servlet IO操作是基于InputStream 和OutputStream
NIO 是基于Selector、Channel、ByteBuffer进行工作
[NIO 与 Servlet的融合](https://www.ibm.com/developerworks/cn/java/j-nioserver/)
