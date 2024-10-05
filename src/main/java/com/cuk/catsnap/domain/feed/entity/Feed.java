package com.cuk.catsnap.domain.feed.entity;

import com.cuk.catsnap.domain.notification.entity.PhotographerSubscribeNotification;
import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="feed")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Feed extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="feed_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="photographer_id")
    private Photographer photographer;

    private String content;


    //OneToMany

    @OneToMany(mappedBy = "feed")
    private List<FeedPhoto> feedPhotoList;

    @OneToMany(mappedBy = "feed")
    private List<FeedLike> feedLikeList;

    @OneToMany(mappedBy = "feed")
    private List<FeedComment> feedCommentList;

    @OneToMany(mappedBy = "feed")
    private List<PhotographerSubscribeNotification> photographerSubscribeNotificationList;
}