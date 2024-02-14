package cn.itcast.netty.c3;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestEventLoop {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup(2);
        // 获取下一个事件循环组
        System.out.println(group.next());
        // 执行普通任务
        group.next().execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("ok");
        });
        // 执行定时任务
        group.next().scheduleAtFixedRate(() -> {
           log.debug("ok");
        }, 0, 1, TimeUnit.SECONDS);
    }
}
