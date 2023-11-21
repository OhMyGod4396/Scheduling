package com.example.scheduling;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

@SpringBootTest
class SchedulingApplicationTests {

    @Test
    void contextLoads() {

        // 创建一个七参数的线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5,                       // 核心线程数
                10,                      // 最大线程数
                60,                      // 线程空闲时间
                TimeUnit.SECONDS,        // 时间单位
                new ArrayBlockingQueue<>(100),  // 任务队列
                Executors.defaultThreadFactory(), // 线程工厂
                new ThreadPoolExecutor.AbortPolicy() // 拒绝策略
        );

        // 提交任务
        for (int i = 0; i < 15; i++) {
            final int taskId = i;
            executor.execute(() -> {
                System.out.println("Task " + taskId + " is running on thread " + Thread.currentThread().getName());
                try {
                    Thread.sleep(2000); // 模拟任务执行耗时
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // 关闭线程池
        executor.shutdown();
    }
    @Test
    void Test(){
        System.out.println("测试");
    }

}
