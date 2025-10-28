package com.test.test.service;

import com.test.test.dto.AddGrowthRecordCommentDTO;

import java.io.IOException;

public interface GrowthRecordService {
    Long addGrowthRecordComment(AddGrowthRecordCommentDTO addGrowthRecordCommentDTO) throws IOException;
}
