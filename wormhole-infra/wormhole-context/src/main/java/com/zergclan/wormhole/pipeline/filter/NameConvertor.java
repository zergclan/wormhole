package com.zergclan.wormhole.pipeline.filter;

import com.zergclan.wormhole.core.api.Filter;
import com.zergclan.wormhole.core.data.DataGroup;
import com.zergclan.wormhole.core.data.DataNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.Map;

/**
 * Name convertor implemented of {@link Filter}.
 */
@RequiredArgsConstructor
public final class NameConvertor implements Filter<DataGroup> {

    @Getter
    private final int order;

    private final Map<String, String> names;

    @Override
    public boolean doFilter(final DataGroup dataGroup) {
        Iterator<Map.Entry<String, String>> iterator = names.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            DataNode<?> dataNode = dataGroup.getDataNode(entry.getKey());
            dataNode.refreshName(entry.getValue());
        }
        return true;
    }

    @Override
    public String getType() {
        return "NAME_CONVERTOR";
    }
}
