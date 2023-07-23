package com.tt.unitify.modules.post;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PostEntity extends PostDto{
    String id;
}
