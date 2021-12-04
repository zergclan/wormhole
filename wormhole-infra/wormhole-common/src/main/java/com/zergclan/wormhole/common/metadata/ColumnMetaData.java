package com.zergclan.wormhole.common.metadata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Meta data for column.
 */
@RequiredArgsConstructor
@Getter
public final class ColumnMetaData implements MetaData {

    private final String owner;

    private final String name;

    private final int dataType;

    private final boolean primaryKey;

    private final boolean uniqueIndex;
}
