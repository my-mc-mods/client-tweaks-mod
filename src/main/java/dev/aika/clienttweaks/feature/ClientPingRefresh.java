package dev.aika.clienttweaks.feature;

import dev.aika.clienttweaks.boot.ModConfig;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
import net.minecraft.util.Util;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class ClientPingRefresh {
    private final long TIMEOUT_MS = TimeUnit.SECONDS.toMillis(5);

    private ClientPacketListener pingListener;
    private PendingPing pending;
    private volatile Sample sample;
    private long nextPingAt;

    public void tick(Minecraft minecraft) {
        final var listener = minecraft.getConnection();
        final var request = preparePing(listener, ModConfig.get().clientPingRefresh());
        if (listener != null && request != null) {
            listener.getConnection().send(new ServerboundPingRequestPacket(request.token()));
        }
    }

    public int getLatencyMs(ClientPacketListener listener) {
        final var current = sample;
        if (current == null || listener == null || current.listener() != listener
                || !listener.getConnection().isConnected()
                || isDisabled(ModConfig.get().clientPingRefresh())) return -1;
        return current.latencyMs();
    }

    private synchronized PendingPing preparePing(ClientPacketListener listener, int interval) {
        if (isDisabled(interval) || listener == null || !listener.getConnection().isConnected()) listener = null;

        final long now = Util.getMillis();
        // A sample and any in-flight request belong to the current connection only.
        if (listener != pingListener) {
            pingListener = listener;
            pending = null;
            sample = null;
            nextPingAt = now;
        }
        if (listener == null) return null;

        final long intervalMs = TimeUnit.SECONDS.toMillis(interval);

        if (pending != null) {
            if (now - pending.sentAtMs() >= TIMEOUT_MS) {
                pending = null;
                sample = null;
                nextPingAt = now + intervalMs;
            }
            return null;
        }

        if (now - nextPingAt < 0) return null;
        nextPingAt = now + intervalMs;
        pending = new PendingPing(ThreadLocalRandom.current().nextLong(), now);
        return pending;
    }

    public synchronized void onPong(ClientPacketListener listener, long token) {
        if (listener != pingListener || pending == null || pending.token() != token) return;

        final long elapsedMs = Util.getMillis() - pending.sentAtMs();
        sample = elapsedMs >= 0 && elapsedMs <= TIMEOUT_MS ? new Sample(listener, (int) elapsedMs) : null;
        pending = null;
    }

    private boolean isDisabled(int interval) {
        return interval < 1 || interval >= 30;
    }

    private record PendingPing(long token, long sentAtMs) {
    }

    // Keeps the connection and its latency together so lock-free readers never mix them.
    private record Sample(ClientPacketListener listener, int latencyMs) {
    }
}
