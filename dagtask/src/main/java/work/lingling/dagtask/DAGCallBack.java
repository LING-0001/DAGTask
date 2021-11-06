package work.lingling.dagtask;

public class DAGCallBack implements IDAGCallBack {

    private final String mAlias;
    private long mStartTime;

    public DAGCallBack(String alias) {
        mAlias = alias;
    }

    @Override
    public void onStartDAGTask() {
        mStartTime = System.currentTimeMillis();
    }

    @Override
    public void onCompleteDAGTask() {
        LogUtil.d(mAlias + "执行耗时", (System.currentTimeMillis() - mStartTime) + "ms");
    }

}
