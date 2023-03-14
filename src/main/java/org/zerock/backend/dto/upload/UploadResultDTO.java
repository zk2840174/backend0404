package org.zerock.backend.dto.upload;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UploadResultDTO {

    private String uuid;

    private String fileName;

    public String getLink() {

        return uuid+"_"+fileName;
    }
}
