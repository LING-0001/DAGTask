# DAGTask
通过有向无环图的拓扑排序，将依赖任务线性拓扑，解放回调地狱，按照依赖关系加载任务。

# 使用文档

## 基本使用

第一步，对应build.gradle配置远程依赖，已经发布到maven central，不用担心jcenter弃用

```java
implementation 'work.lingling.dagtask:dagtsk:1.0.0'
```

第二步，继承IDAGTask类，在run方法中实现对应的初始化逻辑

```java
public class ATask extends IDAGTask {

    public ATask(String alias) {
        super(alias);
    }

    @Override
    public void run() {
        super.run();
        try {
            // 模拟随机时间
            Random random = new Random();
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 第三方框架内部使用同步加载
        // completeDAGTask方法写在run方法末尾即可
        completeDAGTask();
    }

    // 第三方框架内部使用异步加载
    // completeDAGTask方法需要写进成功回调
    /*onLibrarySuccess(){
        completeDAGTask();
    }*/

}
```

tips：加载任务内部未开线程，completeDAGTask方法写在run方法的末尾，感知初始化结束；加载任务内部使用多线程，需要将completeDAGTask方法写进加载成功回调。

第三步，根据任务的依赖关系构建DAGProject并执行，如下图依赖关系：

<img src="https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/6d642f75389a492b8576e230713eb199~tplv-k3u1fbpfcp-watermark.image?" alt="ab.png" style="zoom:50%;" />

我们模拟对应的任务，任务A、B、C、D、E，构建DAGProject如下：

```java
ATask a = new ATask("ATask");
BTask b = new BTask("BTask");
CTask c = new CTask("CTask");
DTask d = new DTask("DTask");
ETask e = new ETask("ETask");
DAGProject dagProject = new DAGProject.Builder()
    .addDAGTask(b)
    .addDAGTask(c)
    .addDAGTask(a)
    .addDAGTask(d)
    .addDAGTask(e)
    .addDAGEdge(b, a)
    .addDAGEdge(c, a)
    .addDAGEdge(d, b)
    .addDAGEdge(d, c)
    .addDAGEdge(e, b)
    .builder();
DAGScheduler dagScheduler = new DAGScheduler();
dagScheduler.start(dagProject);
```

依赖任务执行结果如下：

![V$D~UWE3ZDO5LPT4HCD{ZSX.png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/eb480c1f9a6a407389d68de4055fd3eb~tplv-k3u1fbpfcp-watermark.image?)

可以看到依赖任务被拆开成A、C、B、E、D的顺序进行执行。

## 其他使用

* 关闭打印

```java
LogUtil.openLog(false);
```

* 自定义CallBack

覆盖IDAGCallBack接口对应方法：

```JAVA
ATask a = new ATask("ATask");
a.setDAGCallBack(new IDAGCallBack() {
    @Override
    public void onStartDAGTask() {
        LogUtil.d("TestCallBack", "onStartDAGTask");
    }

    @Override
    public void onCompleteDAGTask() {
        LogUtil.d("TestCallBack", "onCompleteDAGTask");
    }
});
```

## 实现原理

掘金：https://juejin.cn/post/7027798345179463687

CSDN：https://blog.csdn.net/qq_42505007/article/details/121196089?spm=1001.2014.3001.5502
