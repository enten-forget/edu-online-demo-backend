package com.enten.eduService.entity.chapter;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChapterVo {
    private String id;
    private String title;
    //章节下的小节
    private List<VideoVo> children = new ArrayList<>();
}
