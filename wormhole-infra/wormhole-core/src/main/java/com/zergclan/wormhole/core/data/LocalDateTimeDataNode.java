package com.zergclan.wormhole.core.data;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data node type of {@link LocalDateTime}.
 */
@RequiredArgsConstructor
public final class LocalDateTimeDataNode implements DataNode<LocalDateTime> {

    private final String name;

    private LocalDateTime value;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public LocalDateTime getValue() {
        return value;
    }

    @Override
    public DataNode<LocalDateTime> refresh(final LocalDateTime value) {
        this.value = value;
        return this;
    }
}
