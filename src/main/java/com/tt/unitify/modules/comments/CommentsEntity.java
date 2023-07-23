package com.tt.unitify.modules.comments;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommentsEntity extends CommentsDto{
    String id;
}
