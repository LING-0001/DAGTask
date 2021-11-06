package work.lingling.dagtask;

import java.util.Random;

public class ETask extends IDAGTask {

    public ETask(String alias) {
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
        completeDAGTask();
    }

}
