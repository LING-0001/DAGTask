package work.lingling.dagtask;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class IDAGTask implements Runnable {

    private final boolean mIsSyn;
    private final AtomicInteger mAtomicInteger;
    private IDAGCallBack mDAGCallBack;
    private final Set<IDAGTask> mNextTaskSet;

    public IDAGTask() {
        this("");
    }

    public IDAGTask(boolean isSyn) {
        this("", isSyn);
    }

    public IDAGTask(String alias) {
        this(alias, false);
    }

    public IDAGTask(String alias, boolean IsSyn) {
        mIsSyn = IsSyn;
        mAtomicInteger = new AtomicInteger();
        mDAGCallBack = new DAGCallBack(alias);
        mNextTaskSet = new HashSet<>();
    }

    boolean getIsAsync() {
        return mIsSyn;
    }

    void addRely() {
        mAtomicInteger.incrementAndGet();
    }

    void deleteRely() {
        mAtomicInteger.decrementAndGet();
    }

    int getRely() {
        return mAtomicInteger.get();
    }

    void addNextDAGTask(IDAGTask DAGTask) {
        mNextTaskSet.add(DAGTask);
    }

    public void setDAGCallBack(IDAGCallBack DAGCallBack) {
        this.mDAGCallBack = DAGCallBack;
    }

    public void completeDAGTask() {
        for (IDAGTask DAGTask : mNextTaskSet) {
            DAGTask.deleteRely();
        }
        mDAGCallBack.onCompleteDAGTask();
    }

    @Override
    public void run() {
        mDAGCallBack.onStartDAGTask();
    }

}
