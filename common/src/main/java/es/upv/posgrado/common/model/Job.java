package es.upv.posgrado.common.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Job {

    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_TITLE = "title";
    public static final String ATTRIBUTE_PUBLISHED_AT = "publishedAt";
    public static final String ATTRIBUTE_STATUS = "status";
    public static final String ATTRIBUTE_RESULT = "result";
    public static final String ATTRIBUTE_REQUESTED_AT = "requestedAt";
    public static final String ATTRIBUTE_PROCESSED_AT = "processedAt";
    public static final String ATTRIBUTE_PROCESSED_BY = "processedBy";
    public static final String ATTRIBUTE_THUMBNAIL = "thumbnail";
    public static final String ATTRIBUTE_SUBMITTED_BY = "submittedBy";
    public static final String ATTRIBUTE_ELAPSED_TIME = "elapsedTime";

    private String id;
    private String title;
    private LocalDateTime publishedAt;
    private JobStatus status;
    private String result;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
    private String processedBy;
    private String thumbnail;
    private String submittedBy;
    private String elapsedTime;
}
