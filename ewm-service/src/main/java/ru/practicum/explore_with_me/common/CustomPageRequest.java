package ru.practicum.explore_with_me.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class CustomPageRequest extends PageRequest {

    private final int offset;
    protected CustomPageRequest(int offset, int size, Sort sort) {
        super(offset/size, size, sort);
        this.offset = offset;
    }

    public static CustomPageRequest of(int offset, int size, Sort sort) {
        return new CustomPageRequest(offset, size, sort);
    }

    @Override
    public long getOffset() {
        return offset;
    }
}
