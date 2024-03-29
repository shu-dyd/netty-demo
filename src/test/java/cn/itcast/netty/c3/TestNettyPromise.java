package cn.itcast.netty.c3;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class TestNettyPromise {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1.准备EventLoop对象
        EventLoop eventLoop = new NioEventLoopGroup().next();

        // 2.可以主动创建promise
        DefaultPromise<Object> promise = new DefaultPromise<>(eventLoop);

        new Thread(() -> {
            // 3.任意一个线程执行计算，计算完毕后向promise填充结果
            System.out.println("开始计算...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            promise.setSuccess(80);
        }).start();

        // 4.接受结果的线程
        log.debug("等待结果...");
        log.debug("结果是：{}", promise.get());

    }
}
