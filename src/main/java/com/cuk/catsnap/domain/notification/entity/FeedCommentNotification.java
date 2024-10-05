package com.cuk.catsnap.domain.notification.entity;

import com.cuk.catsnap.domain.feed.entity.FeedComment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "feed_comment_notification")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FeedCommentNotification extends Notification {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="feed_comment_id")
    private FeedComment feedComment;
}