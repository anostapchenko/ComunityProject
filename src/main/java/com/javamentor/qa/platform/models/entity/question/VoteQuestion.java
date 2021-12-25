package com.javamentor.qa.platform.models.entity.question;

import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "votes_on_questions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteQuestion implements Serializable {

    private static final long serialVersionUID = 6479035497338780810L;

    @Id
    @GeneratedValue(generator = "VoteQuestion_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @Column(name = "persist_date", updatable = false)
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    private LocalDateTime localDateTime = LocalDateTime.now();

    @Column(name = "vote")
    @Enumerated(EnumType.STRING)
    private VoteType vote;

    public VoteQuestion(User user, Question question, VoteType vote) {
        this.user = user;
        this.question = question;
        this.vote = vote;
    }
}
