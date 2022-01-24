package com.zergclan.wormhole.pipeline.handler;

import com.zergclan.wormhole.loader.Loader;
import com.zergclan.wormhole.pipeline.Handler;
import com.zergclan.wormhole.pipeline.data.BatchedDataGroup;
import lombok.RequiredArgsConstructor;

/**
 * Loaded handler.
 */
@RequiredArgsConstructor
public final class LoadedHandler implements Handler<BatchedDataGroup> {

    private final Loader loader;

    @Override
    public void handle(final BatchedDataGroup data) {
        // TODO loader data
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
