package dev.aika.clienttweaks.feature;

import dev.aika.clienttweaks.boot.ModConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
import net.minecraft.util.Util;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@UtilityClass
public class PingTracker {
    private long INTERVAL_MS = TimeUnit.SECONDS.toMillis(1);
    private final long TIMEOUT_MS = TimeUnit.SECONDS.toMillis(5);

    private final AtomicReference<PendingPing> PENDING = new AtomicReference<>();

    @Setter @Getter
    private volatile long latencyMs = -1;
    private long nextPingAt;
    private boolean scheduled;

    public void tick(Minecraft minecraft) {
        final int clientPingInterval = ModConfig.get().clientPingInterval();
        if (clientPingInterval <= 0 || clientPingInterval >= 30) return;

        ClientPacketListener listener = minecraft.getConnection();

        if (listener == null || !listener.getConnection().isConnected()) {
            latencyMs = -1;
            scheduled = false;
            return;
        }

        final long now = Util.getMillis();
        PendingPing pending = PENDING.get();

        if (pending != null) {
            if (now - pending.sentAtMs() < TIMEOUT_MS) return;

            if (PENDING.compareAndSet(pending, null)) {
                latencyMs = -1;
                nextPingAt = now + INTERVAL_MS;
                scheduled = true;
            }
            return;
        }

        if (scheduled && now - nextPingAt < 0) return;

        final long token = ThreadLocalRandom.current().nextLong();
        PendingPing request = new PendingPing(token, now);

        if (!PENDING.compareAndSet(null, request)) return;

        nextPingAt = now + INTERVAL_MS;
        scheduled = true;

        listener.getConnection().send(new ServerboundPingRequestPacket(token));
    }

    public void onPong(long token) {
        PendingPing pending = PENDING.get();

        if (pending == null || pending.token() != token) return;

        if (!PENDING.compareAndSet(pending, null)) return;

        long elapsedMs = Util.getMillis() - pending.sentAtMs();
        if (elapsedMs >= 0 && elapsedMs <= TIMEOUT_MS) {
            latencyMs = (int) elapsedMs;
        }
    }

    private record PendingPing(long token, long sentAtMs) {
    }

    public static void setInterval(int value) {
        INTERVAL_MS = TimeUnit.SECONDS.toMillis(value);
    }
}