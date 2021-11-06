package work.lingling.dagtask;

import android.os.Handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.os.Looper.getMainLooper;

public class DAGScheduler {

    private Set<IDAGTask> mTaskSet;
    private Map<IDAGTask, Set<IDAGTask>> mTaskMap;
    private ArrayBlockingQueue<Runnable> mTaskBlockingDeque;
    private ThreadPoolExecutor mTaskThreadPool;

    public void start(DAGProject DAGProject) {
        mTaskSet = DAGProject.getDAGTaskSet();
        mTaskMap = DAGProject.getDAGTaskMap();
        checkCircle();
        init();
        loop();
    }

    private void loop() {
        for (IDAGTask DAGTask : mTaskSet) {
            if (DAGTask.getRely() == 0) {
                mTaskBlockingDeque.add(DAGTask);
            }
        }
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(() -> {
            for (; ; ) {
                try {
                    while (!mTaskBlockingDeque.isEmpty()) {
                        IDAGTask executedDAGTsk = (IDAGTask) mTaskBlockingDeque.take();
                        if (executedDAGTsk.getIsAsync()) {
                            Handler handler = new Handler(getMainLooper());
                            handler.post(executedDAGTsk);
                        } else {
                            mTaskThreadPool.execute(executedDAGTsk);
                        }
                        mTaskSet.remove(executedDAGTsk);
                    }
                    if (mTaskSet.isEmpty()) {
                        singleThreadExecutor.shutdown();
                        mTaskThreadPool.shutdown();
                        return;
                    }
                    Iterator<IDAGTask> iterator = mTaskSet.iterator();
                    while (iterator.hasNext()) {
                        IDAGTask DAGTask = iterator.next();
                        if (DAGTask.getRely() == 0) {
                            mTaskBlockingDeque.put(DAGTask);
                            iterator.remove();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void init() {
        int codeTaskSize = Runtime.getRuntime().availableProcessors();
        int maxTaskSize = Math.max(Runtime.getRuntime().availableProcessors() * 2 + 1, mTaskSet.size());
        int keepAliveTime = 1000;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        ArrayBlockingQueue<Runnable> taskBlockingDeque = new ArrayBlockingQueue<>(maxTaskSize - codeTaskSize);
        mTaskThreadPool = new ThreadPoolExecutor(codeTaskSize, maxTaskSize, keepAliveTime, timeUnit, taskBlockingDeque);
        mTaskBlockingDeque = new ArrayBlockingQueue<>(mTaskSet.size());
    }

    private void checkCircle() {
        Set<IDAGTask> tempTaskSet = new HashSet<>(mTaskSet);
        Map<IDAGTask, Set<IDAGTask>> tempTaskMap = new HashMap<>();
        for (IDAGTask IDATask : mTaskMap.keySet()) {
            if (mTaskMap.get(IDATask) != null) {
                Set<IDAGTask> set = new HashSet<>(mTaskMap.get(IDATask));
                tempTaskMap.put(IDATask, set);
            }
        }
        LinkedList<IDAGTask> resultTaskQueue = new LinkedList<>();
        LinkedList<IDAGTask> tempTaskQueue = new LinkedList<>();
        for (IDAGTask DAGTask : tempTaskSet) {
            if (tempTaskMap.get(DAGTask) == null) {
                tempTaskQueue.add(DAGTask);
            }
        }
        while (!tempTaskQueue.isEmpty()) {
            IDAGTask tempDAGTask = tempTaskQueue.pop();
            resultTaskQueue.add(tempDAGTask);
            for (IDAGTask DAGTask : tempTaskMap.keySet()) {
                Set<IDAGTask> tempDAGSet = tempTaskMap.get(DAGTask);
                if (tempDAGSet != null && tempDAGSet.contains(tempDAGTask)) {
                    tempDAGSet.remove(tempDAGTask);
                    if (tempDAGSet.size() == 0) {
                        tempTaskQueue.add(DAGTask);
                    }
                }
            }
        }
        if (resultTaskQueue.size() != tempTaskSet.size()) {
            throw new IllegalArgumentException("相互依赖，玩屁啊，我不跑了！");
        }
    }

}
