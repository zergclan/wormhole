package com.zergclan.wormhole.common.metadata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Meta data table.
 */
@RequiredArgsConstructor
@Getter
public final class TableMetaData implements MetaData {

    private final String owner;

    private final String name;

    private final Map<String, ColumnMetaData> columns = new LinkedHashMap<>();

    private final Map<String, IndexMetaData> indexes = new LinkedHashMap<>();
}
