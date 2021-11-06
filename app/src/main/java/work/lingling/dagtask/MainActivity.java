package work.lingling.dagtask;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }

}