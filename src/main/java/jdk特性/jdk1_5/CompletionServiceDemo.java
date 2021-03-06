package jdk特性.jdk1_5;

import java.util.concurrent.*;

/**
 * Desc:  CompletionService
 * 区别于ThreadPoolExecutor take()时不按提交顺序返回 而是按完成顺序返回
 * author: DLJ
 * Date: 2017/2/7
 */
public class CompletionServiceDemo implements Callable<String> {

    private int id;

    public CompletionServiceDemo(int i) {
        this.id = i;
    }

    public static void main(String[] args) throws Exception {
        ExecutorService           service    = Executors.newCachedThreadPool();
        CompletionService<String> completion = new ExecutorCompletionService<String>(service);
        for (int i = 0; i < 10; i++) {
            completion.submit(new CompletionServiceDemo(i));
        }

        for (int i = 0; i < 10; i++) {
            System.out.println(completion.take().get());
        }
        service.shutdown();
    }

    @Override
    public String call() throws Exception {
        Integer time = (int) (Math.random() * 1000);
        try {
            System.out.println(this.id + " start");
            Thread.sleep(time);
            System.out.println(this.id + " end");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.id + ":" + time;
    }

}  
