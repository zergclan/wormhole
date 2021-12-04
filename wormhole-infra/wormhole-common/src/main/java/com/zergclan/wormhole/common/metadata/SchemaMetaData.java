package com.zergclan.wormhole.common.metadata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Meta data of schema.
 */
@RequiredArgsConstructor
@Getter
public final class SchemaMetaData implements MetaData {

    private final String owner;

    private final String name;

    private final Map<String, TableMetaData> tables = new LinkedHashMap<>();

}
