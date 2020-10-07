package util;

import com.sun.source.tree.Tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class TreeMapper extends RecursiveTask<Node> {
    final Node node;
    public TreeMapper (Node node) {
        this.node = node;
    }
    @Override
    protected Node compute() {
        Node retNode;
        String[] rawText = node.getText().split("/",2);

                List<TreeMapper> subTasks = new LinkedList<>();

        for(Node child : node.getChildren()) {
            ValueSumCounter task = new ValueSumCounter(child);
            task.fork(); // запустим асинхронно
            subTasks.add(task);
        }

        for(ValueSumCounter task : subTasks) {
            sum += task.join(); // дождёмся выполнения задачи и прибавим результат
        }

        return sum;
        return null;
    }
    static long aVeryBigSum(long[] ar) {
        long result;
        for(long dig:ar){

        }

    }
}
