package com.javamentor.qa.platform.models.entity.question.answer;

import com.javamentor.qa.platform.models.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
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
import java.time.LocalDateTime;


@Entity
@Table(name = "votes_on_answers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteAnswer {

    @Id
    @GeneratedValue(generator = "AnswerVote_seq")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Answer answer;

    @CreationTimestamp
    @Column(name = "persist_date", updatable = false)
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    private LocalDateTime persistDateTime;

    @Column
    @Enumerated(EnumType.STRING)
    private VoteType vote;

    public VoteAnswer(User user, Answer answer, VoteType vote) {
        this.user = user;
        this.answer = answer;
        this.vote = vote;
    }
}
