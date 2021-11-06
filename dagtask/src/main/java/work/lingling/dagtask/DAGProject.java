package work.lingling.dagtask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DAGProject {

    private final Set<IDAGTask> mTaskSet;
    private final Map<IDAGTask, Set<IDAGTask>> mTaskMap;

    public DAGProject(Builder builder) {
        mTaskSet = builder.mTaskSet;
        mTaskMap = builder.mTaskMap;
    }

    Set<IDAGTask> getDAGTaskSet() {
        return mTaskSet;
    }

    Map<IDAGTask, Set<IDAGTask>> getDAGTaskMap() {
        return mTaskMap;
    }

    public static class Builder {

        private final Set<IDAGTask> mTaskSet = new HashSet<>();
        private final Map<IDAGTask, Set<IDAGTask>> mTaskMap = new HashMap<>();

        public Builder addDAGTask(IDAGTask DAGTask) {
            if (this.mTaskSet.contains(DAGTask)) {
                throw new IllegalArgumentException();
            }
            this.mTaskSet.add(DAGTask);
            return this;
        }

        public Builder addDAGEdge(IDAGTask DAGTask, IDAGTask preDAGTask) {
            if (!this.mTaskSet.contains(DAGTask) || !this.mTaskSet.contains(preDAGTask)) {
                throw new IllegalArgumentException();
            }
            Set<IDAGTask> preDAGTaskSet = this.mTaskMap.get(DAGTask);
            if (preDAGTaskSet == null) {
                preDAGTaskSet = new HashSet<>();
                this.mTaskMap.put(DAGTask, preDAGTaskSet);
            }
            if (preDAGTaskSet.contains(preDAGTask)) {
                throw new IllegalArgumentException();
            }
            DAGTask.addRely();
            preDAGTaskSet.add(preDAGTask);
            preDAGTask.addNextDAGTask(DAGTask);
            return this;
        }

        public DAGProject builder() {
            return new DAGProject(this);
        }

    }

}
