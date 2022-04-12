package com.qa.platform.models.entity.question;

import com.qa.platform.models.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@ToString
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tag_tracked")
public class TrackedTag implements Serializable {

    public TrackedTag(Tag trackedTag, User user) {
        this.trackedTag = trackedTag;
        this.user = user;
    }

    private static final long serialVersionUID = 6056471660108076229L;
    @Id
    @GeneratedValue(generator = "TrackedTag_seq")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Tag trackedTag;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @CreationTimestamp
    @Column(name = "persist_date", updatable = false)
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    private LocalDateTime persistDateTime;

}
