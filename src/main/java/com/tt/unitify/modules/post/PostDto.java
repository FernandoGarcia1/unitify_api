package com.tt.unitify.modules.post;

import lombok.Data;

@Data
public class PostDto {
    String idUser;
    String title;
    String description;
    String creationDate;
    String idPostType;
}
