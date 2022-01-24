package com.zergclan.wormhole.pipeline.handler;

import com.zergclan.wormhole.core.data.DataNode;
import com.zergclan.wormhole.pipeline.FilterChain;
import com.zergclan.wormhole.pipeline.Handler;
import com.zergclan.wormhole.pipeline.data.BatchedDataGroup;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Merged handler.
 */
@RequiredArgsConstructor
public final class MergedHandler implements Handler<BatchedDataGroup> {

    private final Integer order;

    private final Handler<BatchedDataGroup> dataGroupHandler;

    private final Map<String, FilterChain<DataNode<?>>> filterChain = new LinkedHashMap<>();

    @Override
    public void handle(final BatchedDataGroup data) {
        // TODO handle
    }

    @Override
    public int getOrder() {
        return order;
    }
}
