package work.lingling.dagtask;

import java.util.Random;

public class CTask extends IDAGTask {

    public CTask(String alias) {
        super(alias);
    }

    @Override
    public void run() {
        super.run();
        // 模拟随机时间
        Random random = new Random();
        try {
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        completeDAGTask();
    }

}
