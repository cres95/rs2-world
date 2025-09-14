package io.github.cres95.rs2world.net;

import io.github.cres95.rs2world.net.util.BufferLease;
import io.github.cres95.rs2world.net.util.BufferPool;
import io.github.cres95.rs2world.net.util.CleaningThread;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.*;
import java.util.function.Supplier;

@Configuration
public class ServerConfig {

    @Bean
    Selector selector() throws IOException {
        return Selector.open();
    }

    @Bean
    ServerSocketChannel serverSocketChannel(ServerProperties properties) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(properties.getHost(), properties.getPort()));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector(), SelectionKey.OP_ACCEPT);
        return serverSocketChannel;
    }

    @Bean
    ThreadLocal<BufferLease> threadLocalByteBuffers(BufferPool bufferPool) {
        return ThreadLocal.withInitial(bufferPool::lease);
    }

    @Bean
    Supplier<BufferLease> bufferSupplier(ThreadLocal<BufferLease> threadLocal) {
        return () -> {
            BufferLease lease = threadLocal.get();
            lease.buffer().clear();
            return lease;
        };
    }

    @Bean
    ThreadFactory cleaningThreadFactory(ThreadLocal<BufferLease> threadLocal) {
        return r -> new CleaningThread(r, () -> {
            BufferLease lease = threadLocal.get();
            if (lease != null) {
                lease.close();
                threadLocal.remove();
            }
        });
    }

    @Bean
    ExecutorService threadPoolExecutor(ServerProperties properties, ThreadFactory cleaningThreadFactory) {
        return new ThreadPoolExecutor(
                properties.getCorePoolSize(),
                properties.getMaxPoolSize(),
                properties.getKeepAliveDuration(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                cleaningThreadFactory);
    }

}
