package com.javamentor.qa.platform.models.entity.question;

import com.javamentor.qa.platform.models.entity.user.User;
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
@Table(name = "tag_ignore")
public class IgnoredTag  implements Serializable {

    public IgnoredTag(Tag ignoredTag, User user) {
        this.ignoredTag = ignoredTag;
        this.user = user;
    }

    private static final long serialVersionUID = 7657497719741178473L;
    @Id
    @GeneratedValue(generator = "IgnoreTag_seq")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Tag ignoredTag;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @CreationTimestamp
    @Column(name = "persist_date", updatable = false)
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    private LocalDateTime persistDateTime;

}
