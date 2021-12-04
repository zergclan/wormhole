package com.zergclan.wormhole.common.metadata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Meta data of index.
 */
@RequiredArgsConstructor
@Getter
public final class IndexMetaData implements MetaData {

    private final String owner;

    private final String name;
}
